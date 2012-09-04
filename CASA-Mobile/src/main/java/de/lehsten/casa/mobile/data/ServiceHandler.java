package de.lehsten.casa.mobile.data;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;

import de.lehsten.casa.utilities.communication.serializing.CSMessageConverter;


public class ServiceHandler {
	InitialContext ctx; 
	boolean isConnected = false;
	
	public ServiceHandler(){
		// Try to connect to Server
		
	}

	public void connectToServer(){
		// Lookup if there is already a context server started here
		try{
			ctx = new InitialContext();
			final Endpoint serverControl = (Endpoint)ctx.lookup("vm:ServerControl");
			System.out.println("GUIRB: "+ serverControl);

			((CamelContext) ctx.lookup("GUIContext")).addRoutes(new RouteBuilder(){

				@Override
				public void configure() throws Exception {
					from("direct:CASA_Server").process(new CSMessageConverter()).to(serverControl).process(new CSMessageConverter());
				}});
			isConnected = (true);
			System.out.println("Connected to ContextServer");
		}catch(NamingException ne){
			System.out.println("No ContextServer found");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
