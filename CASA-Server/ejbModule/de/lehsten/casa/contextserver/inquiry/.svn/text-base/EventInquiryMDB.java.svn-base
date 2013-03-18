/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lehsten.casa.contextserver.inquiry;

import java.util.ArrayList;
import java.util.Date;

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
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.QueryResultsRow;

import de.lehsten.casa.contextserver.ContextServer;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.event.Course;
import de.lehsten.casa.contextserver.types.entities.event.Event;
import de.lehsten.casa.contextserver.types.entities.event.Lecture;
import de.lehsten.casa.contextserver.types.entities.person.Person;
import de.lehsten.casa.contextserver.types.entities.place.rooms.Room;
import de.lehsten.casa.contextserver.types.xml.CSMessage;

/**
 *
 * @author phil
 */

@DependsOn("ContextServer")
@MessageDriven(mappedName = "jms.queue.EventInquiry", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class EventInquiryMDB implements MessageListener {
    
    StatefulKnowledgeSession ksession;
    ConnectionFactory cf;
    Connection connection;
    Session session;

	
    public EventInquiryMDB() { 
    	try {
			InitialContext ctx = new InitialContext();
			ContextServer cs = (ContextServer) ctx.lookup("java:global/CASA-Server/ContextServer");
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
			System.out.println("[EI]: Message received");
			/*
			 * if (message instanceof TextMessage) { String messageText =
			 * ((TextMessage) message).getText();
			 * System.out.println("[EI]: Got request [" + messageText + "]");
			 * Message responseMessage = session.createTextMessage("Processed "
			 * + messageText); if (message.getJMSCorrelationID() != null) { //
			 * pass it through
			 * responseMessage.setJMSCorrelationID(message.getJMSCorrelationID
			 * ()); } producer.send(responseMessage); }
			 */
			if (ksession == null) {System.err.println("[EI]: No knowledge session available");}
			else if (message instanceof ObjectMessage) {
				ArrayList<Entity> EntityResp = new ArrayList<Entity>();
				ArrayList<Event> EventResp = new ArrayList<Event>();
				ArrayList<Course> CourseResp = new ArrayList<Course>();
				ArrayList<Lecture> LectureResp = new ArrayList<Lecture>();
				CSMessage msg = (CSMessage) ((ObjectMessage) message)
						.getObject();
				System.out.println("[EI]: Got request [" + msg.text + "]");
			

				if (msg.text.contains("getCourse")) {
					System.out.println("[EI]: CourseRequest received");
					if (msg.parameter.containsKey("Title")){
					CourseResp.addAll(GetCoursesByTitle(msg.parameter.get("Title"),
							ksession));
					}
					if (msg.parameter.containsKey("Username")){
						CourseResp.addAll(GetCoursesByUsername(msg.parameter.get("Username"),
								ksession));
						}
					if (msg.parameter.containsKey("Date")){
						CourseResp.addAll(GetCoursesByDate(Long.getLong(msg.parameter.get("Date")),
								ksession));
						}
					if (msg.parameter.containsKey("Room")){
						CourseResp.addAll(GetCoursesByRoom(msg.parameter.get("Room"),
								ksession));
						}
					
					CSMessage responseMessage = new CSMessage();
					if (CourseResp != null) {
						responseMessage.text = CourseResp.size()
								+ " Courses found!";
						int i = 0;
						while (CourseResp.size() > i) {
							responseMessage.payload.add(CourseResp.get(i));
							i++;
						}
					} else{
						responseMessage.text = msg.parameter.get("Title")
								+ " not found!";
					
					responseMessage.payload.add(null);
					}
					Message respmsg = session
							.createObjectMessage(responseMessage);
					if (message.getJMSReplyTo() != null){
					session.createProducer(message.getJMSReplyTo()).send(respmsg);
					System.out.println("[EI]: Sent response ["
							+ responseMessage.text + "]");
					}
					else {
						System.err.println("[EI]: Sending of response ["
								+ responseMessage.text + "] impossible - no ReplyTo defined");
							
					}
				}
				else if (msg.text.contains("getLecture")) {
					System.out.println("[EI]: LectureRequest received");
					if (msg.parameter.containsKey("Title")){
					LectureResp = GetLecturesByTitle(msg.parameter.get("Title"),
							ksession);
					}
					CSMessage responseMessage = new CSMessage();
					if (LectureResp != null) {
						responseMessage.text = LectureResp.size()
								+ " Lectures found!";
						int i = 0;
						while (LectureResp.size() > i) {
							responseMessage.payload.add(LectureResp.get(i));
							i++;
						}
					} else{
						responseMessage.text = msg.parameter.get("Title")
								+ " not found!";
					
					responseMessage.payload.add(null);
					}
					Message respmsg = session
							.createObjectMessage(responseMessage);
					session.createProducer(message.getJMSReplyTo()).send(respmsg);
					System.out.println("[EI]: Sent response ["
							+ responseMessage.text + "]");
				}
				else if (msg.text.contains("getNextCourse")) {
					Long date = null;
					System.out.println("[EI]: NextCourseRequest received");
					if (msg.parameter.containsKey("Username")){
						if(!msg.parameter.containsKey("Date")){
							date = new Date().getTime();
						}
						else date = Long.getLong(msg.parameter.get("Date"));
					CourseResp = GetNextCourseByUsername(msg.parameter.get("Username"),date/1000,
							ksession);
					}
					else if (msg.parameter.containsKey("Room")){
						if(!msg.parameter.containsKey("Date")){
							date = new Date().getTime();
						}
						else date = Long.getLong(msg.parameter.get("Date"));
					CourseResp = GetNextCourseByRoom(msg.parameter.get("Room"),date/1000,
							ksession);
					}
					
					CSMessage responseMessage = new CSMessage();
					if (CourseResp != null) {
						responseMessage.text = CourseResp.size()
								+ " Next Course found!";
						int i = 0;
						while (CourseResp.size() > i) {
							responseMessage.payload.add(CourseResp.get(i));
							i++;
						}
					} else{
						responseMessage.text = msg.parameter.get("Username")
								+ " next Course not found!";
					
					responseMessage.payload.add(null);
					}
					Message respmsg = session
							.createObjectMessage(responseMessage);
					session.createProducer(message.getJMSReplyTo()).send(respmsg);
					System.out.println("[EI]: Sent response ["
							+ responseMessage.text + "]");
				}
				else if (msg.text.contains("getActualCourse")) {
					Long date = null;
					System.out.println("[EI]: ActualCourseRequest received");
					if (msg.parameter.containsKey("Username")){
						if(!msg.parameter.containsKey("Date")){
							date = new Date().getTime();
						}
						else date = Long.getLong(msg.parameter.get("Date"));
					CourseResp = GetActualCourseByUsername(msg.parameter.get("Username"),date/1000,
							ksession);
					}
					else if (msg.parameter.containsKey("Room")){
						if(!msg.parameter.containsKey("Date")){
							date = new Date().getTime();
						}
						else date = Long.getLong(msg.parameter.get("Date"));
					CourseResp = GetActualCourseByRoom(msg.parameter.get("Room"),date/1000,
							ksession);
					}
					
					CSMessage responseMessage = new CSMessage();
					if (CourseResp != null) {
						responseMessage.text = CourseResp.size()
								+ " Next Course found!";
						int i = 0;
						while (CourseResp.size() > i) {
							responseMessage.payload.add(CourseResp.get(i));
							i++;
						}
					} else{
						responseMessage.text = msg.parameter.get("Username")
								+ " next Course not found!";
					
					responseMessage.payload.add(null);
					}
					Message respmsg = session
							.createObjectMessage(responseMessage);
					session.createProducer(message.getJMSReplyTo()).send(respmsg);
					System.out.println("[EI]: Sent response ["
							+ responseMessage.text + "]");
				}
				else if (msg.text.contains("getActualLecture")) {
					Long date = null;
					System.out.println("[EI]: ActualLectureRequest received");
					if (msg.parameter.containsKey("Username")){
						if(!msg.parameter.containsKey("Date")){
							date = new Date().getTime();
						}
						else date = Long.getLong(msg.parameter.get("Date"));
					LectureResp = GetActualLectureByUsername(msg.parameter.get("Username"),date/1000,
							ksession);
					}
					else if (msg.parameter.containsKey("Room")){
						if(!msg.parameter.containsKey("Date")){
							date = new Date().getTime();
						}
						else date = Long.getLong(msg.parameter.get("Date"));
					LectureResp = GetActualLectureByRoom(msg.parameter.get("Room"),date/1000,
							ksession);
					}
					
					CSMessage responseMessage = new CSMessage();
					if (CourseResp != null) {
						responseMessage.text = LectureResp.size()
								+ " Next Course found!";
						int i = 0;
						while (CourseResp.size() > i) {
							responseMessage.payload.add(LectureResp.get(i));
							i++;
						}
					} else{
						responseMessage.text = msg.parameter.get("Username")
								+ " next Course not found!";
					
					responseMessage.payload.add(null);
					}
					Message respmsg = session
							.createObjectMessage(responseMessage);
					session.createProducer(message.getJMSReplyTo()).send(respmsg);
					System.out.println("[EI]: Sent response ["
							+ responseMessage.text + "]");
				}
				else if (msg.text.contains("getAllEvents")) {
					System.out.println("[EI]: AllEvents received");
					EventResp = GetAllEvents(ksession);
					
					CSMessage responseMessage = new CSMessage();
					if (EventResp != null) {
						responseMessage.text = EventResp.size()
								+ " event(s) found!";
						int i = 0;
						while (EventResp.size() > i) {
							responseMessage.payload.add(EventResp.get(i));
							i++;
						}
					} else{
						responseMessage.text = "No event(s) found!";
					
					responseMessage.payload.add(null);
					}
					Message respmsg = session
							.createObjectMessage(responseMessage);
					session.createProducer(message.getJMSReplyTo()).send(respmsg);
					System.out.println("[EI]: Sent response ["
							+ responseMessage.text + "]");
				}
				else if (msg.text.contains("getAllEntities")) {
					System.out.println("[EI]: AllEntities received");
					EntityResp = GetAllEntities(ksession);
					
					CSMessage responseMessage = new CSMessage();
					if (EntityResp != null) {
						responseMessage.text = EntityResp.size()
								+ " entities(s) found!";
						int i = 0;
						while (EntityResp.size() > i) {
							responseMessage.payload.add(EntityResp.get(i));
							i++;
						}
					} else{
						responseMessage.text = "No entities(s) found!";
					
					responseMessage.payload.add(null);
					}
					Message respmsg = session
							.createObjectMessage(responseMessage);
					session.createProducer(message.getJMSReplyTo()).send(respmsg);
					System.out.println("[EI]: Sent response ["
							+ responseMessage.text + "]");
				}
				else if (msg.text.contains("getEntity")) {
					System.out.println("[EI]: GetEntity received");
					String key = msg.parameter.get("key");
					String value = msg.parameter.get("value");
					EntityResp = GetEntity(key, value, ksession);
					
					CSMessage responseMessage = new CSMessage();
					if (EntityResp != null) {
						responseMessage.text = EntityResp.size()
								+ " entities(s) found!";
						int i = 0;
						while (EntityResp.size() > i) {
							responseMessage.payload.add(EntityResp.get(i));
							i++;
						}
					} else{
						responseMessage.text = "No entities(s) found!";
					
					responseMessage.payload.add(null);
					}
					Message respmsg = session
							.createObjectMessage(responseMessage);
					session.createProducer(message.getJMSReplyTo()).send(respmsg);
					System.out.println("[EI]: Sent response ["
							+ responseMessage.text + "]");
				}
				else {
					System.out.println("[EI]: Request unrecognized!");
				}

			}

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	

	private static ArrayList<Event> GetAllEvents(StatefulKnowledgeSession ksession){
		ArrayList<Event> list = new ArrayList<Event>();
		Event event = null;
		QueryResults results = ksession.getQueryResults( "GetAllEvents");
//		System.out.println( "we have " + results.size() + " events" );
		
		for ( QueryResultsRow row : results ) {
			event = ( Event ) row.get( "e" );
		    list.add(event);
		}
		return list;
	}
	
	private static ArrayList<Entity> GetAllEntities(StatefulKnowledgeSession ksession){
		ArrayList<Entity> list = new ArrayList<Entity>();
		Entity entity = null;
		QueryResults results = ksession.getQueryResults( "GetAllEntities");
//		System.out.println( "we have " + results.size() + " events" );
		
		for ( QueryResultsRow row : results ) {
			entity = ( Entity ) row.get( "e" );
		    list.add(entity);
		}
		return list;
	}
	
	private static ArrayList<Entity> GetEntity(String key, String value, StatefulKnowledgeSession ksession){
		Object[] myargs = new Object[2];
		myargs[0] = key;
		myargs[1] = value;
		ArrayList<Entity> list = new ArrayList<Entity>();
		Entity entity = null;
		System.out.println("GetEntity with key="+key+" and value="+value);    
		QueryResults results = ksession.getQueryResults( "GetEntity"/*, myargs*/);
		System.out.println( "we have " + results.size() + " Entities" );
		
		for ( QueryResultsRow row : results ) {   
			entity = ( Entity ) row.get( "e" );
		    list.add(entity);   
		} 
		return list;
	}	
	
	private static ArrayList<Course> GetCoursesByUsername(String Username, StatefulKnowledgeSession ksession){
		Object[] myargs = new Object[1];
		myargs[0] = Username;
		ArrayList<Course> list = new ArrayList<Course>();
		Course course = null;
		QueryResults results = ksession.getQueryResults( "GetCoursesByUsername", myargs );
//		System.out.println( "we have " + results.size() + " events" );
		
		for ( QueryResultsRow row : results ) {
			course = ( Course ) row.get( "c" );
		    list.add(course);
		}
		return list;
	}  
	
	private static ArrayList<Course> GetCoursesByTitle(String Title, StatefulKnowledgeSession ksession){
		Object[] myargs = new Object[1];
		myargs[0] = Title;
		ArrayList<Course> list = new ArrayList<Course>();
		Course course = null;
		QueryResults results = ksession.getQueryResults( "GetCoursesByTitle", myargs );
//		System.out.println( "we have " + results.size() + " events" );
		
		for ( QueryResultsRow row : results ) {
			course = ( Course ) row.get( "c" );
		    list.add(course);
		}
		return list;
		
	}

	private static ArrayList<Lecture> GetLecturesByTitle(String Title, StatefulKnowledgeSession ksession){
		Object[] myargs = new Object[1];
		myargs[0] = Title;
		ArrayList<Lecture> list = new ArrayList<Lecture>();
		Lecture lecture = null;
		QueryResults results = ksession.getQueryResults( "GetLecturesByTitle", myargs );
//		System.out.println( "we have " + results.size() + " events" );
		
		for ( QueryResultsRow row : results ) {
			lecture = ( Lecture ) row.get( "l" );
//			System.out.println(lecture.getTitle() +", "+ new Date(lecture.getBegin()*1000));
		    list.add(lecture);
		}
		return list;
		
	}

	private static ArrayList<Course> GetCoursesByDate(Long date, StatefulKnowledgeSession ksession){
		Object[] myargs = new Object[1];
		myargs[0] = date;
		ArrayList<Course> list = new ArrayList<Course>();
		Course course = null;
		QueryResults results = ksession.getQueryResults( "GetCoursesByDate", myargs );
		System.out.println( "we have " + results.size() + " courses" );
		
		for ( QueryResultsRow row : results ) {
			course = ( Course ) row.get( "c" );
			System.out.println(course.getTitle() +", hat "+ new Date(date*1000)+" eine Veranstaltung ");
			list.add(course);
		}
		return list;
		
	}

	private static ArrayList<Course> GetCoursesByRoom(String room, StatefulKnowledgeSession ksession){
		Object[] myargs = new Object[1];
		myargs[0] = room;
		ArrayList<Course> list = new ArrayList<Course>();
		Course course = null;
		QueryResults results = ksession.getQueryResults( "GetCoursesByRoom", myargs );
		System.out.println( "we have " + results.size() + " courses" );
		
		for ( QueryResultsRow row : results ) {
			course = ( Course) row.get( "c" );
			System.out.println(course.getTitle() +", hat in "+ room+" eine Veranstaltung ");
			list.add(course);
		}
		return list;
		
	}

	private static ArrayList<Course> GetCoursesByRoomAndByDate(String room, Long date ,StatefulKnowledgeSession ksession){
		Object[] myargs = new Object[2];
		myargs[0] = room;
		myargs[1] = date;
		ArrayList<Course> list = new ArrayList<Course>();
		Course course = null;
		QueryResults results = ksession.getQueryResults( "GetCoursesByRoomAndByDate", myargs );
		System.out.println( "we have " + results.size() + " courses" );
		
		for ( QueryResultsRow row : results ) {
			course = ( Course) row.get( "c" );
			System.out.println(course.getTitle() +", hat in "+ room+" eine Veranstaltung am "+ new Date(date*1000));
			list.add(course);
		}
		return list;
		
	}

	private static ArrayList<Course> GetNextCourseByRoom(String room, Long date ,StatefulKnowledgeSession ksession){
		Object[] myargs = new Object[2];
		myargs[0] = room;
		myargs[1] = date;
		ArrayList<Course> list = new ArrayList<Course>();
		Course course = null;
		Lecture lecture = null;
		QueryResults results = ksession.getQueryResults( "GetNextCourseByRoom", myargs );
		System.out.println( "we have " + results.size() + " courses" );
		
		for ( QueryResultsRow row : results ) {
			course = ( Course) row.get( "c" );
			lecture = (Lecture) row.get("l");
			System.out.println(course.getTitle() +", hat in "+ room+" eine Veranstaltung am "+ new Date(lecture.getBegin()*1000));
			list.add(course);
		}
		return list;
		
	}

	private static ArrayList<Course> GetNextCourseByUsername(String username, Long date ,StatefulKnowledgeSession ksession){
		Object[] myargs = new Object[2];
		myargs[0] = username;
		myargs[1] = date;
		ArrayList<Course> list = new ArrayList<Course>();
		Person person = null;
		Lecture lecture = null;
		Course course = null;
		QueryResults results = ksession.getQueryResults( "GetNextCourseByUsername", myargs );
		System.out.println( "we have " + results.size() + " courses" );
		
		for ( QueryResultsRow row : results ) {
			person = (Person) row.get("p");
			course = ( Course) row.get( "c" );
			lecture = (Lecture) row.get("l");
			System.out.println(person.getFirstname() +", hat "+ new Date(lecture.getBegin()*1000) +" die Veranstaltung "+ lecture.getTitle() + " in " + lecture.getRoom().getTitle() );
			list.add(course);
		}
		return list;
		
	}
	
	private static ArrayList<Course> GetActualCourseByUsername (String username, Long date ,StatefulKnowledgeSession ksession){
		Object[] myargs = new Object[2];
		myargs[0] = username;
		myargs[1] = date;
		ArrayList<Course> list = new ArrayList<Course>();
		Person person = null;
		Lecture lecture = null;
		Course course = null;
		QueryResults results = ksession.getQueryResults( "GetActualCourseByUsername", myargs );
		System.out.println( username + " has " + results.size() + " courses now" );
		
		for ( QueryResultsRow row : results ) {
			person = (Person) row.get("p");
			course = ( Course) row.get( "c" );
			lecture = (Lecture) row.get("l");
			System.out.println(person.getFirstname() +", hat "+ new Date(lecture.getBegin()*1000) +" die Veranstaltung "+ lecture.getTitle() + " in " + lecture.getRoom().getTitle() );
			list.add(course);
		}
		return list;
		
	}
	
	private static ArrayList<Course> GetActualCourseByRoom (String room, Long date ,StatefulKnowledgeSession ksession){
		Object[] myargs = new Object[2];
		myargs[0] = room;
		myargs[1] = date;
		ArrayList<Course> list = new ArrayList<Course>();
		Room temp_room;
		Lecture lecture = null;
		Course course = null;
		QueryResults results = ksession.getQueryResults( "GetActualCourseByUsername", myargs );
		System.out.println( room + " has " + results.size() + " courses now" );
		
		for ( QueryResultsRow row : results ) {
			temp_room = (Room) row.get("r");
			course = ( Course) row.get( "c" );
			lecture = (Lecture) row.get("l");
			System.out.println("In "+temp_room.getTitle() +" takes  "+ lecture.getTitle() + " since "+ new Date(lecture.getBegin()*1000) +" place" );
			list.add(course);
		}
		return list;
		
	}
	
	private static ArrayList<Lecture> GetActualLectureByUsername (String username, Long date ,StatefulKnowledgeSession ksession){
		Object[] myargs = new Object[2];
		myargs[0] = username;
		myargs[1] = date;
		ArrayList<Lecture> list = new ArrayList<Lecture>();
		Person person = null;
		Lecture lecture = null;
		Course course = null;
		QueryResults results = ksession.getQueryResults( "GetActualLectureByUsername", myargs );
		System.out.println( username + " has " + results.size() + " lectures now" );
		
		for ( QueryResultsRow row : results ) {
			person = (Person) row.get("p");
			course = ( Course) row.get( "c" );
			lecture = (Lecture) row.get("l");
			System.out.println(person.getFirstname() +", hat "+ new Date(lecture.getBegin()*1000) +" die Veranstaltung "+ lecture.getTitle() + " in " + lecture.getRoom().getTitle() );
			list.add(lecture);
		}
		return list;
		
	}
	
	private static ArrayList<Lecture> GetActualLectureByRoom (String room, Long date ,StatefulKnowledgeSession ksession){
		Object[] myargs = new Object[2];
		myargs[0] = room;
		myargs[1] = date;
		ArrayList<Lecture> list = new ArrayList<Lecture>();
		Room temp_room;
		Lecture lecture = null;
		Course course = null;
		QueryResults results = ksession.getQueryResults( "GetActualLectureByUsername", myargs );
		System.out.println( room + " has " + results.size() + " lectures now" );
		
		for ( QueryResultsRow row : results ) {
			temp_room = (Room) row.get("r");
			course = ( Course) row.get( "c" );
			lecture = (Lecture) row.get("l");
			System.out.println("In "+temp_room.getTitle() +" takes  "+ lecture.getTitle() + " since "+ new Date(lecture.getBegin()*1000) +" place" );
			list.add(lecture);
		}
		return list;
		
	}
	

}
