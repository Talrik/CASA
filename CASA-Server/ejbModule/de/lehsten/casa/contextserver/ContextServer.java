/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lehsten.casa.contextserver;

import de.lehsten.casa.contextserver.factentry.FactEntryStudIP;
import de.lehsten.casa.contextserver.importer.CASAImporterManager;
import de.lehsten.casa.contextserver.importer.studip.StudIP_DB_Event_Gatherer;
import de.lehsten.casa.contextserver.importer.studip.StudIP_DB_Inventory_Gatherer;
import de.lehsten.casa.contextserver.importer.studip.StudIP_DB_Person_Gatherer;
import de.lehsten.casa.contextserver.importer.studip.StudIP_DB_Room_Gatherer;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.event.Event;
import de.lehsten.casa.contextserver.types.entities.place.rooms.Room;
import de.lehsten.casa.contextserver.types.xml.CSMessage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties; 

import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.ejb.Stateful;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.StatefulSession;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.conf.EventProcessingOption;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.ClassObjectFilter;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;


/**
 *
 * @author phil
 */
@Startup
@Singleton
public class ContextServer implements IContextServer{ 
    
    KnowledgeBase kbase; 
    StatefulKnowledgeSession ksession;
    IContextServer server;
    
    public ContextServer(){
    	
    	InitialContext ctx;
		try {
			ctx = new InitialContext();
		server = (IContextServer) ctx.lookup("java:global/CASA-Server/IContextServer");
		} catch (NamingException e2) { 
			// TODO Auto-generated catch block
			//e2.printStackTrace();
		}
		if (server == null)
		{
		System.out.println("First initialization.");
		
        try { 
        	
        	setupBase();
        	
//			StudIP_DB_Collector s1 = new StudIP_DB_Collector();
//        	CASAImporterManager im = new CASAImporterManager();
//        	im.startImporter(null);
/*			FactEntryStudIP e1 = new FactEntryStudIP(ksession);			
			e1.initialGather(); 
*/
			//			PublicTraffic_Collector p1 = new PublicTraffic_Collector();
			
			System.out.println("----- KnowledgeBase wurde geladen -----");
			System.out.println("----- "+ksession.getFactCount()+" Objekte enthaten -----");
			
			
			System.out.println("----- Regeln werden angewendet -----");

			ksession.fireAllRules();
//			ksession.fireUntilHalt(); 

			System.out.println("----- Initialisierung beendet -----"); 
			   	        

		} catch (Exception e) {
			e.printStackTrace();
		}
		}
        else{ 
        	try {
        		
			} catch (Throwable e) { 
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		}
    
    }
    
    public StatefulKnowledgeSession getSession(){ 
    	return ksession; 
    }
    
    public int status(){
    	return ksession.getObjects().size();    	
    }
    
    @PreDestroy
    public void shutdown(){
    	   long l = ksession.getFactCount();
           ksession.dispose();	 
    	   System.out.println("Knowledge session with "+ l +" facts disposed.");
    }

    private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("StudIPTransformationRules.drl"),
				ResourceType.DRL);
		kbuilder.add(ResourceFactory.newClassPathResource("StudIPQueries.drl"),
				ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}
    
    private void setupBase(){
   		
		try {
			kbase = readKnowledgeBase();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ksession = kbase.newStatefulKnowledgeSession();

		KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
				.newFileLogger(ksession, "Log_ContextServer");
		KnowledgeBaseConfiguration config = KnowledgeBaseFactory
				.newKnowledgeBaseConfiguration();
		config.setOption(EventProcessingOption.STREAM);

    }
    
    private void altSetupBase(){
    	PackageBuilder builder = new PackageBuilder();
    	try{ 
    		builder.addPackageFromDrl(new InputStreamReader(ContextServer.class.getResourceAsStream("StudIPTransformationRules.drl")));
    		builder.addPackageFromDrl(new InputStreamReader(ContextServer.class.getResourceAsStream("StudIPQueries.drl")));
    	} catch (DroolsParserException e) {
            throw new IllegalArgumentException("Invalid drl", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read drl", e);
        }
    	RuleBase ruleBase = RuleBaseFactory.newRuleBase();
    	ruleBase.addPackages(builder.getPackages());
    	
    	StatefulSession ssession = ruleBase.newStatefulSession();
    	
    	
    }

	@Override
	public void startServer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopServer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEntity(Entity e) {
		// TODO Auto-generated method stub
		ksession.insert(e);
	}

	@Override
	public void removeEntity(Entity e) {
		// TODO Auto-generated method stub
		ksession.retract(ksession.getFactHandle(e));
		
	}

	@Override
	public void updateEntity(Entity oldEntity, Entity newEntity) {
		// TODO Auto-generated method stub
		ksession.retract(ksession.getFactHandle(oldEntity));
		ksession.insert(newEntity);
	}

	@Override
	public void applyRules() {
		// TODO Auto-generated method stub
		ksession.fireAllRules();
	}

	@Override
	public long getFactCount() {
		// TODO Auto-generated method stub
		
		return ksession.getFactCount();
	}
    
}
