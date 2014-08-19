package de.lehsten.casa.mobile.communication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.annotation.PreDestroy;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.Request;
import de.lehsten.casa.contextserver.types.xml.CSMessage;
import de.lehsten.casa.mobile.data.Node;
import de.lehsten.casa.mobile.gui.CASAUI;
import de.lehsten.casa.utilities.communication.serializing.CSMessageConverter;

public class MobileRouteBuilder extends RouteBuilder{
	
	CamelContext camelContext;
	InitialContext ctx;
	CASAUI app;
	HashMap<Node,ProducerTemplate> endpoints = new HashMap<Node,ProducerTemplate>();
	HashMap<Node,String> routes = new HashMap<Node,String>();

	public MobileRouteBuilder(CASAUI casaui){
		this.app = casaui;
		startContext();
//		connectToServer();
	}
	
	private void startContext(){
		try { 
			System.out.println("-----------------");
			System.out.println("Lookup context...");
			this.camelContext = (CamelContext) ctx.lookup("MobileContext");
			System.out.println("Context found: "+ this.camelContext);
		} catch (Exception e1 ) {
			// TODO Auto-generated catch block

			System.out.println("No Context found - defining new context..."); 
			DefaultCamelContext camelContext = new DefaultCamelContext();
			System.out.println("Starting context...");
			try {
				ctx = new InitialContext();
				camelContext.start();
				System.out.println("Context started...");
				System.out.println("CamelContextID: "+camelContext.getName());
				ctx.rebind("MobileContext", camelContext);
				System.out.println("Context registered: "+ ctx.lookup("MobileContext"));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}	
		}
	}

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void connectToServer(Node node){
		// Lookup if there is already a context server started here
		try{
			final Endpoint serverControl = (Endpoint)ctx.lookup(node.getEndpoint());
			System.out.println("GUIRB: "+ serverControl);
			final String LOCAL_ENDPOINT_STRING = "direct:CASA_Server-" + node.hashCode();
			final String LOCAL_ROUTE_ID = "Route_to_CASA_Server-"+node.hashCode();
			((CamelContext) ctx.lookup("MobileContext")).addRoutes(new RouteBuilder(){

				@Override
				public void configure() throws Exception {
					from(LOCAL_ENDPOINT_STRING).process(new CSMessageConverter()).to(serverControl).process(new CSMessageConverter()).routeId(LOCAL_ROUTE_ID);
				}});
			ProducerTemplate producer = ((CamelContext) ctx.lookup("MobileContext")).createProducerTemplate();
			producer.setDefaultEndpoint(((CamelContext) ctx.lookup("MobileContext")).getEndpoint(LOCAL_ENDPOINT_STRING));
			endpoints.put(node, producer);
			for(Object o :this.sendCommand("getQueryNames", node).payload){
				node.getAvailableQueries().add((String) o);
			}
			routes.put(node, LOCAL_ROUTE_ID);
			System.out.println("Connected to ContextServer "+ node.getDescription());
		}catch(NamingException ne){
			System.out.println("No ContextServer found");
			ne.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void disconnectFromServer(Node node)
	{
		try {
			((CamelContext) ctx.lookup("MobileContext")).suspendRoute(routes.get(node));
			((CamelContext) ctx.lookup("MobileContext")).stopRoute(routes.get(node));
			((CamelContext) ctx.lookup("MobileContext")).removeRoute(routes.get(node));
			((CamelContext) ctx.lookup("MobileContext")).removeEndpoints("direct:CASA_Server-" + node.hashCode());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Collection<Node> getNodes(){
		if (endpoints.size()==0){
			return new ArrayList<Node>();
		}
		else{
			return endpoints.keySet();
		}
	}
	
	public boolean isConnected(Node node){
		return endpoints.containsKey(node);
	}
	
	public CSMessage sendRequest(Request req, Node node){
		//insert request
		CSMessage insertReq = new CSMessage();
		insertReq.text = "addEntity";
		insertReq.payload.add(req);
		endpoints.get(node).sendBody(insertReq);
		//fire all rules
		CSMessage fireAllRules = new CSMessage();
		fireAllRules.text = "applyRules";
		endpoints.get(node).sendBody(fireAllRules);
		//retract request
		CSMessage retractReq = new CSMessage();
		retractReq.text = "getQueryResult";
		retractReq.payload.add("GetRequestById");
		Object[] arguments = new Object[1];
		arguments[0] = req.getRequestId();
		retractReq.payload.add(arguments);
		CSMessage reply = (CSMessage)endpoints.get(node).requestBody(retractReq);
		for (Object o : reply.payload){
			if (o instanceof Entity){
				System.out.println("Entity"+o.getClass());
			}
		} 
		//delete request from node
		CSMessage removeReq = new CSMessage();
		removeReq.text = "removeEntity";
		removeReq.payload.add( reply.payload.get(1));
		endpoints.get(node).sendBody(removeReq);
		return reply;				
	}
	
	public CSMessage sendCommand(String command, Node n){
		CSMessage comMsg = new CSMessage();
		comMsg.text = command;
		return (CSMessage)endpoints.get(n).requestBody(comMsg);				
	}
	
	@PreDestroy
	public void shutdownContext(){
		// stop CamelContext
		try {
			this.camelContext = (CamelContext) ctx.lookup("MobileContext");
			this.camelContext.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		//unbind CamelContext
		try {
			ctx.unbind("MobileContext");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}

	}

}
