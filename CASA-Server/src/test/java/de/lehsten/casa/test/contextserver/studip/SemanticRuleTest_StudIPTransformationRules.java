package de.lehsten.casa.test.contextserver.studip;

import static org.junit.Assert.*;

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
import org.drools.runtime.rule.Activation;
import org.drools.runtime.rule.AgendaFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lehsten.casa.contextserver.CASAContextServer;
import de.lehsten.casa.contextserver.types.entities.device.MobilePhone;
import de.lehsten.casa.contextserver.types.entities.event.Course;
import de.lehsten.casa.contextserver.types.entities.event.Lecture;
import de.lehsten.casa.contextserver.types.entities.person.identity.StudIPIdentity;
import de.lehsten.casa.contextserver.types.entities.place.Building;
import de.lehsten.casa.contextserver.types.entities.place.Cafeteria;
import de.lehsten.casa.contextserver.types.entities.place.Place;
import de.lehsten.casa.contextserver.types.entities.place.rooms.Room;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;

public class SemanticRuleTest_StudIPTransformationRules {

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
			kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/studip/StudIPTransformationRules.drl"),
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
	
	private class RuleNameEqualsAgendaFilter implements
    AgendaFilter {
  private final String  name;

  private final boolean accept;

  public RuleNameEqualsAgendaFilter(final String name) {
      this( name,
            true );
  }

  public RuleNameEqualsAgendaFilter(final String name,
                                    final boolean accept) {
      this.name = name;
      this.accept = accept;
  }

  @Override
  public boolean accept(Activation activation) {
      if ( activation.getRule().getName().equals( this.name ) ) {
          return this.accept;
      } else {
          return !this.accept;
      }
  }

}

	
	@Test
	public void RuleTest_ConnectLecturesWithCourses() {
		//create requires data
		Course c1 = new Course();
		c1.setSource("Source1");
		c1.setStudIP_ID("12345");
		Lecture l1 = new Lecture();
		l1.setSource("Source2");
		l1.setStudIP_ID("12345");
		l1.setisAssigned(false);
		//insert facts
		ksession.insert(c1);
		ksession.insert(l1);
		//fire rules
	   int rulesFired = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "ConnectLecturesWithCourses" ) );
	   assertEquals(1, rulesFired);
	}
	
	@Test
	public void RuleTest_ConnectLectureWithRooms() {
		//create requires data
		Lecture l1 = new Lecture();
		l1.setSource("Source2");
		l1.setStudIP_ID("12345");
		l1.setisAssigned(false);
		l1.addResource("6789");
		Room r1 = new Room();
		r1.setSource("Source3");
		r1.setResource_id("6789");
		Course c1 = new Course();
		c1.setSource("Source1");
		c1.setStudIP_ID("12345");
		c1.addLecture(l1);
		//insert facts
		ksession.insert(c1);
		ksession.insert(l1);
		ksession.insert(r1);
		//fire rules
	   int rulesFired = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "ConnectLectureWithRooms" ) );
	   assertEquals(1, rulesFired);
	}
	
	@Test
	public void RuleTest_ConnectPersonsWithCourses() {

		//create requires data
		Course c1 = new Course();
		c1.setSource("Source1");
		c1.setStudIP_ID("12345");
		StudIPIdentity i1 = new StudIPIdentity("TestUserID");
		i1.setSource("Source3");
		i1.setStudip_user_id("TestUserID");
		c1.addMembersID("TestUserID");
		//insert facts
		ksession.insert(i1);
		ksession.insert(c1);
		//fire rules
		   int rulesFired = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "ConnectPersonsWithCourses" ) );
		   assertEquals(1, rulesFired); 
	}
	
	@Test
	public void RuleTest_ConnectRoomsWithBuildings() {
		//create requires data
		Room r1 = new Room();
		r1.setSource("Source3");
		r1.setResource_id("6789");
		Building b1 = new Building();
		b1.setSource("Source2");
		b1.addRoomID("6789");	
		//insert facts
		ksession.insert(r1);
		ksession.insert(b1);
		//fire rules
		int rulesFired = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "ConnectRoomsWithBuildings" ) );
	   assertEquals(1, rulesFired);
	}
	
	@Test
	public void RuleTest_ConnectWebsitesWithEvents() {
		//create requires data
		Lecture l1 = new Lecture();
		l1.setSource("Source3");
		l1.setStudIP_ID("id12345");
		Website w1 = new Website();
		w1.setSource("Source2");
		w1.setTitle("Uni Homepage");
		w1.setTargetURL("http://www.uni-rostock.de");
		w1.addProperty("StudIP_ID", "id12345");	
		//insert facts
		ksession.insert(l1);
		ksession.insert(w1);
		//fire rules
		int rulesFired = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "ConnectWebsitesWithEvents" ) );
	   assertEquals(1, rulesFired);
	}
	@Test
	public void RuleTest_ConnectWebsitesWithLocations() {
		//create requires data
		Place p1 = new Place();
		p1.setSource("Source3");
		p1.setTitle("Zuse-Haus");
		Website w1 = new Website();
		w1.setSource("Source2");
		w1.setTitle("ITMZ Homepage");
		w1.setTargetURL("http://www.itmz.uni-rostock.de");
		w1.addProperty("StudIP_Location", "Zuse-Haus");	
		//insert facts
		ksession.insert(p1);
		ksession.insert(w1);
		//fire rules
		int rulesFired = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "ConnectWebsitesWithLocations" ) );
	   assertEquals(1, rulesFired);
	}
	
}
