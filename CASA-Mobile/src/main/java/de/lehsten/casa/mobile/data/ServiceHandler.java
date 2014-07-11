package de.lehsten.casa.mobile.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Producer;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.QueryRequest;
import de.lehsten.casa.contextserver.types.entities.place.Place;
import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.entities.services.websites.EventWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;
import de.lehsten.casa.contextserver.types.position.Position;
import de.lehsten.casa.contextserver.types.xml.CSMessage;
import de.lehsten.casa.mobile.gui.CASAMobileApplication;
import de.lehsten.casa.mobile.gui.CASAUI;
import de.lehsten.casa.utilities.communication.serializing.CSMessageConverter;


public class ServiceHandler implements Serializable{
	
	private static final long serialVersionUID = 1L;
	InitialContext ctx; 
	private static boolean isConnected = false; 
	private static ArrayList<Service> services = new ArrayList<Service>();
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private static CASAUI app;
	private static CamelContext camelContext;
	
	public ServiceHandler(CASAUI casaui){
		// Try to connect to Server
		this.app = CASAUI.getApp();
		try{
			ctx = new InitialContext();
			camelContext = (CamelContext)ctx.lookup("MobileContext");
		}catch(NamingException ne){
			System.err.println("No Context found ");
				
		}
		System.out.println("ServiceHandler created");
//		connectToServer();
//		refresh();
	}

	public void connectToServer(){
		// Lookup if there is a context server started here
		try{
			ctx = new InitialContext();
			camelContext = (CamelContext)ctx.lookup("MobileContext");
			this.nodes = app.getNodeHandler().getNodes();
			/*
			serverProducer = camelContext.createProducerTemplate();
			if (camelContext.hasEndpoint("direct:CASA_Server") != null)
			{
			serverProducer.setDefaultEndpoint(camelContext.getEndpoint("direct:CASA_Server"));
			System.out.println("Connected to ContextServer");
			this.isConnected = true;
			
			}
			*/
		}catch(NamingException ne){
			System.out.println("No ContextServer found - Using dummy data...");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static ArrayList<Entity> getQueryResult(String query, Node node){
		ArrayList<Entity> entityList = new ArrayList<Entity>();
			try 
			{
				CSMessage msg = new CSMessage();
				
				String queryName = "GetServices";
				Object[] arguments = new Object[0];
				QueryRequest qrequest = new QueryRequest();
				qrequest.getQuery().put(queryName, arguments);
				String id = "CASA_Mobile_"+System.currentTimeMillis();
				qrequest.setRequestId(id);
				//create objects
				if (node.isUsePosition()){
					Position currentPosition = new Position(app.getCurrentLatitude(),app.getCurrentLongitude());
					qrequest.getRestrictions().add(currentPosition);
				}
				CSMessage entityMsg = app.getRouteBuilder().sendRequest(qrequest, node);
				ArrayList<Object> objectList = entityMsg.payload;
				for (Object o : objectList){
					System.out.println(o);
					if (o instanceof Entity){
						entityList.add((Entity)o);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		if (entityList.isEmpty()){
			System.out.println("No services found - using dummy data...");
			LocationWebsite lw1 = new LocationWebsite();
			lw1.setSource("DummyData");
			lw1.setDescription("Description for dummy location website");
			lw1.setProvider("DummData");
			lw1.setTitle("Raumsteuerung Smartlab");
			Place p1 = new Place();
			double currentLatitude =  54.0748861;
			double currentLongitude = 12.1161766;
			p1.setLatitude(currentLatitude);
			p1.setLongitude(currentLongitude);
			LocationWebsite lw2 = new LocationWebsite();
			lw2.setPlace(p1);
			lw2.setSource("DummyData");
			lw2.setDescription("Description for dummy location website");
			lw2.setProvider("DummData");
			lw2.setTitle("Haltestellenabfahrtsplan Mensa");
			lw2.setPlace(p1);
			LocationWebsite lw3 = new LocationWebsite();
			lw3.setPlace(p1);
			lw3.setSource("DummyData");
			lw3.setDescription("Description for dummy location website");
			lw3.setProvider("DummData");
			lw3.setTitle("Haltestellenabfahrtsplan E.-Schlesinger-Straße");
			lw3.setPlace(p1);
			entityList.add(lw1);
			entityList.add(lw3);
			entityList.add(lw2);
			
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
		System.out.println("Refreshing");
		
		Collection<Node> nodes = app.getRouteBuilder().getNodes();
		System.out.println(nodes.size()+" endpoints found.");
		for (Node n : nodes){
		ArrayList<Entity> entity = getQueryResult("GetServices",n);
/*
		ArrayList<Entity> entity = getQueryResult("GetServices",null);
*/
		for (Entity e : entity){
			if (e instanceof Service){
				if(!services.contains((Service) e)){
				services.add((Service)e); 
				}
			}
		}
		System.out.println("Size of service list: "+services.size());
		}
		
	}
	
	public boolean getConnectionStatus(){
		return isConnected;
	}
	
	public static ServiceContainer getContainer(){
		ServiceContainer container = new ServiceContainer();
		container.addAll(services);
		return container;
	}

	public static ServiceContainer getContainerEventWebsites(){
		ServiceContainer container = new ServiceContainer();
		container.addAll(getEventWebsites());
		return container;
	}
	public static ServiceContainer getContainerLocationWebsites(){
		ServiceContainer container = new ServiceContainer();
		container.addAll(getLocationWebsites());
		return container;
	}
	public static ServiceContainer getContainerPersonalWebsites(){
		ServiceContainer container = new ServiceContainer();
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
}
