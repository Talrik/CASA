package de.lehsten.casa.contextserver.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.lehsten.casa.contextserver.interfaces.ContextServer;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.Rule;
import de.lehsten.casa.contextserver.types.xml.CSMessage;

/**
 * Controller for {@link ContextServer} implemented as Message-Driven Bean 
 * Implements also the {@link ContextServer}.  
 *
 */
/*

@MessageDriven(
		messageListenerInterface=MessageListener.class,
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue") }, 
				mappedName = "jms.queue.controller")
public class CSControllerMDB implements MessageListener, ContextServer{
	
	ContextServer server;
	HashMap<String,Method> methods = new HashMap<String,Method>();
	InvocationHandler invHandler;
	String mappedQueue = "jms.queue.controller";


    public CSControllerMDB() { 
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
	

    public void onMessage(Message message) {
    	System.out.println("[CSCMDB]: Message received.");
    	if (server == null) {System.err.println("[CSCMDB]: No ContextServer available");}
		else if (message instanceof TextMessage) {
			try {
				String msg =  ((TextMessage)message).getText();
				System.out.println("[CSCMDB]:"+msg);
				System.out.println("[CSCMDB]:"+methods.containsKey(msg));
				
				if (methods.containsKey(msg)){
					try {
						Method action = server.getClass().getDeclaredMethod(msg);
						if(action.getReturnType().equals(Collection.class)){
							// Method returns a collection
							action.setAccessible(true); 
							ArrayList<Object> list = (ArrayList<Object>) action.invoke(server);
//							JMSMessenger messenger = new JMSMessenger("CSCMDB", ((Queue)message.getJMSReplyTo()).getQueueName(), "jms.queue.controller");
							CSMessage reply = new CSMessage();
							System.out.println("[CSCMDB]:"+ list.size()+ " Objects found.");
							reply.text = list.size()+ "Objects found.";
							reply.payload = list;
//							messenger.send(reply);
						} else if (action.getReturnType().equals(Void.TYPE)){
							action.setAccessible(true);
							action.invoke(server);
						};
						
						
					} catch (SecurityException e) {
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
				}
				
				
				
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
		else if(message instanceof ObjectMessage){
			try {
				CSMessage cmsg = (CSMessage) ((ObjectMessage) message).getObject();
				String msg = cmsg.text;
				System.out.println("[CSCMDB]: ObjectMessage asks for method:"+ msg);
				Object[] args = cmsg.payload.toArray();
				for (Object o : args){
					System.out.println("[CSCMDB]: Args:"+o);
				}
				if (methods.containsKey(msg)){
					try {
						Method m = methods.get(msg);
						Class<?>[] x = m.getParameterTypes();
						Class<?>[] y = new Class<?>[x.length];
						for (int i = 0; i<x.length; i++){
							y[i] = x[i];
						}
						Method action = server.getClass().getDeclaredMethod(msg, y);
						if(action.getReturnType().equals(Collection.class)){
							action.setAccessible(true);
							ArrayList<Object> list = (ArrayList<Object>) action.invoke(server, args);
//							JMSMessenger messenger = new JMSMessenger("CSCMDB", ((Queue)message.getJMSReplyTo()).getQueueName(), "jms.queue.controller");
							CSMessage reply = new CSMessage();
							reply.payload = list;
//							messenger.send(reply);	
						} else if (action.getReturnType().equals(Void.TYPE)){
							action.setAccessible(true);
							action.invoke(server, args);
						};

					} catch (SecurityException e) {
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
				}
				else {
					System.out.println("Unknown request:"+cmsg.text);
				}
			} catch (JMSException e) {
				// TODO Auto-generated catch block
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
	public void stopServer() {
		server.stopServer(); 
	}

	@Override
	public void updateEntity(Entity arg0, Entity arg1) {
		server.updateEntity(arg0, arg1);
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

}
*/
