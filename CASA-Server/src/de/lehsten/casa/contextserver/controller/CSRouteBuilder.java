package de.lehsten.casa.contextserver.controller;

import javax.annotation.PreDestroy;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import de.lehsten.casa.contextserver.factentry.FactEntry;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.utilities.communication.CamelMessenger;
import de.lehsten.casa.utilities.communication.serializing.CSMessageConverter;
import de.lehsten.casa.utilities.communication.serializing.CSMessageDeserializer;
import de.lehsten.casa.utilities.communication.serializing.CSMessageSerializer;
import de.lehsten.casa.utilities.communication.serializing.EntityDeserializer;
import de.lehsten.casa.utilities.communication.serializing.EntitySerializer;

public class CSRouteBuilder extends RouteBuilder {

	InitialContext ctx;
	private CamelContext camelContext;
	Processor deserializer;


	public CSRouteBuilder(){
		try {
			ctx = new InitialContext();
			ctx.rebind("FactEntry", new FactEntry());
			ctx.rebind("CSControl", new CSController());
			startContext();
			new InitialContext().rebind("vm:ServerFactEntry", camelContext.hasEndpoint("vm:ServerFactEntry"));
			new InitialContext().rebind("vm:ServerControl", camelContext.hasEndpoint("vm:ServerControl"));
			} 
		catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startContext(){
		try { 
			log.debug("Lookup context...");
			this.camelContext = (CamelContext) ctx.lookup("ContextServerContext");
			log.debug("Context found: "+ this.camelContext);
		} catch (Exception e1 ) {
			// TODO Auto-generated catch block

			log.info("No Context found - defining new context..."); 
			DefaultCamelContext camelContext = new DefaultCamelContext();
			log.debug("Starting context...");
			try {
				//		camelContext.setRegistry(createRegistry());
				camelContext.addRoutes(this);
				camelContext.start();

				log.debug("Context started...");
				log.debug("CamelContextID: "+camelContext.getName());
				ctx.rebind("ContextServerContext", camelContext);
				log.debug("Context registered: "+ ctx.lookup("ContextServerContext"));
				this.camelContext = (CamelContext) ctx.lookup("ContextServerContext");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}	
		}
	}
	
	@PreDestroy
	public void stopContext(){
		
		try {
			ctx.unbind("ContextServerContext");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			camelContext.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ctx.unbind("FactEntry");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ctx.unbind("CSControl");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void configure() throws Exception {
		from("vm:ServerFactEntry").process(new EntityDeserializer()).to("FactEntry");
		from("vm:ServerControl").process(new CSMessageConverter()).to("CSControl").process(new CSMessageConverter());
	}

}
