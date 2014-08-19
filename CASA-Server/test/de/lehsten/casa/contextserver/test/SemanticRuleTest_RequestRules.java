package de.lehsten.casa.contextserver.test;

import static org.junit.Assert.assertEquals;

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
import org.drools.runtime.rule.QueryResultsRow;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lehsten.casa.contextserver.CASAContextServer;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.QueryRequest;
import de.lehsten.casa.contextserver.types.StudIPRequest;
import de.lehsten.casa.contextserver.types.entities.event.Course;
import de.lehsten.casa.contextserver.types.entities.event.Lecture;
import de.lehsten.casa.contextserver.types.entities.place.rooms.Room;
import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.entities.services.websites.EventWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;


public class SemanticRuleTest_RequestRules {

	CASAContextServer cs;
	KnowledgeBase kbase; 
	StatefulKnowledgeSession ksession;

	@Before
	public void setUp() throws Exception {
		setupServer();
	}
	
	@After
	public void tearDown() throws Exception {
//		   long l = ksession.getFactCount();
           ksession.dispose();	 
//    	   System.out.println("Knowledge session with "+ l +" facts disposed.");
	}

	private void setupServer(){
		try {
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/request/RequestRules.drl"),
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
	public void RequestTest_noArguments() {
		//config the query
		String queryName = "GetServices";
		Object[] arguments = new Object[0];
		QueryRequest qrequest = new QueryRequest();
		qrequest.getQuery().put(queryName, arguments);
		qrequest.setRequestId("RequestTest_noArguments");
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
		ksession.insert(qrequest);
		//fire query
		int rulesFiredSecond = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "HandleQueryRequest" ) );
		assertEquals(1, rulesFiredSecond);
		//get the request back out of the session
		queryName = "GetRequestById";
		arguments = new Object[1];
		arguments[0] = "RequestTest_noArguments";
		QueryResults results= ksession.getQueryResults( ((String)queryName), arguments );
		// is it only the one request we expected?
		assertEquals(1, results.size());
		//get the results from the request
		ArrayList<Entity> list = new ArrayList<Entity>();
		for ( QueryResultsRow row : results ) {
			Entity e = ( Entity) row.get( "r" );
			list.add(e);
		}
		// does it contain the service as result?
		assertEquals(4, ((QueryRequest)list.get(0)).getResults().size());

	}
	
	@Test
	public void RequestTest_oneArgument() {
		// config the query
		String queryName = "GetServiceByUsername";
		Object[] arguments = new Object[1];
		arguments[0] = "pl039";
		QueryRequest qrequest = new QueryRequest();
		qrequest.getQuery().put(queryName, arguments);
		qrequest.setRequestId("RequestTest_oneArgument");
		//create objects
		Website w1 = new Website();
		w1.setSource("Source1.5");
		w1.addRestriction("pl039");
		//insert objects
		ksession.insert(w1);
		ksession.insert(qrequest);
		//fire query
		int rulesFiredSecond = ksession.fireAllRules( new RuleNameEqualsAgendaFilter( "HandleQueryRequest" ) );
		assertEquals(1, rulesFiredSecond);
		//get the request back out of the session
		queryName = "GetRequestById";
		arguments = new Object[1];
		arguments[0] = "RequestTest_oneArgument";
		QueryResults results= ksession.getQueryResults( ((String)queryName), arguments );
		// is it only the one request we expected?
		assertEquals(1, results.size());
		//get the results from the request
		ArrayList<Entity> list = new ArrayList<Entity>();
		for ( QueryResultsRow row : results ) {
			Entity e = ( Entity) row.get( "r" );
			list.add(e);
		}
		// does it contain the service as result?
		assertEquals(1, ((QueryRequest)list.get(0)).getResults().size());

	}
	
}
	

