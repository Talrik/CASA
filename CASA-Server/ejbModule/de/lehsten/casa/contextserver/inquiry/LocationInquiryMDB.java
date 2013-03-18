/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
@MessageDriven(mappedName = "jms.queue.LocationInquiry", activationConfig = {
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class LocationInquiryMDB implements MessageListener {

	StatefulKnowledgeSession ksession;
	ConnectionFactory cf;
	Connection connection;
	Session session;

	public LocationInquiryMDB() {
		try {
			InitialContext ctx = new InitialContext();
			ContextServer cs = (ContextServer) ctx
					.lookup("de.lehsten.casa.contextserver.ContextServer");
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
				ArrayList<Stop> StopResp = new ArrayList<Stop>();
				CSMessage msg = (CSMessage) ((ObjectMessage) message)
						.getObject();
				System.out.println("[EI]: Got request [" + msg.text + "]");
				if (msg.text.contains("getClosestStop")) {
					Long date = null;
					System.out.println("[EI]: ClosestStopRequest received");

					StopResp = GetClosestStop(
							Double.valueOf(msg.parameter.get("Longitude")),
							Double.valueOf(msg.parameter.get("Latitude")),
							ksession);

					CSMessage responseMessage = new CSMessage();
					if (StopResp != null) {
						responseMessage.text = StopResp.size()
								+ " Close stops found!";
						int i = 0;
						while (StopResp.size() > i) {
							responseMessage.payload.add(StopResp.get(i));
							i++;
						}
					} else {
						responseMessage.text = " No close stops found!";

						responseMessage.payload.add(null);
					}
					Message respmsg = session
							.createObjectMessage(responseMessage);
					session.createProducer(message.getJMSReplyTo()).send(
							respmsg);
					System.out.println("[EI]: Sent response ["
							+ responseMessage.text + "]");
				} else {
					System.out.println("[EI]: Request unrecognized!");
				}

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static ArrayList<Stop> GetClosestStop(Double Lon, Double Lat,
			StatefulKnowledgeSession ksession) {
		Object[] myargs = new Object[2];
		myargs[0] = Lat;
		myargs[1] = Lon;
		Stop stop = new Stop();
		ArrayList<Stop> list = new ArrayList<Stop>();
		QueryResults results = ksession.getQueryResults("GetClosestStop",
				myargs);
		System.out.println("There are " + results.size() + " stop close");

		for (QueryResultsRow row : results) {
			stop = (Stop) row.get("s");
			System.out.println("Close is " + stop.getTitle());
			list.add(stop);
		}
		return list;

	}
}
