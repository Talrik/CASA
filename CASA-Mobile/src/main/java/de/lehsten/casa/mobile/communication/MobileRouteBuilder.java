package de.lehsten.casa.mobile.communication;

import javax.annotation.PreDestroy;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import de.lehsten.casa.mobile.gui.CASAMobileApplication;
import de.lehsten.casa.utilities.communication.serializing.CSMessageConverter;

public class MobileRouteBuilder extends RouteBuilder{
	
	CamelContext camelContext;
	InitialContext ctx;
	CASAMobileApplication app;

	public MobileRouteBuilder(CASAMobileApplication app){
		this.app = app;
		startContext();
		connectToServer();
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
	
	public void connectToServer(){
		// Lookup if there is already a context server started here
		try{
			final Endpoint serverControl = (Endpoint)ctx.lookup("vm:ServerControl");
			System.out.println("GUIRB: "+ serverControl);

			((CamelContext) ctx.lookup("MobileContext")).addRoutes(new RouteBuilder(){

				@Override
				public void configure() throws Exception {
					from("direct:CASA_Server").process(new CSMessageConverter()).to(serverControl).process(new CSMessageConverter());
				}});
			System.out.println("Connected to ContextServer");
		}catch(NamingException ne){
			System.out.println("No ContextServer found");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
