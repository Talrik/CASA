package de.lehsten.casa.contextserver.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
import org.drools.runtime.rule.QueryResults;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lehsten.casa.contextserver.CASAContextServer;
import de.lehsten.casa.contextserver.types.entities.device.MobilePhone;
import de.lehsten.casa.contextserver.types.entities.place.Cafeteria;
import de.lehsten.casa.contextserver.types.entities.place.Place;
import de.lehsten.casa.contextserver.types.entities.place.Stop;
import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.contextserver.types.position.MovementEvent;

public class SemanticRuleTest_LocationRules {
	
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
	
	@Test
	public void RuleTest_CafeteriaTest() {
		//create requires data
		Cafeteria c = new Cafeteria();
		c.setSource("Test");
		MobilePhone m = new MobilePhone();
		m.setSource("Test2");
		//insert facts
		ksession.insert(c);
		ksession.insert(m);
		//fire rules
	   int rulesFired = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "CafeteriaTest" ) );
	   assertEquals(1, rulesFired);
	}

	@Test
	public void RuleTest_LactoseFreeMenuNotification() {
		//create requires data
		Cafeteria c = new Cafeteria();
		c.setSource("Test");
		c.lactosefree_menu_available = true;
		MobilePhone m = new MobilePhone();
		m.setSource("Test2");
		ArrayList<Place> places = new ArrayList<Place>();
		places.add(c);
		m.setNearbyPlaces(places);
		//insert facts
		ksession.insert(c);
		ksession.insert(m);
		//fire rules
	   int rulesFired = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "LactoseFreeMenuNotification" ) );
	   assertEquals(1, rulesFired);
	}
	
	

	@Test
	public void RuleTest_HandleLocationUpdates() {
		//create requires data
		MobilePhone m = new MobilePhone();
		m.setSource("Test2");
		m.setDeviceID("TestID");
		MovementEvent e = new MovementEvent();
		e.setSource("Test");
		e.setDeviceID("TestID");
		//insert facts
		ksession.insert(e);
		ksession.insert(m);
		//fire rules
	   int rulesFired = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "HandleLocationUpdates" ) );
	   assertEquals(1, rulesFired);
	}
	
	@Test
	public void RuleTest_InsertPublicTransportService() {
		//create requires data
		Stop s =  new Stop();
		s.setSource("");
		s.setStopNumber(1234567);
		//insert facts
		ksession.insert(s);
		//fire rules
	   int rulesFired = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "InsertPublicTransportService" ) );
	   assertEquals(1, rulesFired);
	   assertEquals(2,ksession.getFactCount());
	   
	}
	
	
	@Test
	public void RuleTest_AddStopLocationToPublicTransportService() {
		//create requires data
		Stop s1 =  new Stop();
		s1.setSource("Source1");
		s1.setTitle("Stop1");
		s1.setStopNumber(123456);
		LocationWebsite lw1 = new LocationWebsite();
		lw1.setSource("Source2");
		lw1.setPlace(s1);
		lw1.setDescription(s1.getTitle());
		LocationWebsite lw2 = new LocationWebsite();
		lw2.setSource("Source3");
		lw2.setDescription(s1.getTitle());
		//insert facts
		ksession.insert(s1);
		ksession.insert(lw1);
		ksession.insert(lw2);
		//fire rules
	   int rulesFired = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "AddStopLocationToPublicTransportService" ) );
	   assertEquals(2,ksession.getFactCount());
	   assertEquals(1, rulesFired);
	}
	
	@Test
	public void QueryTest_GetClosestStop() {
		//config the query
		String queryName = "GetClosestStop";
		Object[] arguments = new Object[2];
		arguments[0] = 0.5d;
		arguments[1] = 0.5d;
		//create objects
		//create requires data
		Stop s1 =  new Stop();
		s1.setSource("Source1");
		s1.setTitle("Stop1");
		s1.setStopNumber(123456);
		s1.setLatitude(1d);
		s1.setLongitude(1d);

		Stop s2 =  new Stop();
		s2.setSource("Source2");
		s2.setTitle("Stop2");
		s2.setStopNumber(789101);
		s2.setLatitude(2d);
		s2.setLongitude(2d);
		//insert facts
		ksession.insert(s1);
		ksession.insert(s2);
		//fire rules
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());

	}
	
	@Test
	public void QueryTest_GetClosestStopWebsite() {
		//config the query
		String queryName = "GetClosestStopWebsite";
		Object[] arguments = new Object[2];
		arguments[0] = 0.5d;
		arguments[1] = 0.5d;
		//create objects
		//create requires data
		Stop s1 =  new Stop();
		s1.setSource("Source1");
		s1.setTitle("Stop1");
		s1.setStopNumber(123456);
		s1.setLatitude(1d);
		s1.setLongitude(1d);
		Stop s2 =  new Stop();
		s2.setSource("Source2");
		s2.setTitle("Stop2");
		s2.setStopNumber(789101);
		s2.setLatitude(2d);
		s2.setLongitude(2d);
		//insert facts
		ksession.insert(s1);
		ksession.insert(s2);
		//fire rules
		int rulesFired = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "InsertPublicTransportService" ) );
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(2, rulesFired);
		assertEquals(1, results.size());

	}
	
	@Test
	public void QueryTest_GetCloseStopWebsites() {
		//config the query
		String queryName = "GetCloseStopWebsites";
		Object[] arguments = new Object[3];
		arguments[0] = 54.0774664d;
		arguments[1] = 12.1067652d;
		arguments[2] = 1d;
		//create objects
		//create requires data
		Stop s1 =  new Stop();
		s1.setSource("Source1");
		s1.setTitle("Mensa");
		s1.setStopNumber(123456);
		s1.setLatitude(54.0742779d);
		s1.setLongitude(12.1049459d);
		Stop s2 =  new Stop();
		s2.setSource("Source2");
		s2.setTitle("Hauptbahnhof Rostock");
		s2.setStopNumber(789101);
		s2.setLatitude(54.078056d);
		s2.setLongitude(12.130833d);
		//insert facts
		ksession.insert(s1);
		ksession.insert(s2);
		//fire rules
		int rulesFired = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "InsertPublicTransportService" ) );
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(2, rulesFired);
		assertEquals(1, results.size());

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

	
	
	
	private void setupServer(){
		try {
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/location/HandleLocationUpdates.drl"),
					ResourceType.DRL);
			kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/location/LocationService_Cafeteria.drl"),
					ResourceType.DRL);
			kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/location/LocationService_PublicTransport.drl"),
					ResourceType.DRL);
			kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/location/Notification_LactoseFreeMenu.drl"),
					ResourceType.DRL);
			kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/location/Queries_Locations.drl"),
					ResourceType.DRL);
			kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/location/LocationRules.drl"),
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
}

