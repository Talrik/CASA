package webservices;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.event.Event;
import de.lehsten.casa.contextserver.types.entities.event.Lecture;
import de.lehsten.casa.contextserver.types.entities.event.StudIPEvent;
import de.lehsten.casa.contextserver.types.entities.place.Place;
import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.entities.services.websites.EventWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;
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
			
		} catch (NamingException e) {
			e.printStackTrace();
		}
}
	private void startContext(){
		try { 
			System.out.println("Lookup context...");
			ctx = new InitialContext();
			this.camelContext = (CamelContext) ctx.lookup(layer+"Context");
			System.out.println("Context found: "+ this.camelContext);
			this.camelContext = null;
		} catch (NamingException e1 ) {
			System.out.println(e1.getMessage());
		}
		if (this.camelContext == null){
			DefaultCamelContext camelContext = new DefaultCamelContext();
			System.out.println("Starting context...");
			try {
				final Endpoint serverControl = (Endpoint)ctx.lookup("vm:ServerControl"); 	
				camelContext.addRoutes(new RouteBuilder(){

					@Override
					public void configure() throws Exception {
						from("direct:CASA_Server").process(new CSMessageConverter()).to(serverControl).process(new CSMessageConverter());
						from("direct:FactEntry").process(new EntitySerializer()).to((Endpoint)(new InitialContext().lookup("vm:ServerFactEntry")));	
					}});
				camelContext.start();

				System.out.println("Context started...");
				System.out.println("CamelContextID: "+camelContext.getName());
				ctx.rebind("ServiceContext", camelContext);
				System.out.println("Context registered: "+ ctx.lookup("ServiceContext"));
				this.camelContext = (CamelContext) ctx.lookup("ServiceContext");
			} catch (Exception e) {
				e.printStackTrace(); 

			}	
		}

		serverProducer = this.camelContext.createProducerTemplate();
		factEntryProducer = this.camelContext.createProducerTemplate();	
	}

	@WebMethod(operationName="getGUI") 
//	@WebResult(name = "getGUIResult") 
	public Service[] getGUI( @WebParam(name = "lecture") String lecture,@WebParam(name = "userRole") String userRole,@WebParam(name = "location") String location  ) 
	  { 
		System.out.println(lecture+userRole+location);
		int size = 0;
		ArrayList<Service> resultList = new ArrayList<Service>();
		if(lecture != null){
		resultList.addAll(getServiceByLectureAndUserRole(lecture, userRole));
		}
		if (location != null){
				if (location.contains(";")){
					while(location.contains(";")){
						resultList.addAll(getServiceByLocationAndUserRole(((location.substring(location.lastIndexOf(";")+1).trim())), userRole));
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
	  }
	
	@WebMethod(operationName="setGUI") 
//	@WebResult(name = "getGUIResult") 
	public Service[] setGUI( @WebParam(name = "lecture") String lecture,
						@WebParam(name = "userRole") String userRole,
						@WebParam(name = "url") String url,
						@WebParam(name = "title") String title,
						@WebParam(name = "description") String description,
						@WebParam(name = "location") String location)
	  { 
		
		System.out.println("Lecture:" + lecture);
		System.out.println("UserRole:" + userRole);
		System.out.println("Location:" + location);
		System.out.println("Title:" + title);
		System.out.println("URL:" + url);
		System.out.println("Description:" + description);
		try {
			System.out.println("Title:" + URLDecoder.decode(title,"UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		
			if (lecture != null){
			//create Event
			Lecture event = new Lecture();
			event.setStudIP_ID(lecture);
			event.setSource("StudIP");
			event.setBeginDate(new Date(Long.parseLong("1356998400000")));
			event.setEndDate(new Date(Long.parseLong("1356998400000")));
			//create Service
			Website service = new Website();
			service.addProperty("StudIP_ID", lecture);
			service.setSource("StudIP");
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
			factEntryProducer.sendBody(event);
			factEntryProducer.sendBody(service);
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CSMessage msg = new CSMessage();
			msg.text = "applyRules";
			serverProducer.sendBody(msg);
			return getGUI(lecture, userRole, null);
			}
			
			if (location != null){
				//create Event
				Place place = new Place();
				place.setSource("StudIP");
				place.setTitle(location);
				//create Service
				LocationWebsite service = new LocationWebsite(place);
				service.setSource("StudIP");
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
		
		System.out.println("Lecture:" + lecture);
		System.out.println("UserRole:" + userRole);
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
			System.out.println(entityResp.length + " Services received.");
				
			if(entityResp.length >0){
				for (Entity e : entityList){
					if (e instanceof Website){
						Website service = ((Website) e);
						System.out.println("Providing Title:"+service.getTitle()+" URL:"+service.getTargetURL());
						response.add(service);
					}
				}
			} 
			return response;
		
	}
	private ArrayList<Service> getServiceByLocationAndUserRole(String location, String userRole){
		System.out.println("Location:" + location);
		System.out.println("UserRole:" + userRole);
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
			System.out.println(entityResp.length + " Services received.");
				
			if(entityResp.length >0){
				for (Entity e : entityList){
					if (e instanceof Website){
						Website service = ((Website) e);
						System.out.println("Providing Title:"+service.getTitle()+" URL:"+service.getTargetURL());
						response.add(service);
					}
				}
			} 
			return response;
	
	}
	
}
