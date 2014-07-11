package de.lehsten.casa.contextserver.test.studip;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

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
import org.drools.runtime.rule.QueryResultsRow;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
 
import de.lehsten.casa.contextserver.CASAContextServer;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.Request;
import de.lehsten.casa.contextserver.types.StudIPRequest;
import de.lehsten.casa.contextserver.types.entities.device.MobilePhone;
import de.lehsten.casa.contextserver.types.entities.event.Course;
import de.lehsten.casa.contextserver.types.entities.event.Event;
import de.lehsten.casa.contextserver.types.entities.event.Lecture;
import de.lehsten.casa.contextserver.types.entities.person.identity.Identity;
import de.lehsten.casa.contextserver.types.entities.person.identity.StudIPIdentity;
import de.lehsten.casa.contextserver.types.entities.place.Building;
import de.lehsten.casa.contextserver.types.entities.place.Cafeteria;
import de.lehsten.casa.contextserver.types.entities.place.Place;
import de.lehsten.casa.contextserver.types.entities.place.rooms.Room;
import de.lehsten.casa.contextserver.types.entities.services.websites.EventWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;

public class SemanticRuleTest_StudIPRequestHandler {

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
			kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/studip/StudIPRequestHandler.drl"),
					ResourceType.DRL);
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
	public void QueryTest_GetRequestById() {
		//config the query
		
		String queryName = "GetRequestById";
		Object[] arguments = new Object[1];
		arguments[0] = "QueryTest_GetRequestById";
		//create objects
		
		Lecture l1 = new Lecture();
		l1.setSource("Source1");
		l1.setTitle("myLecture");
		l1.setStudIP_ID("thisIsAsGoodAsAStudipId");
		StudIPIdentity i1 = new StudIPIdentity();
		i1.setStudip_role("dozent");
		Request request = new Request();
		ArrayList<Entity> restrictions = new ArrayList<Entity>();
		restrictions.add(l1);
		restrictions.add(i1);
		request.setRestrictions(restrictions);
		request.setRequestId("QueryTest_GetRequestById");
		//insert objects
		ksession.insert(request);
		//fire Query
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		//
		assertEquals(1, results.size());
		//
	}
	
	@Test
	public void QueryTest_HandleRequests() {
		//config the query
		Lecture l1 = new Lecture();
		l1.setSource("Source1");
		l1.setTitle("myLecture");
		l1.setStudIP_ID("thisIsAsGoodAsAStudipId");
		StudIPIdentity i1 = new StudIPIdentity();
		i1.setStudip_role("dozent");
		Request request = new Request();
		ArrayList<Entity> restrictions = new ArrayList<Entity>();
		restrictions.add(l1);
		restrictions.add(i1);
		request.setRestrictions(restrictions);
		request.setRequestId("QueryTest_HandleRequests");
		//insert objects
		ksession.insert(request);
		
		//fire query
		int rulesFired = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "HandleRequests" ) );	
		assertEquals(1, rulesFired);
		//
	}
	
	@Test
	public void QueryTest_HandleJsonStudIPRequests() {
		//config the query
		Lecture l1 = new Lecture();
		l1.setSource("Source1");
		l1.setTitle("myLecture");
		l1.setStudIP_ID("thisIsAsGoodAsAStudipId");
		StudIPIdentity i1 = new StudIPIdentity();
		i1.setStudip_role("dozent");
		Request request = new Request();
		ArrayList<Entity> restrictions = new ArrayList<Entity>();
		restrictions.add(l1);
		restrictions.add(i1);
		request.setRestrictions(restrictions);
		StudIPRequest studipRequest = new StudIPRequest(request);
		studipRequest.setRequestId("QueryTest_HandleJsonRequests");
		Gson gson = new Gson();
		studipRequest.getProperties().put("json",gson.toJson(request));
		//insert objects
		ksession.insert(studipRequest);
		//fire query
		int rulesFired = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "HandleJsonStudIPRequests" ) );	
		assertEquals(1, rulesFired);
		//
	}
	
	@Test
	public void QueryTest_HandleStudIPRequests_Lecture() {
		//create objects
		
		Lecture l1 = new Lecture();
		l1.setSource("Source1");
		l1.setTitle("myLecture");
		l1.setStudIP_ID("thisIsAsGoodAsAStudipId");
		StudIPIdentity i1 = new StudIPIdentity();
		i1.setStudip_role("dozent");
		EventWebsite e1 = new EventWebsite(l1);
		e1.addProperty("StudIP_ID", l1.getStudIP_ID());
		e1.setSource("Source1.5");
		e1.addRestriction("dozent");
		e1.setEvent(l1);
		Request request = new Request();
		ArrayList<Entity> restrictions = new ArrayList<Entity>();
		restrictions.add(l1);
		restrictions.add(i1);
		request.setRestrictions(restrictions);
		StudIPRequest studipRequest = new StudIPRequest(request);
		studipRequest.setRequestId("QueryTest_HandleStudIPRequests_Lecture");
		//insert objects
		ksession.insert(studipRequest);
		ksession.insert(l1);
		ksession.insert(e1);
		//fire query
		int rulesFiredSecond = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "HandleStudIPRequests" ) );
		assertEquals(1, rulesFiredSecond);
		//get the request back out of the session
		String queryName = "GetRequestById";
		Object[] arguments = new Object[1];
		arguments[0] = "QueryTest_HandleStudIPRequests_Lecture";
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		// is it only the one request we expected?
		assertEquals(1, results.size());
		//get the results from the request
		ArrayList<Entity> list = new ArrayList<Entity>();
		for ( QueryResultsRow row : results ) {
			Entity e = ( Entity) row.get( "r" );
			list.add(e);
		}
		// does it contain the service as result?
		assertEquals(1, ((StudIPRequest)list.get(0)).getResults().size());
				
	}
	
	@Test
	public void QueryTest_HandleStudIPRequests_Room() {
		//create objects
		Room r1 = new Room();
		r1.setSource("Source1");
		r1.setTitle("SmartLab");
		StudIPIdentity i1 = new StudIPIdentity();
		i1.setStudip_role("dozent");
		LocationWebsite l1 = new LocationWebsite(r1);
		l1.setSource("Source1.5");
		l1.addRestriction("dozent");
		l1.setPlace(r1);
		Request request = new Request();
		ArrayList<Entity> restrictions = new ArrayList<Entity>();
		restrictions.add(r1);
		restrictions.add(i1);
		request.setRestrictions(restrictions);
		StudIPRequest studipRequest = new StudIPRequest(request);
		studipRequest.setRequestId("QueryTest_HandleStudIPRequests_Room");
		//insert objects
		ksession.insert(studipRequest);
		ksession.insert(r1);
		ksession.insert(l1);
		//fire query
		int rulesFiredSecond = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "HandleStudIPRequests" ) );
		//did the rule fire?
		assertEquals(1, rulesFiredSecond);
		//get the request back out of the session
		String queryName = "GetRequestById";
		Object[] arguments = new Object[1];
		arguments[0] = "QueryTest_HandleStudIPRequests_Room";
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		// is it only the one request we expected?
		assertEquals(1, results.size());
		//get the results from the request
		ArrayList<Entity> list = new ArrayList<Entity>();
		for ( QueryResultsRow row : results ) {
			Entity e = ( Entity) row.get( "r" );
			list.add(e);
		}
		// does it contain the service as result?
		assertEquals(1, ((StudIPRequest)list.get(0)).getResults().size());
		//
		
	}
	
	@Test
	public void QueryTest_HandleStudIPRequests_GetServiceByLocationAndRole() {
		//config the query
		
		String queryName = "GetRequestById";
		Object[] arguments = new Object[1];
		arguments[0] = "QueryTest_HandleStudIPRequests_GetServiceByLocationAndRole";
		//create objects
		Room r1 = new Room();
		r1.setSource("Source1");
		r1.setTitle("SmartLab");
		StudIPIdentity i1 = new StudIPIdentity();
		i1.setStudip_role("dozent");
		Request request = new Request();
		ArrayList<Entity> restrictions = new ArrayList<Entity>();
		restrictions.add(r1);
		restrictions.add(i1);
		request.setRestrictions(restrictions);
		request.setRequestId("QueryTest_HandleStudIPRequests_GetServiceByLocationAndRole");
		LocationWebsite l1 = new LocationWebsite(r1);
		l1.setSource("Source1.5");
		l1.addRestriction("dozent");
		//insert objects
		ksession.insert(l1);
		ksession.insert(r1);
		ksession.insert(request);
		//fire rules
		int rulesFired_1 = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "HandleRequests" ) );	
		assertEquals(1, rulesFired_1);
		Collection<Object> coll = ksession.getObjects();
		for(Object o : coll){
			if(o instanceof StudIPRequest){System.out.println(o);}
		}
//		int rulesFired_2 = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "HandleStudIPRequests" ) );
//		System.out.println(rulesFired_2);
//		assertEquals(1, rulesFired_2);
		
		//get the request back out of the knowledge session
		QueryResults results= ksession.getQueryResults( queryName, arguments );
		// is it only the one request we expected?
		assertEquals(1, results.size());
		ArrayList<Entity> list = new ArrayList<Entity>();
		for ( QueryResultsRow row : results ) {
			Entity e = ( Entity) row.get( "r" );
			list.add(e);
		}
		ksession.insert(list.get(0));
		ksession.fireAllRules();
		// does it contain the service as result?
		assertEquals(1, ((StudIPRequest)list.get(0)).getResults().size());
		//
	}
	
	
	
}
