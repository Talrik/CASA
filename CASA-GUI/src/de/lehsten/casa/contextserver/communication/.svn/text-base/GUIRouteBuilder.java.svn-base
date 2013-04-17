package de.lehsten.casa.contextserver.communication;

import java.util.List;

import javax.annotation.PreDestroy;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import com.vaadin.ui.Window;

import de.lehsten.casa.contextserver.gui.GuiApplication;


public class GUIRouteBuilder extends RouteBuilder{

	CamelContext camelContext;
	InitialContext ctx;
	GuiApplication app;

	public GUIRouteBuilder(GuiApplication app){
		this.app = app;
		startContext();
	}

	private void startContext(){
		try { 
			System.out.println("-----------------");
			System.out.println("Lookup context...");

			this.camelContext = (CamelContext) ctx.lookup("GUIContext");

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
				ctx.rebind("GUIContext", camelContext);
				System.out.println("Context registered: "+ ctx.lookup("GUIContext"));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}	
		}
	}

	@Override
	public void configure() throws Exception { 
	}
	
	public void connectToServer(){
		// Lookup if there is already a context server started here
		try{
			final Endpoint serverControl = (Endpoint)ctx.lookup("vm:ServerControl");
			this.camelContext = (CamelContext) ctx.lookup("GUIContext");
			List<Route> list = this.camelContext.getRoutes();
			for (Route r : list){
				if(r.getEndpoint().getEndpointUri().contains("direct://CASA_Server")){
					this.camelContext.stopRoute(r.getId());
					}
			}
			((CamelContext) ctx.lookup("GUIContext")).addRoutes(new RouteBuilder(){

				@Override
				public void configure() throws Exception {
					from("direct:CASA_Server").process(new CSMessageConverter()).to(serverControl).process(new CSMessageConverter());
				}});
			app.setServerStatus(true);
			System.out.println("Connected to ContextServer");
			app.getMainWindow().showNotification("Connected to server","<br>The connection to the server has been established", Window.Notification.TYPE_HUMANIZED_MESSAGE);
		}catch(NamingException ne){
			System.out.println("No ContextServer found");
			app.getMainWindow().showNotification("No server found","<br>"+ne.getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void connectToImporter(){
		// Lookup if there is already an importer started at this server
		try{
			final Endpoint importerControl = (Endpoint)ctx.lookup("vm:ImporterControl");
			this.camelContext = (CamelContext) ctx.lookup("GUIContext");
			List<Route> list = this.camelContext.getRoutes();
			for (Route r : list){
				if(r.getEndpoint().getEndpointUri().contains("direct://Importer")){
					this.camelContext.stopRoute(r.getId());
				}
			}
		((CamelContext) ctx.lookup("GUIContext")).addRoutes(new RouteBuilder(){

				@Override
				public void configure() throws Exception {
					from("direct:Importer").process(new CSMessageConverter()).to(importerControl).process(new CSMessageConverter());
				}});
				app.setImporterStatus(true);
				System.out.println("Connected to Importer");
				app.getMainWindow().showNotification("Connected to importer","<br>The connection to the importer has been established", Window.Notification.TYPE_HUMANIZED_MESSAGE);
		}catch (NamingException ne){
			System.out.println("No Importer found");
			app.getMainWindow().showNotification("No importer manager found","<br>"+ne.getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@PreDestroy
	public void shutdownContext(){
		// stop CamelContext
		try {
			this.camelContext = (CamelContext) ctx.lookup("GUIContext");
			this.camelContext.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}		
		//unbind CamelContext
		try {
			ctx.unbind("GUIContext");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}

	}

}
