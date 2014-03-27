package de.lehsten.casa.contextserver.test;

import static org.junit.Assert.*;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.junit.Test;

public class SyntacticRuleTest {

	@Test
	public void SyntacticTestStudIPQueries(){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/studip/StudIPQueries.drl"),
				ResourceType.DRL);			
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		assertEquals("Error in StudIPQueries.drl", 0 , errors.size());
	}
	
	@Test
	public void SyntacticTestStudIPRequestHandler(){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/studip/StudIPRequestHandler.drl"),
				ResourceType.DRL);			
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		for(KnowledgeBuilderError  e : errors){
			System.err.println(e);
		}
		assertEquals("Error in StudIPRequestHandler.drl", 0 , errors.size());

	}

	@Test
	public void SyntacticTestStudIPTransformationRules(){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/studip/StudIPTransformationRules.drl"),
				ResourceType.DRL);			
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		assertEquals("Error in StudIPTransformationRules.drl", 0 , errors.size());
	}
	
	@Test
	public void SyntacticTestEventService_NextCourse(){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/studip/EventService_NextCourse.drl"),
				ResourceType.DRL);			
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		assertEquals("Error in EventService_NextCourse.drl", 0 , errors.size());
	}

	@Test
	public void SyntacticTestServiceQueries(){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/service/ServiceQueries.drl"),
				ResourceType.DRL);			
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		assertEquals("Error in ServiceQueries.drl", 0 , errors.size());
	}
	
	@Test
	public void SyntacticTestLocationRules(){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/location/LocationRules.drl"),
				ResourceType.DRL);			
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		assertEquals("Error in LocationRules.drl", 0 , errors.size());
	}
	
	@Test
	public void SyntacticTestHandleLocationUpdates(){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/location/HandleLocationUpdates.drl"),
				ResourceType.DRL);			
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		assertEquals("Error in HandleLocationUpdates.drl", 0 , errors.size());
	}

	@Test
	public void SyntacticTestLocationService_Cafeteria(){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/location/LocationService_Cafeteria.drl"),
				ResourceType.DRL);			
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		assertEquals("Error in LocationService_Cafeteria.drl", 0 , errors.size());
	}
	
	@Test
	public void SyntacticTestLocationService_PublicTransport(){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/location/LocationService_PublicTransport.drl"),
				ResourceType.DRL);			
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		assertEquals("Error in LocationService_PublicTransport.drl", 0 , errors.size());
	}
	
	@Test
	public void SyntacticTestNotification_LactoseFreeMenu(){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/location/Notification_LactoseFreeMenu.drl"),
				ResourceType.DRL);			
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		assertEquals("Error in Notification_LactoseFreeMenu.drl", 0 , errors.size());
	}
	
	@Test
	public void SyntacticTestQueries_Location(){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/location/Queries_Locations.drl"),
				ResourceType.DRL);			
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		assertEquals("Error in Queries_Locations.drl", 0 , errors.size());
	}
	
}
