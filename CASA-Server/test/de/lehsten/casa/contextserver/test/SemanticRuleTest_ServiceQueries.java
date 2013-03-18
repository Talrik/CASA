package de.lehsten.casa.contextserver.test;

import static org.junit.Assert.assertEquals;

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
import de.lehsten.casa.contextserver.types.entities.event.Lecture;
import de.lehsten.casa.contextserver.types.entities.place.rooms.Room;
import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.entities.services.websites.EventWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;

public class SemanticRuleTest_ServiceQueries {

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
			kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/service/ServiceQueries.drl"),
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
	public void QueryTest_GetServices() {
		//config the query
		String queryName = "GetServices";
		Object[] arguments = new Object[0];
		//create objects
		Service s1 = new Service();
		s1.setSource("Source1");
		Website s2 = new Website();
		s2.setSource("Source2");
		LocationWebsite s3 = new LocationWebsite();
		s3.setSource("Source3");
		EventWebsite s4 = new EventWebsite();
		s4.setSource("Source4");
		//insert objects
		ksession.insert(s1);
		ksession.insert(s2);
		ksession.insert(s3);
		ksession.insert(s4);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(4, results.size());
	}

	@Test
	public void QueryTest_GetServiceByLecture() {
		//config the query
		String queryName = "GetServiceByLecture";
		Object[] arguments = new Object[1];
		arguments[0] = "myLecture";
		//create objects
		Lecture l1 = new Lecture();
		l1.setSource("Source1");
		l1.setTitle("myLecture");
		EventWebsite e1 = new EventWebsite(l1);
		e1.setSource("Source1.5");
		Course c1 = new Course();
		c1.setSource("Source1.7");
		c1.addLecture(l1);
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
		ksession.insert(e1);
		ksession.insert(c1);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	@Test
	public void QueryTest_GetServiceByLecture2() {
		//config the query
		String queryName = "GetServiceByLecture";
		Object[] arguments = new Object[1];
		arguments[0] = "thisIsAsGoodAsAStudipId";
		//create objects
		Lecture l1 = new Lecture();
		l1.setSource("Source1");
		l1.setTitle("myLecture");
		l1.setStudIP_ID("thisIsAsGoodAsAStudipId");
		EventWebsite e1 = new EventWebsite(l1);
		e1.setSource("Source1.5");
		Course c1 = new Course();
		c1.setSource("Source1.7");
		c1.addLecture(l1);
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
		ksession.insert(e1);
		ksession.insert(c1);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	@Test
	public void QueryTest_GetServiceByLectureAndRole() {
		//config the query
		String queryName = "GetServiceByLectureAndRole";
		Object[] arguments = new Object[2];
		arguments[0] = "thisIsAsGoodAsAStudipId";
		arguments[1] = "dozent";
		//create objects
		Lecture l1 = new Lecture();
		l1.setSource("Source1");
		l1.setTitle("myLecture");
		l1.setStudIP_ID("thisIsAsGoodAsAStudipId");
		EventWebsite e1 = new EventWebsite(l1);
		e1.addProperty("StudIP_ID", "thisIsAsGoodAsAStudipId");
		e1.setSource("Source1.5");
		e1.addRestriction("dozent");
		//insert objects
		ksession.insert(l1);
		ksession.insert(e1);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	
	@Test
	public void QueryTest_GetServiceByLocationAndRole() {
		//config the query
		String queryName = "GetServiceByLocationAndRole";
		Object[] arguments = new Object[2];
		arguments[0] = "SmartLab";
		arguments[1] = "dozent";
		//create objects
		Room r1 = new Room();
		r1.setSource("Source1");
		r1.setTitle("SmartLab");
		LocationWebsite l1 = new LocationWebsite(r1);
		l1.setSource("Source1.5");
		l1.addRestriction("dozent");
		//insert objects
		ksession.insert(l1);
		ksession.insert(r1);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
	@Test
	public void QueryTest_GetServiceByUsername() {
		//config the query
		String queryName = "GetServiceByUsername";
		Object[] arguments = new Object[1];
		arguments[0] = "pl039";
		//create objects
		Website w1 = new Website();
		w1.setSource("Source1.5");
		w1.addRestriction("pl039");
		//insert objects
		ksession.insert(w1);
		//fire query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
	}
}
	

