package de.lehsten.casa.test.contextserver;

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
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.QueryResultsRow;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lehsten.casa.contextserver.CASAContextServer;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.event.Lecture;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;

public class ContextServerStartUp {
	
	CASAContextServer cs;
	KnowledgeBase kbase; 
	StatefulKnowledgeSession ksession;
	KnowledgeRuntimeLogger logger;
	   
	@Test
	public void testUpdateEntity() {
			//create required data
			Website w1 = new Website();
			w1.setSource("Source1");
			w1.setTitle("Uni Homepage");
			w1.setTargetURL("http://www.uni-rostock.de");
			w1.addProperty("StudIP_ID", "id12345");	
			Website w2 = new Website();
			w2.setSource("Source2");
			w2.setTitle("Uni Homepage");
			w2.setTargetURL("http://www.uni-greifswald.de");
			w2.addProperty("StudIP_ID", "id54321");	
			//insert facts
			ksession.insert(w1);
			//run operation
			Object[] arguments = new Object[2];
			arguments[0] = "StudIP_ID";
			arguments[1] = "id12345";
			
			ArrayList<Entity> list = new ArrayList<Entity>();
			QueryResults results=ksession.getQueryResults( "GetEntityByPropertyAndValue", arguments );
			for ( QueryResultsRow row : results ) {
				Entity e = ( Entity) row.get( "r" );
				list.add(e);
			}
			if (list.size() == 1){
				ksession.update(ksession.getFactHandle(list.get(0)), w2);
			}else{
			//TODO ERRORHANDLING
			}
			arguments[1] = "id54321";
			results = ksession.getQueryResults("GetEntityByPropertyAndValue", arguments);
			assertEquals(1, results.size());
			assertEquals(1,ksession.getFactCount());
		}

	@Before
	public void setUp() throws Exception {
//		cs = new CASAContextServer();
				setupServer();
	}

	@After
	public void tearDown() throws Exception {
		   long l = ksession.getFactCount();
           ksession.dispose();	 
    	   System.out.println("Knowledge session with "+ l +" facts disposed.");
//    	   cs.shutdown();
	}

	

	
	private void setupServer(){
		try {
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			
			kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/studip/StudIPTransformationRules.drl"),
					ResourceType.DRL);			
			kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/service/ServiceQueries.drl"),
					ResourceType.DRL);
			kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/studip/StudIPQueries.drl"),
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
		logger = KnowledgeRuntimeLoggerFactory
				.newFileLogger(ksession, "Log_ContextServer");

	}
	

	
	}