package de.lehsten.casa.contextserver.test.studip;

import static org.junit.Assert.*;

import java.util.Date;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.AssertBehaviorOption;
import org.drools.conf.EventProcessingOption;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.QueryResults;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lehsten.casa.contextserver.CASAContextServer;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.event.Course;
import de.lehsten.casa.contextserver.types.entities.event.Event;
import de.lehsten.casa.contextserver.types.entities.event.Lecture;
import de.lehsten.casa.contextserver.types.entities.person.Person;
import de.lehsten.casa.contextserver.types.entities.person.identity.Identity;
import de.lehsten.casa.contextserver.types.entities.person.identity.StudIPIdentity;
import de.lehsten.casa.contextserver.types.entities.place.rooms.Room;

public class SemanticRuleTest_StudIPQueries {

	CASAContextServer cs;
	KnowledgeBase kbase; 
	StatefulKnowledgeSession ksession;


	@Before
	public void setUp() throws Exception {
		setupServer();
	}
	
	@After
	public void tearDown() throws Exception {
		   long l = ksession.getFactCount();
           ksession.dispose();	 
    	   System.out.println("Knowledge session with "+ l +" facts disposed.");
	}

	private void setupServer(){
		try {
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/studip/StudIPQueries.drl"),
					ResourceType.DRL);
			KnowledgeBuilderErrors errors = kbuilder.getErrors();
			if (errors.size() > 0) {
				for (KnowledgeBuilderError error : errors) {
					System.err.println(error);
				}
				throw new IllegalArgumentException("Could not parse knowledge.");
			}
			KnowledgeBaseConfiguration config = KnowledgeBaseFactory
				.newKnowledgeBaseConfiguration();
			config.setOption(EventProcessingOption.STREAM);
			config.setOption(AssertBehaviorOption.EQUALITY);
			kbase = KnowledgeBaseFactory.newKnowledgeBase(config);
			kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		ksession = kbase.newStatefulKnowledgeSession();
		KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
				.newFileLogger(ksession, "Log_ContextServer");

	}
	
	@Test
	public void QueryTest_GetEntityByPropertyAndValue() {
		//config the query
		String queryName = "GetEntityByPropertyAndValue";
		Object[] arguments = new Object[2];
		arguments[0] = "StudIP_ID";
		arguments[1] = "1234567890";
		//create objects
		Lecture event = new Lecture();
		event.setStudIP_ID("1234567890");
		event.setSource("StudIP");
		event.setBeginDate(new Date(Long.parseLong("1356998400000")));
		event.setEndDate(new Date(Long.parseLong("1356998400000")));

		//insert objects
		ksession.insert(event);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	@Test
	public void QueryTest_GetAllEntities() {
		//config the query
		String queryName = "GetAllEntities";
		Object[] arguments = new Object[0];
		//create objects
		Entity e = new Entity();
		e.setSource("Source");
		//insert objects
		ksession.insert(e);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	@Test
	public void QueryTest_GetAllPersons() {
		//config the query
		String queryName = "GetAllPersons";
		Object[] arguments = new Object[0];
		//create objects
		Entity e = new Entity();
		e.setSource("Source");
		Person p = new Person();
		p.setSource("Source1");
		//insert objects
		ksession.insert(e);
		ksession.insert(p);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	@Test
	public void QueryTest_GetIdentityByUsername() {
		//config the query
		String queryName = "GetIdentityByUsername";
		Object[] arguments = new Object[1];
		arguments[0] = "myUsername";
		//create objects
		Identity i = new Identity();
		i.setSource("Source1");
		i.setIdentityUserName("myUsername");
		//insert objects
		ksession.insert(i);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}

	@Test
	public void QueryTest_GetAllEvents() {
		//config the query
		String queryName = "GetAllEvents";
		Object[] arguments = new Object[0];
		//create objects
		Event e = new Event();
		e.setSource("Source1");
		//insert objects
		ksession.insert(e);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	@Test
	public void QueryTest_GetAllCourses() {
		//config the query
		String queryName = "GetAllCourses";
		Object[] arguments = new Object[0];
		//create objects
		Course c = new Course();
		c.setSource("Source1");
		//insert objects
		ksession.insert(c);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}

	@Test
	public void QueryTest_GetCoursesByUsername() {
		//config the query
		String queryName = "GetCoursesByUsername";
		Object[] arguments = new Object[1];
		arguments[0] = "myUsername";
		//create objects
		Course c = new Course();
		c.setSource("Source1");
		Identity i = new Identity();
		i.setSource("Source2");
		i.setIdentityUserName("myUsername");
		c.addMember(i);
		//insert objects
		ksession.insert(c);
		ksession.insert(i);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	@Test
	public void QueryTest_GetCoursesByTitle() {
		//config the query
		String queryName = "GetCoursesByTitle";
		Object[] arguments = new Object[1];
		arguments[0] = "myCourse";
		//create objects
		Course c1 = new Course();
		c1.setSource("Source1");
		c1.setTitle("myCourse");
		Course c2 = new Course();
		c2.setTitle("myCourse");
		c2.setSource("Source2");
		Course c3 = new Course();
		c3.setSource("Source3");
		c3.setTitle("yourCourse");
		//insert objects
		ksession.insert(c1);
		ksession.insert(c2);
		ksession.insert(c3);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(2, results.size());
	}
	
	@Test
	public void QueryTest_GetLecturesByTitle() {
		//config the query
		String queryName = "GetLecturesByTitle";
		Object[] arguments = new Object[1];
		arguments[0] = "myLecture";
		//create objects
		Lecture l1 = new Lecture();
		l1.setSource("Source1");
		l1.setTitle("myLecture");
		Lecture l2 = new Lecture();
		l2.setTitle("myLecture");
		l2.setSource("Source2");
		Lecture l3 = new Lecture();
		l3.setSource("Source3");
		l3.setTitle("yourLecture");
		//insert objects
		ksession.insert(l1);
		ksession.insert(l2);
		ksession.insert(l3);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(2, results.size());
	}
	
	@Test
	public void QueryTest_GetLecturesByRoom() {
		//config the query
		String queryName = "GetLecturesByRoom";
		Object[] arguments = new Object[1];
		arguments[0] = "SmartLab";
		//create objects
		Lecture l1 = new Lecture();
		l1.setSource("Source1");
		l1.setTitle("myLecture");
		Room r1 = new Room();
		r1.setTitle("SmartLab");
		r1.setSource("Source2");
		l1.setRoom(r1);
		Lecture l2 = new Lecture();
		l2.setSource("Source3");
		l2.setTitle("yourLecture");
		//insert objects
		ksession.insert(l1);
		ksession.insert(r1);
		ksession.insert(l2);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	@Test
	public void QueryTest_GetCoursesByDate() {
		//config the query
		String queryName = "GetCoursesByDate";
		Object[] arguments = new Object[1];
		Date now = new Date();
		arguments[0] = now;
		//create objects
		Lecture l1 = new Lecture();
		l1.setSource("Source1");
		l1.setTitle("myLecture");
		long beginTime = now.getTime() - 1800;
		long endTime = now.getTime() + 1800;
		l1.setBeginDate(new Date(beginTime));
		l1.setEndDate(new Date(endTime));
		Course c1 = new Course();
		c1.setSource("Source3");
		c1.setTitle("myCourse");
		c1.addLecture(l1);
		//insert objects
		ksession.insert(l1);
		ksession.insert(c1);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	@Test
	public void QueryTest_GetCoursesByRoom() {
		//config the query
		String queryName = "GetCoursesByRoom";
		Object[] arguments = new Object[1];
		arguments[0] = "SmartLab";
		//create objects
		Room r1 = new Room();
		r1.setSource("Source1");
		r1.setTitle("SmartLab");
		r1.setResource_id("12345");
		Course c1 = new Course();
		c1.setSource("Source3");
		c1.setTitle("myCourse");
		c1.addResource("12345");
		//insert objects
		ksession.insert(r1);
		ksession.insert(c1);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}

	@Test
	public void QueryTest_GetCoursesByRoomAndDate() {
		//config the query
		String queryName = "GetCoursesByRoomAndDate";
		Object[] arguments = new Object[2];
		arguments[0] = "SmartLab";
		Date now = new Date();
		arguments[1] = now;
		//create objects
		Room r1 = new Room();
		r1.setSource("Source1");
		r1.setTitle("SmartLab");
		r1.setResource_id("12345");
		Lecture l1 = new Lecture();
		l1.setSource("Source2");
		l1.setTitle("myLecture");
		long beginTime = now.getTime() - 1800;
		long endTime = now.getTime() + 1800;
		l1.setBeginDate(new Date(beginTime));
		l1.setEndDate(new Date(endTime));
		Course c1 = new Course();
		c1.setSource("Source3");
		c1.setTitle("myCourse");
		c1.addResource(r1.getResource_id());
		c1.addLecture(l1);
		//insert objects
		ksession.insert(l1);
		ksession.insert(r1);
		ksession.insert(c1);
		//fire query		
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		assertEquals(1, results.size());
	}
	@Test
	public void QueryTest_GetNextCourseByRoom() {
		//config the query
		String queryName = "GetNextCourseByRoom";
		Object[] arguments = new Object[2];
		arguments[0] = "SmartLab";
		Date now = new Date();
		arguments[1] = now;
		//create objects
		Room r1 = new Room();
		r1.setSource("Source1");
		r1.setTitle("SmartLab");
		r1.setResource_id("12345");
		Lecture l1 = new Lecture();
		l1.setSource("Source2");
		l1.setTitle("myLecture");
		long beginTime = now.getTime() + 1800;
		long endTime = now.getTime() + 3600;
		l1.setBeginDate(new Date(beginTime));
		l1.setEndDate(new Date(endTime));
		Course c1 = new Course();
		c1.setSource("Source3");
		c1.setTitle("myCourse");
		c1.addLecture(l1);
		c1.addResource("12345");
		//insert objects
		ksession.insert(r1);
		ksession.insert(l1);
		ksession.insert(c1);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	@Test
	public void QueryTest_GetNextCourseByUsername() {
		//config the query
		String queryName = "GetNextCourseByUsername";
		Object[] arguments = new Object[2];
		arguments[0] = "myUsername";
		Date now = new Date();
		arguments[1] = now;
		//create objects
		Identity i1 = new Identity();
		i1.setIdentityUserName("myUsername");
		i1.setSource("Source1");
		Lecture l1 = new Lecture();
		l1.setSource("Source2");
		l1.setTitle("myLecture");
		long beginTime = now.getTime() + 1800;
		long endTime = now.getTime() + 3600;
		l1.setBeginDate(new Date(beginTime));
		l1.setEndDate(new Date(endTime));
		Course c1 = new Course();
		c1.setSource("Source3");
		c1.setTitle("myCourse");
		c1.addLecture(l1);
		c1.addMember(i1);
		//insert objects
		ksession.insert(i1);
		ksession.insert(l1);
		ksession.insert(c1);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	@Test
	public void QueryTest_GetActualCourseByUsername() {
		//config the query
		String queryName = "GetActualCourseByUsername";
		Object[] arguments = new Object[2];
		arguments[0] = "myUsername";
		Date now = new Date();
		arguments[1] = now;
		//create objects
		Identity i1 = new Identity();
		i1.setIdentityUserName("myUsername");
		i1.setSource("Source1");
		Lecture l1 = new Lecture();
		l1.setSource("Source2");
		l1.setTitle("myLecture");
		long beginTime = now.getTime() - 1800;
		long endTime = now.getTime() + 1800;
		l1.setBeginDate(new Date(beginTime));
		l1.setEndDate(new Date(endTime));
		Course c1 = new Course();
		c1.setSource("Source3");
		c1.setTitle("myCourse");
		c1.addLecture(l1);
		c1.addMember(i1);
		//insert objects
		ksession.insert(i1);
		ksession.insert(l1);
		ksession.insert(c1);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}

	@Test
	public void QueryTest_GetActualCourseByRoom() {
		//config the query
		String queryName = "GetActualCourseByRoom";
		Object[] arguments = new Object[2];
		arguments[0] = "SmartLab";
		Date now = new Date();
		arguments[1] = now;
		//create objects
		Room r1 = new Room();
		r1.setSource("Source1");
		r1.setTitle("SmartLab");
		r1.setResource_id("12345");
		Lecture l1 = new Lecture();
		l1.setSource("Source2");
		l1.setTitle("myLecture");
		long beginTime = now.getTime() - 1800;
		long endTime = now.getTime() + 1800;
		l1.setBeginDate(new Date(beginTime));
		l1.setEndDate(new Date(endTime));
		Course c1 = new Course();
		c1.setSource("Source3");
		c1.setTitle("myCourse");
		c1.addLecture(l1);
		c1.addResource("12345");
		//insert objects
		ksession.insert(r1);
		ksession.insert(l1);
		ksession.insert(c1);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	@Test
	public void QueryTest_GetActualLectureByUsername() {
		//config the query
		String queryName = "GetActualLectureByUsername";
		Object[] arguments = new Object[2];
		arguments[0] = "myUsername";
		Date now = new Date();
		arguments[1] = now;
		//create objects
		Identity i1 = new Identity();
		i1.setIdentityUserName("myUsername");
		i1.setSource("Source1");
		Lecture l1 = new Lecture();
		l1.setSource("Source2");
		l1.setTitle("myLecture");
		long beginTime = now.getTime() - 1800;
		long endTime = now.getTime() + 1800;
		l1.setBeginDate(new Date(beginTime));
		l1.setEndDate(new Date(endTime));
		Course c1 = new Course();
		c1.setSource("Source3");
		c1.setTitle("myCourse");
		c1.addLecture(l1);
		c1.addMember(i1);
		//insert objects
		ksession.insert(i1);
		ksession.insert(l1);
		ksession.insert(c1);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	@Test
	public void QueryTest_GetActualLectureByRoom() {
		//config the query
		String queryName = "GetActualLectureByRoom";
		Object[] arguments = new Object[2];
		arguments[0] = "SmartLab";
		Date now = new Date();
		arguments[1] = now;
		//create objects
		Room r1 = new Room();
		r1.setSource("Source1");
		r1.setTitle("SmartLab");
		r1.setResource_id("12345");
		Lecture l1 = new Lecture();
		l1.setSource("Source2");
		l1.setTitle("myLecture");
		long beginTime = now.getTime() - 1800;
		long endTime = now.getTime() + 1800;
		l1.setBeginDate(new Date(beginTime));
		l1.setEndDate(new Date(endTime));
		Course c1 = new Course();
		c1.setSource("Source3");
		c1.setTitle("myCourse");
		c1.addLecture(l1);
		c1.addResource("12345");
		//insert objects
		ksession.insert(r1);
		ksession.insert(l1);
		ksession.insert(c1);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	@Test
	public void QueryTest_GetNextRoomByUsername() {
		//config the query
		String queryName = "GetNextRoomByUsername";
		Object[] arguments = new Object[2];
		arguments[0] = "myUsername";
		Date now = new Date();
		arguments[1] = now;
		//create objects
		Identity i1 = new Identity();
		i1.setIdentityUserName("myUsername");
		i1.setSource("Source1");
		Room r1 = new Room();
		r1.setSource("Source1");
		r1.setTitle("SmartLab");
		r1.setResource_id("12345");
		Lecture l1 = new Lecture();
		l1.setSource("Source2");
		l1.setTitle("myLecture");
		l1.setRoom(r1);
		long beginTime = now.getTime() + 1800;
		long endTime = now.getTime() + 3600;
		l1.setBeginDate(new Date(beginTime));
		l1.setEndDate(new Date(endTime));
		Course c1 = new Course();
		c1.setSource("Source3");
		c1.setTitle("myCourse");
		c1.addLecture(l1);
		c1.addMember(i1);
		//insert objects
		ksession.insert(r1);
		ksession.insert(i1);
		ksession.insert(l1);
		ksession.insert(c1);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	@Test
	public void QueryTest_GetActualRoomByUsername() {
		//config the query
		String queryName = "GetActualRoomByUsername";
		Object[] arguments = new Object[2];
		arguments[0] = "myUsername";
		Date now = new Date();
		arguments[1] = now;
		//create objects
		Identity i1 = new Identity();
		i1.setIdentityUserName("myUsername");
		i1.setSource("Source1");
		Room r1 = new Room();
		r1.setSource("Source1");
		r1.setTitle("SmartLab");
		r1.setResource_id("12345");
		Lecture l1 = new Lecture();
		l1.setSource("Source2");
		l1.setTitle("myLecture");
		long beginTime = now.getTime() - 1800;
		long endTime = now.getTime() + 1800;
		l1.setBeginDate(new Date(beginTime));
		l1.setEndDate(new Date(endTime));
		l1.setRoom(r1);
		Course c1 = new Course();
		c1.setSource("Source3");
		c1.setTitle("myCourse");
		c1.addLecture(l1);
		c1.addMember(i1);
		//insert objects
		ksession.insert(r1);
		ksession.insert(i1);
		ksession.insert(l1);
		ksession.insert(c1);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	
}
