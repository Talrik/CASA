package webservices;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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
import javax.ejb.Stateless;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
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
import de.lehsten.casa.contextserver.types.entities.services.Service;
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
	CamelContext camelContext;
	ProducerTemplate serverProducer;
	ProducerTemplate factEntryProducer;
	ProducerTemplate casaDbProducer;
	private final static Logger log = LoggerFactory.getLogger( GUI_Broker_StudIP.class ); 
	private LoadingCache<HashMap<String,String>, Service[]> cache;
	
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void init(){		
		try { 
			this.layer = "Service";
			ctx = new InitialContext();
			new InitialContext().rebind("GUI_Broker_StudIP", this);
			startContext();
			factEntryProducer.setDefaultEndpoint(camelContext.getEndpoint("direct:FactEntry"));
			serverProducer.setDefaultEndpoint(camelContext.getEndpoint("direct:CASA_Server"));
			casaDbProducer.setDefaultEndpoint(camelContext.getEndpoint("direct:CASA_DB"));
			
			cache = CacheBuilder.newBuilder()
				       .maximumSize(1000)
//				       .expireAfterAccess(10, TimeUnit.MINUTES)
				       .build(
				           new CacheLoader<HashMap<String,String>, Service[]>() {
				             public Service[] load(HashMap<String,String> key) throws Exception {
				            	 
				            	if(key.get("userRole") != null){
				            		String userRole = key.get("userRole");
				            	
				            	 ArrayList<Service> resultList = new ArrayList<Service>();
				         		if(key.get("lecture") != null){
				         		try {
				         			resultList.addAll(getServiceByLectureAndUserRole(key.get("lecture"), key.get("userRole")));
				         		} catch (Exception e) {
				         			// TODO Auto-generated catch block
				         			e.printStackTrace();
				         		}
				         		}
				         		if (key.get("location") != null){
				         			String location = key.get("location");
				         				if (location.contains(";")){
				         					while(location.contains(";")){
				         						try {
				         							resultList.addAll(getServiceByLocationAndUserRole((location.substring(location.lastIndexOf(";")+1).trim()), key.get("userRole")));
				         						} catch (Exception e) {
				         							// TODO Auto-generated catch block
				         							e.printStackTrace();
				         						}
				         						location = location.substring(0, location.lastIndexOf(";"));
				         					}
				         					resultList.addAll(getServiceByLocationAndUserRole(location, userRole));
				         				}else {
				         					resultList.addAll(getServiceByLocationAndUserRole(location, userRole));
				         				}
				         		}
				         		Service[] test = new Service[resultList.size()];
				         		test = resultList.toArray(test);
				         		return test;
				            	}else return new Service[0];
				             }});


			
		} catch (NamingException e) {
			e.printStackTrace();
		}
}
	private void startContext(){
		try { 
			log.debug("Lookup context...");
			ctx = new InitialContext();
			this.camelContext = (CamelContext) ctx.lookup(layer+"Context");
			log.debug("Context found: "+ this.camelContext);
			this.camelContext = null;
		} catch (NamingException e1 ) {
			System.out.println(e1.getMessage());
		}
		if (this.camelContext == null){
			DefaultCamelContext camelContext = new DefaultCamelContext();
			log.debug("Starting context...");
			try {
				final Endpoint serverControl = (Endpoint)ctx.lookup("vm:ServerControl"); 	
				camelContext.addRoutes(new RouteBuilder(){

					@Override
					public void configure() throws Exception {
						from("direct:CASA_Server").process(new CSMessageConverter()).to(serverControl).process(new CSMessageConverter());
						from("direct:FactEntry").process(new EntitySerializer()).to((Endpoint)(new InitialContext().lookup("vm:ServerFactEntry")));	
						from("direct:CASA_DB").to("jdbc:jdbc/casa");
					}});
				camelContext.start();

				log.debug("Context started...");
				log.debug("CamelContextID: "+camelContext.getName());
				ctx.rebind("ServiceContext", camelContext);
				log.debug("Context registered: "+ ctx.lookup("ServiceContext"));
				this.camelContext = (CamelContext) ctx.lookup("ServiceContext");
			} catch (Exception e) {
				e.printStackTrace(); 

			}	
		}
		casaDbProducer = this.camelContext.createProducerTemplate();
		serverProducer = this.camelContext.createProducerTemplate();
		factEntryProducer = this.camelContext.createProducerTemplate();	
	}

	@WebMethod(operationName="getGUI") 
//	@WebResult(name = "getGUIResult") 
	public Service[] getGUI( @WebParam(name = "lecture") String lecture,@WebParam(name = "userRole") String userRole,@WebParam(name = "location") String location  ) 
	  { 
		
		HashMap<String,String> request = new HashMap<String,String>();
		request.put("lecture", lecture);
		request.put("location", location);
		request.put("userRole", userRole);
		try {
			return cache.get(request);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	  }
	
	@WebMethod(operationName="setGUI") 
//	@WebResult(name = "getGUIResult") 
	public Service[] setGUI( @WebParam(name = "lecture") String lecture,
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

		ArrayList<Entity> entityList = new ArrayList<Entity>();
		
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
	
	@WebMethod(operationName="setMediaGUI") 
	public Service[] setMediaGUI( @WebParam(name = "lecture") String lecture,
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
	
	
/*	private Link[] getServiceByUserRole(){
		
	}
*/
	private ArrayList<Service> getServiceByLectureAndUserRole(String lecture, String userRole){
		
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		ArrayList<Service> response = new ArrayList<Service>(); 
		
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
	private ArrayList<Service> getServiceByLocationAndUserRole(String location, String userRole){
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		ArrayList<Service> response = new ArrayList<Service>(); 
		
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
					}
				}
			} 
			log.info("Service request for Location " + location + " and user role " +userRole+" resulted in " + entityResp.length + " service(s).");
			
			return response;
	}	
	private class request{
		String value1;
		String value2;
		public request(String value1, String value2){
			this.value1 = value1;
			this.value1	= value2;
		}
		
	private void logToDB(){
		
	}	
	}
}

