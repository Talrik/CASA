package de.lehsten.casa.mobile.data;

import java.io.Serializable;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Producer;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.place.Place;
import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.entities.services.websites.EventWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;
import de.lehsten.casa.contextserver.types.xml.CSMessage;
import de.lehsten.casa.utilities.communication.serializing.CSMessageConverter;


public class ServiceHandler implements Serializable{
	
	private static final long serialVersionUID = 1L;
	InitialContext ctx; 
	private static boolean isConnected = false; 
	private static ProducerTemplate serverProducer;
//	private CamelContext camelContext;
	private static ArrayList<Service> services = new ArrayList<Service>();
	public static ArrayList<ServiceProxy> serviceProxies = new ArrayList<ServiceProxy>();

	public ServiceHandler(){
		// Try to connect to Server
		connectToServer();
		
		ServiceProxy stops = new ServiceProxyImp();
		stops.setQuery("GetCloseStopWebsites");
		Object[] params = new Object[2];
		params[0] = 54.0744279d;
		params[1] = 12.1035669d;
		stops.setParams(params);
		stops.setServieType(LocationWebsite.class);
		stops.setTitle("Haltestellen");
		stops.setDescription("Haltestellen in einem Umkreis von 1km.");
		this.addServiceProxy(stops);
		
		ServiceProxy events = new ServiceProxyImp();
		events.setQuery("GetCloseStopWebsites");
		Object[] eventsparams = new Object[2];
		params[0] = 54.0744279d;
		params[1] = 12.1035669d;
		events.setParams(eventsparams);
		events.setServieType(LocationWebsite.class);
		events.setTitle("Haltestellen");
		events.setDescription("Haltestellen in einem Umkreis von 1km.");
		this.addServiceProxy(events);
		
		refresh();
	}

	public void connectToServer(){
		// Lookup if there is a context server started here
		try{
			ctx = new InitialContext();
			CamelContext camelContext = (CamelContext)ctx.lookup("MobileContext");
			serverProducer = camelContext.createProducerTemplate();
			if (camelContext.hasEndpoint("direct:CASA_Server") != null)
			{
			serverProducer.setDefaultEndpoint(camelContext.getEndpoint("direct:CASA_Server"));
			System.out.println("Connected to ContextServer");
			this.isConnected = true;
			}
		}catch(NamingException ne){
			System.out.println("No ContextServer found - Using dummy data...");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static ArrayList<Entity> getQueryResult(String query, Object[] params){
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		if (isConnected){
			try 
			{
				CSMessage msg = new CSMessage();
				msg.text = "getQueryResult";
				msg.payload.add(query);
				msg.payload.add(params);
				
				CSMessage entityMsg = (CSMessage) serverProducer.requestBody(msg);
				ArrayList<Object> objectList = entityMsg.payload;
				for (Object o : objectList){
					if (o instanceof Entity){
						entityList.add((Entity)o);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else {
			LocationWebsite lw1 = new LocationWebsite();
			lw1.setSource("DummyData");
			lw1.setDescription("Description for dummy location website");
			lw1.setProvider("DummData");
			lw1.setTitle("Dummy Location Site");
			Place p1 = new Place();
			double currentLatitude =  54.0748861;
			double currentLongitude = 12.1161766;
			p1.setLatitude(currentLatitude);
			p1.setLongitude(currentLongitude);
			lw1.setPlace(p1);
			entityList.add(lw1);
			
			
		}
		return entityList;
	}
	
	public ArrayList<Service> getAllServices(){
		return services;
	}
	
	public ArrayList<Website> getAllWebsites(){
		ArrayList<Website> websites = new ArrayList<Website>();
		for (Service s : services){
			if (s instanceof Website && (s instanceof LocationWebsite) && (s instanceof LocationWebsite) )
				websites.add((Website)s);
		}
		return websites;
	}
	
	public static ArrayList<LocationWebsite> getLocationWebsites(){
		ArrayList<LocationWebsite> websites = new ArrayList<LocationWebsite>();
		for (Service s : services){
			if (s instanceof LocationWebsite)
				websites.add((LocationWebsite)s);
		}
		return websites;
	}
	
	public static ArrayList<EventWebsite> getEventWebsites(){
		ArrayList<EventWebsite> websites = new ArrayList<EventWebsite>();
		for (Service s : services){
			if (s instanceof EventWebsite)
				websites.add((EventWebsite)s);
		}
		return websites;
	}
	
	public static ArrayList<Website> getPersonalWebsites(){
		ArrayList<Website> websites = new ArrayList<Website>();
		for (Service s : services){
			if (s instanceof Website && !(s instanceof LocationWebsite) && !(s instanceof EventWebsite) )
						websites.add((Website)s);
		}
		return websites;
	}
	
	public static void refresh(){
		for (ServiceProxy s : serviceProxies){
			ArrayList<Entity> entity = getQueryResult(s.getQuery(), s.getParams());
			try {
				Service superService = s.getServieType().newInstance();
				for (Entity e : entity){
					if (e instanceof Service){
						if(!superService.getSubServices().contains((Service) e)){
							superService.addSubService((Service)e); 
						}
					}
				}
				superService.setTitle(s.getTitle());
				superService.setDescription(s.getDescription());
				services.add(superService);
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public boolean getConnectionStatus(){
		return isConnected;
	}
	
	public static ServiceContainer getContainer(){
		ServiceContainer container = new ServiceContainer();
		refresh();
		container.addAll(services);
		return container;
	}

	public static ServiceContainer getContainerEventWebsites(){
		ServiceContainer container = new ServiceContainer();
		refresh();
		container.addAll(getEventWebsites());
		return container;
	}
	public static ServiceContainer getContainerLocationWebsites(){
		ServiceContainer container = new ServiceContainer();
		refresh();
		container.addAll(getLocationWebsites());
		return container;
	}
	public static ServiceContainer getContainerPersonalWebsites(){
		ServiceContainer container = new ServiceContainer();
		refresh();
		container.addAll(getPersonalWebsites());
		return container;
	}
	
	private CamelContext getContext(){
		CamelContext camelContext = null;
		try{
			ctx = new InitialContext();
			camelContext = (CamelContext)ctx.lookup("MobileContext");
		}catch(NamingException ne){
			
		}
		return camelContext;
	}
	
	public static ArrayList<ServiceProxy> getServiceProxies() {
		return serviceProxies;
	}
	
	public static void addServiceProxy(ServiceProxy s){
		serviceProxies.add(s);
	}

	public static void setServiceProxies(ArrayList<ServiceProxy> serviceProxies) {
		ServiceHandler.serviceProxies = serviceProxies;
	}
	
}
