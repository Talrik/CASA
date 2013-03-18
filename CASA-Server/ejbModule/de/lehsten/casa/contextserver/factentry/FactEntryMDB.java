/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lehsten.casa.contextserver.factentry;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.lehsten.casa.contextserver.ContextServer;
import de.lehsten.casa.contextserver.IContextServer;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.xml.CSMessage;

/**
 *
 * @author phil
 */
@MessageDriven(mappedName = "jms.queue.FactEntry", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class FactEntryMDB implements MessageListener {
    
	IContextServer server;
	
    public FactEntryMDB() {
    	InitialContext ctx;
		try {
			ctx = new InitialContext();
		server = (IContextServer) ctx.lookup("java:global/CASA-Server/ContextServer");
		} catch (NamingException e2) { 
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	
    }
    
    @Override
    public void onMessage(Message message) {
    	
    	System.out.println("Msg received by FEMDB.");
    	if (server == null) {System.err.println("[FEMDB]: No ContextServer available");}
		else if (message instanceof ObjectMessage) {
			CSMessage msg;
			try {
				msg = (CSMessage) ((ObjectMessage) message).getObject();
			for (Entity e : msg.payload){
				server.addEntity(e);
				System.out.println("FactCount:"+server.getFactCount());
			}
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}

    }
}
