package de.lehsten.casa.contextserver.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lehsten.casa.contextserver.types.entities.place.Cafeteria;

public class ProcessTest {
	
	StatefulKnowledgeSession ksession;

	@Before
	public void setUp() throws Exception {
		// load up the knowledge base
		KnowledgeBase kbase = readKnowledgeBase();
		
		
		
	 ksession = kbase.newStatefulKnowledgeSession();
		// start a new process instance
	}

	@After
	public void tearDown() throws Exception {
		ksession.dispose();
	}

	@Test
	public void test() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("latitude", 54d);
		params.put("longitude", 12d);
		params.put("time", new Date());
		
		ksession.insert(new Cafeteria());
		
		ksession.startProcess("de.lehsten.casa.process.location", params);
	}

	
	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("location.bpmn"), ResourceType.BPMN2);
		return kbuilder.newKnowledgeBase();
	}
}
