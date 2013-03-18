
package de.lehsten.casa.contextserver.inquiry;

import java.util.ArrayList;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.DependsOn;
import javax.ejb.MessageDriven;
import javax.ejb.Singleton;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.QueryResultsRow;

import de.lehsten.casa.contextserver.ContextServer;
import de.lehsten.casa.contextserver.types.entities.event.Course;
import de.lehsten.casa.contextserver.types.entities.event.Lecture;
import de.lehsten.casa.contextserver.types.entities.person.Person;
import de.lehsten.casa.contextserver.types.entities.place.Stop;
import de.lehsten.casa.contextserver.types.xml.CSMessage;

/**
 *
 * @author phil
 */
@DependsOn("ContextServer")  
@MessageDriven(mappedName = "jms.queue.PersonInquiry", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class PersonInquiryMDB implements MessageListener {
    
	StatefulKnowledgeSession ksession;
    ConnectionFactory cf;
    Connection connection;
    Session session;
	
    public PersonInquiryMDB() {
    	try {
			InitialContext ctx = new InitialContext();
			ContextServer cs = (ContextServer) ctx.lookup("java:global/CASA/CASA-Server/ContextServer");
			ksession = cs.getSession();
			cf = (ConnectionFactory) ctx.lookup("jms.ConnectionFactory");
			connection = cf.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    public void onMessage(Message message) {
    	
    	try {
			if (message instanceof ObjectMessage) {
				Person UserResp = null;
				ArrayList<Person> UserRespList = new ArrayList<Person>();
				CSMessage msg = (CSMessage) ((ObjectMessage) message).getObject();
				System.out.println("[EI]: Got request [" + msg.text + "]");
				if (msg.text.contains("getUser")) {
					System.out.println("[EI]: UserRequest received");
					UserResp = GetPersonByUsername(msg.parameter
							.get("Username"), ksession);
				
					CSMessage responseMessage = new CSMessage();
					if (UserResp != null) {
						responseMessage.text = UserResp.getUsername()
								+ " found!";
					} else
						responseMessage.text = msg.parameter.get("Username")
								+ " not found!";
					responseMessage.payload.add(UserResp);
					Message respmsg = session.createObjectMessage(responseMessage);
					session.createProducer(message.getJMSReplyTo()).send(respmsg);
					System.out.println("[EI]: Sent response ["
							+ responseMessage.text + "]");
				}
				if (msg.text.contains("getAllPersons")) {
					System.out.println("[EI]: UserRequest received");
					UserRespList = GetAllPersons(ksession);
				
					CSMessage responseMessage = new CSMessage();
					if (UserRespList.size() != 0) {
						responseMessage.text = UserRespList.size() +" persons found!";
						for (Person p : UserRespList){
						responseMessage.payload.add(p);
						}
					} else{
						responseMessage.text = "No persons found!";
					}
					Message respmsg = session.createObjectMessage(responseMessage);
					session.createProducer(message.getJMSReplyTo()).send(respmsg);
					System.out.println("[EI]: Sent response ["
							+ responseMessage.text + "]");
				}
				else {
					System.out.println("[EI]: Request unrecognized!");
				}

			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    private static ArrayList<Person> GetAllPersons(StatefulKnowledgeSession ksession){
		ArrayList<Person> personList = new ArrayList<Person>();
		QueryResults results = ksession.getQueryResults( "GetAllPersons");
		if(results.size() != 0){
		for ( QueryResultsRow row : results ) {
		    personList.add( (Person) row.get( "p" ));
		}
		return personList;
		}
		else return null;
		
	}
	private static Person GetPersonByUsername(String Username, StatefulKnowledgeSession ksession){
		Object[] myargs = new Object[1];
		myargs[0] = Username;
		Person person = null;
		QueryResults results = ksession.getQueryResults( "GetPersonByUsername", myargs );
		if(results.size() == 1){
		for ( QueryResultsRow row : results ) {
		    person = ( Person ) row.get( "p" );
		    System.out.print( person.getUsername() + "\n" );
		}
		return person;
		}
		else return null;
		
	}
	
}
