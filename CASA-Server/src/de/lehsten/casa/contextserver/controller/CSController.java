package de.lehsten.casa.contextserver.controller;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lehsten.casa.contextserver.CASAContextServer;
import de.lehsten.casa.contextserver.interfaces.ContextServer;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.Rule;
import de.lehsten.casa.contextserver.types.xml.CSMessage;


public class CSController implements Processor, ContextServer{
	
	ContextServer server;
	HashMap<String,Method> methods = new HashMap<String,Method>();
	InvocationHandler invHandler; 
	private final static Logger log = LoggerFactory.getLogger( CSController.class ); 
	   
	

	public CSController(){ 
    	InitialContext ctx;
		try {
			ctx = new InitialContext();
		server = (ContextServer) ctx.lookup("java:global/CASA-Server/CASAContextServer");
		Method[] meth =	this.getClass().getDeclaredMethods();
		for (Method m : meth){
			methods.put(m.getName(), m);
		}
		
		} catch (NamingException e2) { 
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
    }

	
	@Override
	public void process(Exchange exchange) throws Exception {
		log.debug("Message received.");
		if (server == null) {log.error("Message received but no ContextServer available");}
    	
 //   	exchange.getOut().setBody("Antwort auf " +exchange.getIn().getBody());
		
    	else if (exchange.getIn().getBody() instanceof CSMessage) {
			try {
				CSMessage msg =  (CSMessage) exchange.getIn().getBody();
				String method = msg.text;
				log.debug("Requested method: " + method);
				log.debug("Method available: "+methods.containsKey(method));
				Object[] args = msg.payload.toArray();
				for (Object o : args){
					log.debug("Args: "+o);
					if (o != null){
					if(o.getClass().isArray()){
						Object[] p = (Object[]) o;
						log.debug("Array.length: "+p.length);
						for (Object q : p){
							log.debug("Param:"+q);
						}
					}
					}
				}					
				try {
				
					if (methods.containsKey(method) && args.length == 0){

						Method action = server.getClass().getDeclaredMethod(method);
						if(action.getReturnType().equals(Collection.class)){
							// Method returns a collection
							action.setAccessible(true);
							ArrayList<Object> list = (ArrayList<Object>) action.invoke(server);
							CSMessage reply = new CSMessage();
							log.debug(list.size()+ " Objects found.");
							reply.text = list.size()+ "Objects found.";
							reply.payload = list;
							exchange.getOut().setBody(reply);
						} else if (action.getReturnType().equals(Void.TYPE)){
							action.setAccessible(true);
							action.invoke(server);
						};
						
						
					}
					else if(methods.containsKey(method) && args.length != 0){

						log.debug("Requested method: " + method);
						log.debug("Method available: "+methods.containsKey(method));
							Method m = methods.get(method);
							Class<?>[] x = m.getParameterTypes();  
							Class<?>[] y = new Class<?>[x.length];
							for (int i = 0; i<x.length; i++){
								log.debug(x[i].getName());
								y[i] = x[i]; 
							}
							Method action = server.getClass().getDeclaredMethod(method, y);
							if(action.getReturnType().equals(Collection.class)){
								action.setAccessible(true);
								ArrayList<Object> list = (ArrayList<Object>) action.invoke(server, args);
								log.debug(list.size()+ " Objects found.");
								CSMessage reply = new CSMessage();
								reply.payload = list;
								exchange.getOut().setBody(reply);
							} else if (action.getReturnType().equals(Void.TYPE)){
								action.setAccessible(true);
								action.invoke(server, args);
							};
					}
					}
					catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}catch (Exception e){
					e.printStackTrace();
				}
    	}
				
		
        
		
	}
	@Override
	public void addEntity(Entity arg0) {
		server.addEntity(arg0);
		
	}

	@Override
	public void applyRule(String arg0) {
		server.applyRule(arg0);
		
	}

	@Override
	public void applyRules() {
		server.applyRules();
	}

	@Override
	public long getFactCount() {
		return server.getFactCount();
	}

	@Override
	public Collection<String> getQueryNames() {
		return server.getQueryNames();
	}

	@Override
	public Collection<Entity> getQueryResult(String arg0, Object[] arg1) {
		return server.getQueryResult(arg0, arg1);
	}

	@Override
	public Collection<String> getRuleNames() {
		return server.getRuleNames();
	}

	@Override
	public void removeEntity(Entity arg0) {
		server.removeEntity(arg0);
	}

	@Override
	public void startServer() {
		server.startServer();
	}

	@Override
	public void storeSession() {
		server.storeSession(); 
	}
	
	@Override
	public void restoreSession() {
		server.restoreSession(); 
	}	
	
	@Override
	public void stopServer() {
		server.stopServer(); 
	}

	@Override
	public void updateEntity(Entity arg0, Entity arg1) {
		server.updateEntity(arg0, arg1);
	}
	
	@Override
	public void updateEntityByProperty(String propertyKey, String propertyValue, Entity newEntity) {
		server.updateEntityByProperty(propertyKey, propertyValue, newEntity);
	}

	@Override
	public void addRule(Rule arg0) {
		server.addRule(arg0);
		
	}
	
	@Override
	public Collection<Rule> getRules(){
		return server.getRules(); 
	}

	@Override
	public void removeRule(Rule arg0) {
		server.removeRule(arg0);
		
	}


	@Override
	public void applyRule(Rule arg0) {
		server.applyRule(arg0);
		
	}


	@Override
	public void updateRule(Rule arg0, Rule arg1) {
		server.updateRule(arg0, arg1);
	}
}
