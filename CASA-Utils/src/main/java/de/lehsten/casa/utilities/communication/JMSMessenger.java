package de.lehsten.casa.utilities.communication;

import java.util.ArrayList;
import java.util.Collection;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.xml.CSMessage;

public class JMSMessenger {

	private Context jndiContext;
	private Connection connection;
	  private Session session;
	  private MessageProducer producer;
	  private MessageConsumer consumer;
	  private Queue response;
	  private String owner;
	
	public JMSMessenger(String owner, String requestQueue){
		try{
			this.owner = owner;
			InitialContext ctx = new InitialContext();
		 ConnectionFactory connectionFactory = (ConnectionFactory) ctx.lookup("jms.ConnectionFactory");
				 
		try {
	          jndiContext = new InitialContext();
	       } catch (NamingException e) {
	           System.out.println("Could not create JNDI API " +
	               "context: " + e.toString());
	           System.exit(1);
	       }
		    connection = connectionFactory.createConnection();
		    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		    // get our request and response queues
		    Queue request = (Queue)jndiContext.lookup(requestQueue);
		    response = session.createQueue(owner+"Response");
		    
		    // attach a consumer and producer to them
		    producer = session.createProducer(request);
		    consumer = session.createConsumer(response);
		    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		    // and start your engines...

		}catch(Exception e){
			e.printStackTrace(); 
		}
	}
	
	public void send(CSMessage msg) {
		  try {
			System.out.println(owner+": Sending Message");
			  connection.start();
			  ObjectMessage reqmsg = session.createObjectMessage(msg);
			  reqmsg.setJMSReplyTo(response); 
			producer.send(reqmsg);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }

	public void send(Entity entity) {
		  try {
			System.out.println(owner+": Sending Message");
			  connection.start();
				CSMessage msg = new CSMessage();
				msg.text = "Entity entry from "+owner;
				msg.payload.add(entity);
			  ObjectMessage reqmsg = session.createObjectMessage(msg);
			  reqmsg.setJMSReplyTo(response); 
			producer.send(reqmsg);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }	
	
	public ArrayList<Entity> receive(){
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		try {
			Message response = consumer.receive();
			if (response instanceof ObjectMessage) {
		    	  
		          CSMessage resp = (CSMessage) ((ObjectMessage) response).getObject();
		    //      log.debug("Server: Got request [" + msg.text + "]");
		          System.out.println(owner+": Got response [" + resp.text + "]");
		          int i =0;
		          ArrayList<Object> resList = resp.payload; 
		          for(Object o : resList) {
		        	  if ( o instanceof Entity) {
		        		  entityList.add((Entity) o);
		        	  }
		          }
		      }
		}
			catch(Exception e){e.printStackTrace();}
		return entityList;
	}
	
}
