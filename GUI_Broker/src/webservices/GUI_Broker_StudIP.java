package webservices;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.seda.SedaEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.event.Event;
import de.lehsten.casa.contextserver.types.entities.event.Lecture;
import de.lehsten.casa.contextserver.types.entities.event.StudIPEvent;
import de.lehsten.casa.contextserver.types.entities.place.Place;
//import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.entities.services.websites.EventWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.MediaWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;
import de.lehsten.casa.contextserver.types.media.Media;
import de.lehsten.casa.contextserver.types.xml.CSMessage;
import de.lehsten.casa.utilities.communication.serializing.CSMessageConverter;
import de.lehsten.casa.utilities.communication.serializing.EntitySerializer;

/**
 * @author phil
 *
 */
@WebService(serviceName = "GUI_Broker_StudIP")
@Stateless()
public class GUI_Broker_StudIP {
	
	String ContextServerIP;
	String layer;
	int ContextServerPort;
	InitialContext ctx;
	DefaultCamelContext camelContext;
	ProducerTemplate serverProducer;
	ProducerTemplate factEntryProducer;
	ProducerTemplate casaDbProducer;
	private final static Logger log = LoggerFactory.getLogger( GUI_Broker_StudIP.class ); 
	private LoadingCache<HashMap<String,String>, Website[]> cache = null;	
	private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	private boolean connected = false;
	private JmDNS jmdns;
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void init(){	
		long start = System.currentTimeMillis();
		try { 
			this.layer = "Service";
			ctx = new InitialContext();
			new InitialContext().rebind("GUI_Broker_StudIP", this);
			  	jmdns = JmDNS.create();
	            log.info("Host: " + jmdns.getHostName() );
	            log.info("Interface: " + jmdns.getInterface() );
	            String type = "_casa._tcp.local.";
	            jmdns.addServiceListener(type, new SampleListener());
	    } catch (IOException e) {
	        e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		long finish = System.currentTimeMillis();
		long dur = finish - start;
		log.info("Startup of Webservice took " + dur +" ms.");
	}
	
	private void connectToServer(){
		startContext();
		factEntryProducer.setDefaultEndpoint(camelContext.getEndpoint("direct:FactEntry"));
		serverProducer.setDefaultEndpoint(camelContext.getEndpoint("direct:CASA_Server"));
		casaDbProducer.setDefaultEndpoint(camelContext.getEndpoint("direct:CASA_DB"));
		cache = this.brokerCache();
		connected = true;
		log.info("Connected to Server");
		
	}
	
	private void disconnectFromServer(){
		connected = false;
		log.info("Lost connection to Server...");
	}
	
	private void startContext(){
		try { 
			log.info("Lookup context...");
			ctx = new InitialContext();

			this.camelContext = (DefaultCamelContext) ctx.lookup(layer+"Context");
			log.debug("Context found: "+ this.camelContext);
			this.camelContext = null;
		} catch (NamingException e1 ) {
			log.error(e1.getMessage());
		}
		if (this.camelContext == null){
			DefaultCamelContext camelContext = new DefaultCamelContext();
			log.debug("Starting context...");
			try {
				camelContext.addRoutes(new RouteBuilder(){
					@Override
					public void configure() throws Exception {
						from("direct:CASA_Server").process(new CSMessageConverter()).to("vm:ServerControl").process(new CSMessageConverter());
						from("direct:FactEntry").process(new EntitySerializer()).to("vm:ServerFactEntry");	
						from("direct:CASA_DB").to("jdbc:jdbc/casa");
					}});
				camelContext.start();

				log.debug("Context started...");
				log.debug("CamelContextID: "+camelContext.getName());
				ctx.rebind("ServiceContext", camelContext);
				log.debug("Context registered: "+ ctx.lookup("ServiceContext"));
				this.camelContext = (DefaultCamelContext) ctx.lookup("ServiceContext");
			} catch (Exception e) {
				e.printStackTrace(); 

			}	
		}
		casaDbProducer = this.camelContext.createProducerTemplate();
		serverProducer = this.camelContext.createProducerTemplate();
		factEntryProducer = this.camelContext.createProducerTemplate();	
	}
	
	@PreDestroy
	private void stopContext(){
		try {
			jmdns.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@WebMethod(operationName="getGUI") 
//	@WebResult(name = "getGUIResult") 
	public Website[] getGUI( @WebParam(name = "lecture") String lecture,@WebParam(name = "userRole") String userRole,@WebParam(name = "location") String location  ) 
	  { 	
		long start = System.currentTimeMillis();
		/*
		try {
			String utf8location = location;
			String newlocation = new String(utf8location.getBytes(), "ISO-8859-1");
			log.info(newlocation);
			newlocation = new String(utf8location.getBytes(), "ISO-8859-15");
			log.info(newlocation);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		if (connected){
		HashMap<String,String> request = new HashMap<String,String>();
		request.put("lecture", lecture);
		request.put("location", location);
		request.put("userRole", userRole);
		try {
			Website[] result = cache.get(request);
			long finish = System.currentTimeMillis();
			long dur = finish - start;
			log.debug("Request to Webservice took " + dur +" ms.");
			return result;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		long finish = System.currentTimeMillis();
		long dur = finish - start;
		log.debug("Request to Webservice took " + dur +" ms.");
		log.error("Not connected to CASA-Server");
		return null;
	  }
	
	@WebMethod(operationName="setGUI") 
	public Website setGUI( @WebParam(name = "lecture") String lecture,
						@WebParam(name = "userRole") String userRole,
						@WebParam(name = "url") String url,
						@WebParam(name = "title") String title,
						@WebParam(name = "description") String description,
						@WebParam(name = "location") String location,
						@WebParam(name = "author") String author)
	  { 
		log.info( "User "+ author 
				+ " requested to add " + title 
				+ " to lecture " + lecture 
				+ " and/or Location " + location
				+ " for " + userRole 
				+ ". It is described as " + description
				+ " and the path is " + url);
		if (connected){
			
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		Website service = new Website();
		if (lecture != null){
		CSMessage msg = new CSMessage();
		Lecture event = new Lecture();;
		msg.text = "getQueryResult";
		msg.payload.add("GetEntityByPropertyAndValue");
		String key = "StudIP_ID";
		String value = lecture;
		Object[] params = {key, value};
		msg.payload.add(params);
		CSMessage rulesMsg = (CSMessage) serverProducer.requestBody(msg);
		if (rulesMsg.payload.size() != 0){
			for (Object o : rulesMsg.payload){
				if (o instanceof Lecture){
					event = (Lecture)o;
				}
			}
		}else{
			//create Event
			event = new Lecture();
			event.setStudIP_ID(lecture);
			event.setSource("StudIP");
			event.setBeginDate(new Date(Long.parseLong("1356998400000")));
			event.setEndDate(new Date(Long.parseLong("1356998400000")));
			addEntity(event);
		}
			//create Service
			service.addProperty("StudIP_ID", lecture);
			service.setSource(author+"@StudIP");
			service.setProvider(author);
			service.setTargetURL(url);
			service.setTitle(title);
			if (userRole != null){
				if (userRole.contains(";")){
					while(userRole.contains(";")){
					service.addRestriction(((userRole.substring(userRole.lastIndexOf(";")+1).trim())));
					userRole = userRole.substring(0, userRole.lastIndexOf(";"));
					}
					service.addRestriction(userRole);
				}else
				service.addRestriction(userRole);
			}
			if (description != null){
				service.setDescription(description);
			}
			addEntity(service);
			
			Endpoint endpoint = this.camelContext.getEndpoint("direct:CASA_DB");
			Exchange exchange = endpoint.createExchange();
			JAXBContext context;
			try {
			context = JAXBContext.newInstance( service.getClass() );
			Marshaller m = context.createMarshaller();
			m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
			java.io.StringWriter sw = new StringWriter();
			m.marshal( service, sw );
			String serviceXML = sw.toString();
			
			exchange.getIn().setBody("INSERT INTO services (title, description, url, userrole, lecture, serviceXML) VALUES ('"+service.getTitle()+"','"+service.getDescription()+"','"+service.getTargetURL()+"','"+service.getRestrictions().toString()+"','"+event.getStudIP_ID()+"','"+serviceXML+"')");

			casaDbProducer.send(endpoint, exchange); 
			}
			catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			msg = new CSMessage();
			msg.text = "applyRules";
			serverProducer.sendBody(msg);
			cache.invalidateAll();
			Website[] results = getGUI(lecture, userRole, location);
			for (Website w : results){
				if (w.getDescription().equals(description) && w.getTitle().equals(title) && w.getTargetURL().toString().equals(url)){
					return w;
				}
			}
			}
			
			if (location != null){
				//create Event
				Place place = new Place();
				place.setSource("StudIP");
				place.setTitle(location);
				//create Service
				service.setSource(author+"@StudIP");
				service.setTargetURL(url);
				service.setTitle(title);
				service.setProvider(author);
				service.addProperty("StudIP_Location", location);
				if (userRole != null){
					if (userRole.contains(";")){
						while(userRole.contains(";")){
						service.addRestriction(((userRole.substring(userRole.lastIndexOf(";")+1).trim())));
						userRole = userRole.substring(0, userRole.lastIndexOf(";"));
						}
						service.addRestriction(userRole);
					}else
					service.addRestriction(userRole);
				}
				if (description != null){
					service.setDescription(description);
				}
				addEntity(place);
				addEntity(service);
				
				Endpoint endpoint = this.camelContext.getEndpoint("direct:CASA_DB");
				Exchange exchange = endpoint.createExchange();
				JAXBContext context;
				try {
				context = JAXBContext.newInstance( service.getClass() );
				Marshaller m = context.createMarshaller();
				m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
				java.io.StringWriter sw = new StringWriter();
				m.marshal( service, sw );
				String serviceXML = sw.toString();
				
				exchange.getIn().setBody("INSERT INTO services (title, description, url, userrole, location, serviceXML) VALUES ('"+service.getTitle()+"','"+service.getDescription()+"','"+service.getTargetURL()+"','"+service.getRestrictions().toString()+"','"+place.getTitle()+"','"+serviceXML+"')");

				// now we send the exchange to the endpoint, and receives the response from Camel
				Exchange out = casaDbProducer.send(endpoint, exchange); 
				}
				catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				CSMessage msg = new CSMessage();
				msg.text = "applyRules";
				serverProducer.sendBody(msg);
				cache.invalidateAll();
				Website[] results = getGUI(lecture, userRole, location);
				for (Website w : results){
					if (w.getDescription().equals(description) && w.getTitle().equals(title) && w.getTargetURL().toString().equals(url)){
						return w;
					}
				}
				}
		}
		log.error("Not connected to CASA-Server");
		return null;
			
	  }

	@WebMethod(operationName="removeService")
	public Website[] removeService( @WebParam(name = "PropertyKey") String key,
			@WebParam(name = "PropertyValue") String value 
//			@WebParam(name = "UserName") String userName
			) 
	{
		log.info("Requesting service removal");
		// get the old entity by property
		CSMessage msg = new CSMessage();
		Website oldEntity = new Website();
		Website newEntity = new Website();
		msg.text = "getQueryResult";
		msg.payload.add("GetEntityByPropertyAndValue");
		String msgKey = key;
		String msgValue = value;
		Object[] params = {key, value};
		msg.payload.add(params);
		CSMessage rulesMsg = (CSMessage) serverProducer.requestBody(msg);
		if (rulesMsg.payload.size() == 1){
			for (Object o : rulesMsg.payload){
				if (o instanceof Website){
					oldEntity = (Website)o;
				}
			}
			CSMessage msg2 = new CSMessage();
			msg2.text = "removeEntity";
			msg2.payload.add(oldEntity);
			serverProducer.requestBody(msg2);	
			cache.invalidateAll();
			rulesMsg = (CSMessage) serverProducer.requestBody(msg);
			if (rulesMsg.parameter.size() ==0){
			log.info("Service "+ oldEntity.toString() +" removed.");	
			return new Website[0];
			}
			else{ 
				log.error("Service "+ oldEntity.toString() +" cound not be removed.");		
				return null;
			}
		}
		else // more than one result
		{
			log.info("Found "+ rulesMsg.payload.size() +" services. - Removal not allowed.");	
			return null;
		}
	}
	
	@WebMethod(operationName="updateService")
	public Website updateService( @WebParam(name = "PropertyKey") String key,
			@WebParam(name = "PropertyValue") String value,
			@WebParam(name = "userRole") String userRole,
			@WebParam(name = "url") String url,
			@WebParam(name = "title") String title,
			@WebParam(name = "description") String description,
			@WebParam(name = "location") String location,
			@WebParam(name = "author") String author){
		log.info("Request updateService()");
		log.info( "User "+ author 
				+ " requested to update " + value 
				+ " to title " + title
				+ " and/or Location " + location
				+ " for " + userRole 
				+ ". It is described as " + description
				+ " and the path is " + url);

		cache.invalidateAll();
		if (key == null){
			log.error("Specified key was " + key + ". Please specify valid identification key");
			return null;
		}
		if (value == null){
			log.error("Specified value was " + value + ". Please specify valid identification value");
			return null;
		}
		// get the old entity by property
		CSMessage msg = new CSMessage();
		Website oldEntity = new Website();
		Website newEntity = new Website();
		msg.text = "getQueryResult";
		msg.payload.add("GetEntityByPropertyAndValue");
		Object[] params = {key, value};
		msg.payload.add(params);
		CSMessage rulesMsg = (CSMessage) serverProducer.requestBody(msg);
		if (rulesMsg.payload.size() == 1){
			for (Object o : rulesMsg.payload){
				if (o instanceof Website){
					oldEntity = (Website)o;
				}
			}
			//clone the old entity to create the new one
			newEntity = (Website) oldEntity.clone();
			//modify the new entity according to the request
			if (userRole != null){
				ArrayList<String> restrictions = new ArrayList<String>();
				if (userRole.contains(";")){
					while(userRole.contains(";")){
						restrictions.add(((userRole.substring(userRole.lastIndexOf(";")+1).trim())));
					userRole = userRole.substring(0, userRole.lastIndexOf(";"));
					}
					restrictions.add(userRole);
				}else
				restrictions.add(userRole);
				if (!oldEntity.getRestrictions().equals(restrictions)){
					newEntity.setRestrictions(restrictions);
				}
			}
			if(location != null && !location.equals(oldEntity.getProperties().get("StudIP_Location"))){
				log.info("Neue Location");
				Place place = new Place();
				place.setSource("StudIP");
				place.setTitle(location);
				this.addEntity(place);
				newEntity.getProperties().put("StudIP_Location", location);
				ArrayList<Place> places = new ArrayList<Place>();
				places.add(place);
				((LocationWebsite)newEntity).setPlaces(places);
			}
			//TODO			location			
			if(url != null && !url.equals(oldEntity.getTargetURL())){
				newEntity.setTargetURL(url);
			}
			if(title != null && !title.equals(oldEntity.getTitle())){
				newEntity.setTitle(title);
			} 
			if(description != null && !description.equals(oldEntity.getDescription())){
				newEntity.setDescription(description);
			}
			if(author != null && !author.equals(oldEntity.getProvider())){
				if (oldEntity.getProvider() != null){
				newEntity.setProvider(author);
				}
			}
			//send the update request
			cache.invalidateAll(); 
			log.info("Old : " +oldEntity.toString());
			log.info("New : " +newEntity.toString());
			this.removeService(key, value);
			this.addEntity(newEntity);
			// wait till the entity has been added
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg = new CSMessage();
			msg.text = "getQueryResult";
			msg.payload.add("GetEntityByPropertyAndValue");
			String msgKey = "ID";
			String msgValue = newEntity.getProperties().get("ID");
			Object[] verifyParams = {msgKey, msgValue};
			msg.payload.add(verifyParams);
			rulesMsg = (CSMessage) serverProducer.requestBody(msg);
			if (rulesMsg.payload.size() == 1){
				for (Object o : rulesMsg.payload){
					if (o instanceof Website){
						newEntity = (Website)o;
						log.info(newEntity.toString());
						return newEntity; 
					}
				}
			}
			else{
				log.info(rulesMsg.payload.size() + " services found with the new id. Something went wrong...");
				return null;
			}
		}else{
		log.info(rulesMsg.payload.size() + " services found with this id. Update was not possible. DEBUG2");
		return null;
		}
		log.info("This should not happen ...");
		return null;
	}
	
	@WebMethod(operationName="setMediaGUI") 
	public Website[] setMediaGUI( @WebParam(name = "lecture") String lecture,
						@WebParam(name = "userRole") String userRole,
						@WebParam(name = "path") String url,
						@WebParam(name = "mediaType") String mediaType,
						@WebParam(name = "title") String title,
						@WebParam(name = "description") String description,
						@WebParam(name = "location") String location,
						@WebParam(name = "author") String author)
	  { 
		
		log.info( "User "+ author 
					+ " requested to add the "+ mediaType + " " + title 
					+ " to lecture " + lecture 
					+ " and/or Location " + location
					+ " for " + userRole 
					+ ". It is described as " + description
					+ " and the path is " + url);

		ArrayList<Entity> entityList = new ArrayList<Entity>();
		
		/* Hallo Stefan!
		 * 
		 * Hier kannst du deine Gamification einbinden
		 * 	Zum Beispiel so: 
		 * 	Media media = new Media();
		 *	media.setMediaType(mediaType);
		 *	media.setPath(url);
		 *	media.setTitle(title);
		 *	media.setAuthor(author);
		 *	media.addConnection(lecture);
		 *
		 *  gamificationProducer.sendBody(media);
		*/
		
		Media media = new Media();
		media.setMediaType(mediaType);
		media.setPath(url);
		media.setTitle(title);
		media.setAuthor(author);
		media.addConnection(lecture);
		
		
		
		if (lecture != null){
		CSMessage msg = new CSMessage();
		Lecture event = new Lecture();;
		msg.text = "getQueryResult";
		msg.payload.add("GetEntityByPropertyAndValue");
		String key = "StudIP_ID";
		String value = lecture;
		Object[] params = {key, value};
		msg.payload.add(params);
		CSMessage rulesMsg = (CSMessage) serverProducer.requestBody(msg);
		if (rulesMsg.payload.size() != 0){
			for (Object o : rulesMsg.payload){
				if (o instanceof Lecture){
					event = (Lecture)o;
				}
			}
		}else{
			//create Event
			event = new Lecture();
			event.setStudIP_ID(lecture);
			event.setSource("StudIP");
			event.setBeginDate(new Date(Long.parseLong("1356998400000")));
			event.setEndDate(new Date(Long.parseLong("1356998400000")));
			factEntryProducer.sendBody(event);
		}
			//create Service
			Website service = new Website();
			service.addProperty("StudIP_ID", lecture);
			service.setSource(author+"@StudIP");
			service.setTargetURL(url);
			service.setTitle(title);
			if (userRole != null){
				if (userRole.contains(";")){
					while(userRole.contains(";")){
					service.addRestriction(((userRole.substring(userRole.lastIndexOf(";")+1).trim())));
					userRole = userRole.substring(0, userRole.lastIndexOf(";"));
					}
					service.addRestriction(userRole);
				}else
				service.addRestriction(userRole);
			}
			if (description != null){
				service.setDescription(description);
			}
			factEntryProducer.sendBody(service);
			
			Endpoint endpoint = this.camelContext.getEndpoint("direct:CASA_DB");
			Exchange exchange = endpoint.createExchange();
			JAXBContext context;
			try {
			context = JAXBContext.newInstance( service.getClass() );
			Marshaller m = context.createMarshaller();
			m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
			java.io.StringWriter sw = new StringWriter();
			m.marshal( service, sw );
			String serviceXML = sw.toString();
			
			exchange.getIn().setBody("INSERT INTO services (title, description, url, userrole, lecture, serviceXML) VALUES ('"+service.getTitle()+"','"+service.getDescription()+"','"+service.getTargetURL()+"','"+service.getRestrictions().toString()+"','"+event.getStudIP_ID()+"','"+serviceXML+"')");

			casaDbProducer.send(endpoint, exchange); 
			}
			catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg = new CSMessage();
			msg.text = "applyRules";
			serverProducer.sendBody(msg);
			cache.invalidateAll();
			return getGUI(lecture, userRole, null);
			}
			
			if (location != null){
				//create Event
				Place place = new Place();
				place.setSource("StudIP");
				place.setTitle(location);
				//create Service
				LocationWebsite service = new LocationWebsite(place);
				service.setSource(author+"@StudIP");
				service.setTargetURL(url);
				service.setTitle(title);
				if (userRole != null){
					if (userRole.contains(";")){
						while(userRole.contains(";")){
						service.addRestriction(((userRole.substring(userRole.lastIndexOf(";")+1).trim())));
						userRole = userRole.substring(0, userRole.lastIndexOf(";"));
						}
						service.addRestriction(userRole);
					}else
					service.addRestriction(userRole);
				}
				if (description != null){
					service.setDescription(description);
				}
				factEntryProducer.sendBody(place);
				factEntryProducer.sendBody(service);

				
				Endpoint endpoint = this.camelContext.getEndpoint("direct:CASA_DB");
				Exchange exchange = endpoint.createExchange();
				JAXBContext context;
				try {
				context = JAXBContext.newInstance( service.getClass() );
				Marshaller m = context.createMarshaller();
				m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
				java.io.StringWriter sw = new StringWriter();
				m.marshal( service, sw );
				String serviceXML = sw.toString();
				
				exchange.getIn().setBody("INSERT INTO services (title, description, url, userrole, location, serviceXML) VALUES ('"+service.getTitle()+"','"+service.getDescription()+"','"+service.getTargetURL()+"','"+service.getRestrictions().toString()+"','"+place.getTitle()+"','"+serviceXML+"')");

				// now we send the exchange to the endpoint, and receives the response from Camel
				Exchange out = casaDbProducer.send(endpoint, exchange); 
				}
				catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				CSMessage msg = new CSMessage();
				msg.text = "applyRules";
				serverProducer.sendBody(msg);
				return getGUI(null, userRole, location);
				}
			return null;
			
	  }
	
	private ArrayList<Website> getServiceByLectureAndUserRole(String lecture, String userRole){
		
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		ArrayList<Website> response = new ArrayList<Website>(); 
		
			CSMessage msg = new CSMessage();
			
			msg.text = "getQueryResult";
			msg.payload.add("GetServiceByLectureAndRole");
			Object[] params = {lecture, userRole};
			msg.payload.add(params);
			CSMessage rulesMsg = (CSMessage) serverProducer.requestBody(msg);
			for (Object o :rulesMsg.payload){
				if (o instanceof Entity){
					entityList.add((Entity)o);
				}
			} 
			Entity[] entityResp = new Entity[entityList.size()];
			entityList.toArray(entityResp);
				
			if(entityResp.length >0){
				for (Entity e : entityList){
					if (e instanceof Website){
						Website service = ((Website) e);
						log.debug("Providing Title:"+service.getTitle()+" URL:"+service.getTargetURL());
						response.add(service);
					}
				}
			} 
			log.info("Service request for Lecture " + lecture + " and user role " +userRole+" resulted in " + entityResp.length + " service(s).");
			
			return response;
	}
	
	private void addEntity(Entity e){
		this.factEntryProducer.sendBody(e);
	}
	
	private ArrayList<Website> getServiceByLocationAndUserRole(String location, String userRole){
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		ArrayList<Website> response = new ArrayList<Website>(); 
		
			CSMessage msg = new CSMessage();
			
			msg.text = "getQueryResult";
			msg.payload.add("GetServiceByLocationAndRole");
			Object[] params = {location, userRole};
			msg.payload.add(params);
			CSMessage rulesMsg = (CSMessage) serverProducer.requestBody(msg);
			
			for (Object o :rulesMsg.payload){
				if (o instanceof Entity){
					entityList.add((Entity)o);
				}
			}
			Entity[] entityResp = new Entity[entityList.size()];
			entityList.toArray(entityResp);
				
			if(entityResp.length >0){
				for (Entity e : entityList){
					if (e instanceof Website){
						Website service = ((Website) e);
						log.debug("Providing Title:"+service.getTitle()+" URL:"+service.getTargetURL());
						response.add(service);
					}else{
						log.error("Response contained "+e.getClass()+", expected "+Website.class );
					}
				}
			} 
			log.info("Service request for Location " + location + " and user role " +userRole+" resulted in " + response.size() + " service(s).");
			
			return response;
	}	
	private class request{
		String value1;
		String value2;
		public request(String value1, String value2){
			this.value1 = value1;
			this.value1	= value2;
		}
	}	
		
	private void logToDB(){
		
	}
	
     class SampleListener implements ServiceListener {
        @Override
        public void serviceAdded(ServiceEvent event) {
            System.out.println("Service added   : " + event.getName() + "." + event.getType());
            ServiceInfo info = event.getDNS().getServiceInfo(event.getType(), event.getName());
            System.out.println("Service info: " + info);
      
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
            System.out.println("Service removed : " + event.getName() + "." + event.getType());
            if (event.getName().equals("ContextServer")){
            disconnectFromServer();
            }
        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            System.out.println("Service resolved: " + event.getInfo());
            log.info(event.getName());
            if (event.getName().equals("ContextServer")){
            connectToServer();
            }
        }
    }
	
	private LoadingCache<HashMap<String, String>, Website[]> brokerCache() {
			cache = CacheBuilder.newBuilder()
			       .maximumSize(1000)
//			       .expireAfterAccess(10, TimeUnit.MINUTES)
			       .build(
			           new CacheLoader<HashMap<String,String>, Website[]>() {
			             public Website[] load(HashMap<String,String> key) throws Exception {
			            	 
			            	 if(key.get("userRole") != null){
			            		 ArrayList<Website> resultList = new ArrayList<Website>();
			            		 ArrayList<Website> tempResult = new ArrayList<Website>();
			            		 if(key.get("lecture") != null){
			            			 String userRole = key.get("userRole");
			            			 try {
			            				 if (userRole.contains(";")){
			            					 while(userRole.contains(";")){
			            						 try {
			            							 tempResult = getServiceByLectureAndUserRole(key.get("lecture"),(userRole.substring(userRole.lastIndexOf(";")+1).trim()));
			            							 for (Website w : tempResult){
			            								 if (!resultList.contains(w)){
			            										 resultList.add(w);
			            								 }
			            							 }
			            						 } catch (Exception e) {
			            							 // TODO Auto-generated catch block
			            							 e.printStackTrace();
			            						 }
			            						 userRole = userRole.substring(0, userRole.lastIndexOf(";"));
			            					 }
			            					 tempResult = getServiceByLectureAndUserRole(key.get("lecture"), userRole);
			            					 for (Website w : tempResult){
	            								 if (!resultList.contains(w)){
	            										 resultList.add(w);
	            								 }
	            							 }
			            				 } 
			            				 else
			            					tempResult = getServiceByLectureAndUserRole(key.get("lecture"), key.get("userRole"));
			            				 	for (Website w : tempResult){
			            				 		if (!resultList.contains(w)){
         										 resultList.add(w);
			            				 		}
			            				 	}
			            			 } catch (Exception e) {
			            				 e.printStackTrace();
			            			 }
			            		 }
			            		 if (key.get("location") != null){
			            			 String userRole = key.get("userRole");
			            			 String location = key.get("location");
			            			 if (userRole.contains(";")){
			            				 if(location.contains(";")){
			            					 while(location.contains(";")){
			            						 String tempLocation = location.substring(location.lastIndexOf(";")+1).trim();
			            						 while(userRole.contains(";")){
			            							 try {
			            								 tempResult = getServiceByLocationAndUserRole(tempLocation,(userRole.substring(userRole.lastIndexOf(";")+1).trim()));
			            								 for (Website w : tempResult){
				            								 if (!resultList.contains(w)){
				            										 resultList.add(w);
				            								 }
				            							 }
			            							 } catch (Exception e) {
			            								 // TODO Auto-generated catch block
			            								 e.printStackTrace();
			            							 }
			            							 userRole = userRole.substring(0, userRole.lastIndexOf(";"));
			            						 }
			            						 tempResult = getServiceByLocationAndUserRole(tempLocation, userRole);
			            						 for (Website w : tempResult){
		            								 if (!resultList.contains(w)){
		            										 resultList.add(w);
		            								 }
		            							 }
			            						 location = location.substring(0, location.lastIndexOf(";"));
			            					 }
			            					 userRole = key.get("userRole");
			            					 while(userRole.contains(";")){
			            						 try {
			            							 tempResult = getServiceByLocationAndUserRole(location,(userRole.substring(userRole.lastIndexOf(";")+1).trim()));
			            							 for (Website w : tempResult){
			            								 if (!resultList.contains(w)){
			            										 resultList.add(w);
			            								 }
			            							 }
			            						 } catch (Exception e) {
			            							 // TODO Auto-generated catch block
			            							 e.printStackTrace();
			            						 }
			            						 userRole = userRole.substring(0, userRole.lastIndexOf(";"));
			            					 }
			            					 tempResult = getServiceByLocationAndUserRole(location, userRole);
			            					 for (Website w : tempResult){
	            								 if (!resultList.contains(w)){
	            										 resultList.add(w);
	            								 }
	            							 }
			            				 }
			            				 else{
			            					 while(userRole.contains(";")){
			            						 try {
			            							 tempResult = getServiceByLocationAndUserRole(key.get("location"),(userRole.substring(userRole.lastIndexOf(";")+1).trim()));
			            							 for (Website w : tempResult){
			            								 if (!resultList.contains(w)){
			            										 resultList.add(w);
			            								 }
			            							 }
			            						 } catch (Exception e) {
			            							 // TODO Auto-generated catch block
			            							 e.printStackTrace();
			            						 }
			            						 userRole = userRole.substring(0, userRole.lastIndexOf(";"));
			            					 }
			            					 tempResult = getServiceByLocationAndUserRole(key.get("location"), userRole);
			            					 for (Website w : tempResult){
	            								 if (!resultList.contains(w)){
	            										 resultList.add(w);
	            								 }
	            							 }
			            				 }
			            			 } 
			            			 else{
			            				 if (location.contains(";")){
			            					 while(location.contains(";")){
			            						 try {
			            							 tempResult= getServiceByLocationAndUserRole((location.substring(location.lastIndexOf(";")+1).trim()), key.get("userRole"));
			            							 for (Website w : tempResult){
			            								 if (!resultList.contains(w)){
			            										 resultList.add(w);
			            								 }
			            							 }
			            						 } catch (Exception e) {
			            							 e.printStackTrace();
			            						 }
			            						 location = location.substring(0, location.lastIndexOf(";"));
			            					 }
			            					 tempResult = getServiceByLocationAndUserRole(location, userRole);
			            					 for (Website w : tempResult){
	            								 if (!resultList.contains(w)){
	            										 resultList.add(w);
	            								 }
	            							 }
			            				 }else {
			            					 tempResult = getServiceByLocationAndUserRole(location, userRole);
			            					 for (Website w : tempResult){
	            								 if (!resultList.contains(w)){
	            										 resultList.add(w);
	            								 }
	            							 }
			            				 }
			            			 }

			            	 }
			            	 Website[] test = new Website[resultList.size()];
			            	 test = resultList.toArray(test);
			            	 return test;
			             }// no userRole
			             else return new Website[0];
			           }});
			return cache;
	}
	}


