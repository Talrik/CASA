package de.lehsten.casa.mobile.data;

import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Producer;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.entities.services.websites.EventWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;
import de.lehsten.casa.contextserver.types.xml.CSMessage;
import de.lehsten.casa.utilities.communication.serializing.CSMessageConverter;


public class ServiceHandler {
	InitialContext ctx; 
	private boolean isConnected = false;
	private ProducerTemplate serverProducer;
	private CamelContext camelContext;
	private ArrayList<Service> services = new ArrayList<Service>();
	
	public ServiceHandler(){
		// Try to connect to Server
		connectToServer();
		refresh();
	}

	public void connectToServer(){
		// Lookup if there is a context server started here
		try{
			ctx = new InitialContext();
			this.camelContext = (CamelContext)ctx.lookup("MobileContext");
			serverProducer = this.camelContext.createProducerTemplate();
			if (this.camelContext.hasEndpoint("direct:CASA_Server") != null)
			{
			serverProducer.setDefaultEndpoint(camelContext.getEndpoint("direct:CASA_Server"));
			System.out.println("Connected to ContextServer");
			this.isConnected = true;
			}
		}catch(NamingException ne){
			System.out.println("No ContextServer found");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ArrayList<Entity> getQueryResult(){
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		if (isConnected){
			try 
			{
				CSMessage msg = new CSMessage();
				msg.text = "getQueryResult";
				msg.payload.add("GetServices");
				msg.payload.add(null);
				
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
		}
		return entityList;
	}
	
	public ArrayList<Service> getAllServices(){
		return services;
	}
	
	public ArrayList<Website> getAllWebsites(){
		ArrayList<Website> websites = new ArrayList<Website>();
		for (Service s : services){
			if (s instanceof Website)
				websites.add((Website)s);
		}
		return websites;
	}
	
	public ArrayList<LocationWebsite> getLocationWebsites(){
		ArrayList<LocationWebsite> websites = new ArrayList<LocationWebsite>();
		for (Service s : services){
			if (s instanceof LocationWebsite)
				websites.add((LocationWebsite)s);
		}
		return websites;
	}
	
	public ArrayList<EventWebsite> getEventWebsites(){
		ArrayList<EventWebsite> websites = new ArrayList<EventWebsite>();
		for (Service s : services){
			if (s instanceof EventWebsite)
				websites.add((EventWebsite)s);
		}
		return websites;
	}
	
	public void refresh(){
		ArrayList<Entity> entity = getQueryResult();
		for (Entity e : entity){
			if (e instanceof Service){
				services.add((Service)e); 
			}
		}
		
	}
	
	public boolean getConnectionStatus(){
		return isConnected;
	}
}
