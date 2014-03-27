/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lehsten.casa.contextserver;

import de.lehsten.casa.contextserver.controller.CSRouteBuilder;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.rules.LocalRuleProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.conf.AssertBehaviorOption;
import org.drools.conf.EventProcessingOption;
import org.drools.definition.KnowledgePackage;
import org.drools.definition.rule.Rule;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.marshalling.Marshaller;
import org.drools.marshalling.MarshallerFactory;
import org.drools.marshalling.ObjectMarshallingStrategy;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.QueryResultsRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author phil
 */
@Startup
@Singleton
public class CASAContextServer implements ContextServer{ 
    
    KnowledgeBase kbase; 
    StatefulKnowledgeSession ksession;
    ContextServer server;
    CSRouteBuilder builder;
    PackageBuilderConfiguration cfg;
    LocalRuleProvider ruleProvider;
    ArrayList<de.lehsten.casa.contextserver.types.Rule> rules = new ArrayList<de.lehsten.casa.contextserver.types.Rule>();
    private final static Logger log = LoggerFactory.getLogger( CASAContextServer.class ); 
    JmDNS jmDNS;
	ServiceInfo serviceInfo;
	
	String description = "Standard CASA-Server";
    String developer = "Philipp Lehsten";
    String requiredVersionOfCASATypes = "0.1.25";
	
    public CASAContextServer(){

    	Properties properties = new Properties();
    	properties.setProperty( "drools.dialect.java.compiler.lnglevel","1.6" );
    	cfg =   new PackageBuilderConfiguration( properties );
    	InitialContext ctx;
		try {
			ctx = new InitialContext();
		server = (ContextServer) ctx.lookup("java:global/CASA-Server/ContextServer");
		log.info("Found "+ctx.lookup("java:global/CASA-Server/ContextServer"));
		} catch (NamingException e2) { 
			log.error(e2.getMessage());
		}
		init();
    }
    
    private void init(){
    	if (server == null)
		{
		log.info("First initialization.");
		try {
			new InitialContext().rebind("CASAServer", this);
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		builder = new CSRouteBuilder();
        try { 
        	
        	setupBase();	
        	 
			log.info("----- KnowledgeBase created -----");
			log.info("----- Initialisierung beendet -----"); 
//			UserAgent ua = new UserAgent(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
        String serviceType = "_casa._tcp.local.";
		String serviceName = "ContextServer";
		int port = 8080;

		try {
			jmDNS = JmDNS.create();
		
		Map<String, byte[]> props = new HashMap<String, byte[]>();
		String test ="TestValue";
		props.put("title", test.getBytes());
		props.put("requiredVersionOfCASATypes", this.requiredVersionOfCASATypes.getBytes());
		props.put("developer", this.developer.getBytes());
		props.put("description", this.description.getBytes());
		
		serviceInfo = ServiceInfo.create(
				  serviceType,
				  serviceName,
				  port,
				  0,
				  0,
				  false,
				  props);
		// Service starten
		jmDNS.registerService(serviceInfo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
    	try{
    		jmDNS.unregisterService (serviceInfo);
    		jmDNS.close();
    		long l = ksession.getFactCount();
    		ksession.dispose();	 
    		log.info("Knowledge session with "+ l +" facts disposed.");
    		builder.stopContext();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }


    
    private void setupBase(){
   		
		try {
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(cfg);
			log.info("Running LocalRuleProvider");
			ruleProvider = new LocalRuleProvider();
			
			ArrayList<de.lehsten.casa.contextserver.types.Rule> ruleList = ruleProvider.getRuleList();
			for (de.lehsten.casa.contextserver.types.Rule rule: ruleList){
				kbuilder.add(ResourceFactory.newReaderResource(new StringReader(rule.getRule())), ResourceType.DRL);
				KnowledgeBuilderErrors errors = kbuilder.getErrors();
				if (errors.size() > 0) {
					for (KnowledgeBuilderError error : errors) {
						System.err.println(error);
					}
					throw new IllegalArgumentException("Could not parse knowledge.");
				}
			}
			
			try{
			kbuilder.add(ResourceFactory.newClassPathResource("StudIPTransformationRules.drl"),
					ResourceType.DRL);
			kbuilder.add(ResourceFactory.newClassPathResource("StudIPQueries.drl"),
					ResourceType.DRL);	
			kbuilder.add(ResourceFactory.newClassPathResource("StudIPRequestHandler.drl"),
					ResourceType.DRL);	
			kbuilder.add(ResourceFactory.newClassPathResource("ServiceQueries.drl"),
							ResourceType.DRL);
			kbuilder.add(ResourceFactory.newClassPathResource("LocationRules.drl"),
					ResourceType.DRL);
			}catch(Exception e){
				
			}
//			kbuilder.add(ResourceFactory.newClassPathResource("DebugRules.drl"),ResourceType.DRL);	
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
			//			LocalRuleProvider l = new LocalRuleProvider();
    		for (de.lehsten.casa.contextserver.types.Rule r :ruleProvider.getRuleList()){
    			this.addRule(r);
    		}
			rules = (ArrayList<de.lehsten.casa.contextserver.types.Rule>) this.getRules();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KnowledgeSessionConfiguration conf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
		conf.setOption( ClockTypeOption.get( "realtime" ) );
		ksession = kbase.newStatefulKnowledgeSession(conf,null);
		log.info("New session initialized");
		KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
				.newFileLogger(ksession, "Log_ContextServer");
		
		
    }

	@Override
	public void startServer() {
		log.info("Starting Server");
		setupBase();
	}

	@Override
	public void stopServer() {
		long l = ksession.getFactCount();
       ksession.dispose();	 
       log.info("Knowledge session with "+ l +" facts disposed.");

	}
	
	public void storeSession(){
		try {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Marshaller marshaller = MarshallerFactory.newMarshaller(kbase, new ObjectMarshallingStrategy[] {MarshallerFactory.newIdentityMarshallingStrategy()});
		File file = new File("ksession.info");
		FileOutputStream foStream; 
		
			foStream = new FileOutputStream(file);
			marshaller.marshall(baos, ksession);
			baos.writeTo(foStream);
			baos.close();
			log.info("Session with "+ ksession.getFactCount() + " facts stored.");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
		
	}
	
	public void restoreSession(){
		Marshaller marshaller = MarshallerFactory.newMarshaller(kbase);
		try {
			FileInputStream fiStream = new FileInputStream("ksession.info");
			ksession = marshaller.unmarshall(fiStream);
			log.info("Session with "+ ksession.getFactCount() + " facts restored.");
		} catch (FileNotFoundException e) {
			log.error("ksession.info was not found. Session was not restored.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addEntity(Entity e) {
		// TODO Auto-generated method stub
		ksession.insert(e);
		log.info("Entity added - Current FactCount: "+ ksession.getFactCount());
	}

	@Override
	public void removeEntity(Entity e) {
		try{
		if(ksession.getFactHandle(e) != null){
		ksession.retract(ksession.getFactHandle(e));
		if (ksession.getFactHandle(e)== null){
			log.info("Entity removed.");
		}else{
			log.error("Unable to remove Entity "+e.toString());	
		}
		}
		else{
			log.error("Unable to remove Entity "+e.toString());
		}
		}catch(NullPointerException exception){
			exception.printStackTrace();
		}
	}

	@Override
	public void updateEntity(Entity oldEntity, Entity newEntity) {
		// TODO Auto-generated method stub
	log.info("Update Process");
	log.info("old: " + oldEntity.toString());
	log.info("new: " + newEntity.toString());
	FactHandle oldHandle = ksession.getFactHandle(oldEntity);
	if(oldHandle != null){
		ksession.update(ksession.getFactHandle(oldEntity), newEntity);
		log.info("Update done.");
	}else{
		log.info("Update not done. Old entity "+oldEntity.toString() +"not found in session.");
	}
//		ksession.insert(newEntity);
	}
	
//	@Override
	public void updateEntityByProperty(String propertyKey, String propertyValue, Entity newEntity) {
		// TODO Auto-generated method stub
	log.info("Update Process");
	log.info("New Entity: "+newEntity + " Key: "+propertyKey + " Value: "+propertyValue);
	ArrayList<Entity> list = new ArrayList<Entity>();
	Object[] arguments = new Object[2];
	arguments[0] = propertyKey;
	arguments[1] = propertyValue;
	QueryResults results=ksession.getQueryResults( "GetEntityByPropertyAndValue", arguments );
	
	if (results.size() == 1){
//		for ( QueryResultsRow row : results ) {
		Entity oldEntity = (Entity)results.iterator().next().get("r");
//		  =  row.getFactHandle( "r" );
//		}
		if (oldEntity != null){
			log.info(ksession.getFactHandle(oldEntity).toString());
			try{
		ksession.update(ksession.getFactHandle(oldEntity), newEntity);
			}catch(NullPointerException e){
				e.printStackTrace();;
//				ksession.update(ksession.getFactHandle(old), newEntity);
			}
			
		ksession.fireAllRules();
		}
		else {
			log.error("Unable to retrieve FactHandle from session.");
		}
	}else{
		log.error("Found "+list.size()+" entities with this key/value. Please choose an existing unique pair.");
	}
	}

	@Override
	public void applyRules() {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(cfg);
		for(de.lehsten.casa.contextserver.types.Rule r : rules){
			if (!r.getIsActive()){
				kbase.removeRule(r.getPackageName(), r.getName());
			}
		}
		int ruleCount =  ksession.fireAllRules();
		for(de.lehsten.casa.contextserver.types.Rule r : rules){
			if (!r.getIsActive()){
				kbuilder.add(ResourceFactory.newReaderResource(new StringReader(r.getRule())), ResourceType.DRL);
			}
		}
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		log.info(ruleCount+" Rules fired");
	}

	@Override
	public long getFactCount() {
		// TODO Auto-generated method stub
		return ksession.getFactCount();
	}

	@Override
	public void applyRule(String rule) {
		if(getRuleNames().contains(rule)){
		ksession.fireAllRules( new RuleNameEqualsAgendaFilter(rule));
		}
	}
	
	public void applyRule(de.lehsten.casa.contextserver.types.Rule rule) {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(cfg);
		kbuilder.add(ResourceFactory.newReaderResource(new StringReader(rule.getRule())), ResourceType.DRL);
		if (!(kbuilder.getErrors().size() > 0)){
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());	
		ksession.fireAllRules( new RuleNameEqualsAgendaFilter(rule.getName()));
		kbase.removeRule(rule.getPackageName(), rule.getName());
		}
		
	}

	@Override
	public Collection<String> getRuleNames() {
		List<String>  rulesList = new ArrayList<String>(); 
		for (KnowledgePackage kp : kbase.getKnowledgePackages()){
			for (Rule r : kp.getRules()){
			rulesList.add(r.getName());
			}
		}
		return rulesList;
	}
	

	public Collection<de.lehsten.casa.contextserver.types.Rule> getRules() {
		return rules;
	}
	

	@Override
	public Collection<String> getQueryNames() {
		List<String>  rulesList = new ArrayList<String>(); 
		for (KnowledgePackage kp : kbase.getKnowledgePackages()){
			for (Rule r : kp.getRules())
			rulesList.add(r.getName());
		}

		return rulesList;
	}

	@Override
	public Collection<Entity> getQueryResult(String queryName, Object[] arguments) {
		ArrayList<Entity> list = new ArrayList<Entity>();
		if (this.getQueryNames().contains(queryName)){
			log.info("Query: "+queryName);
			log.info("QueryArguments: "+arguments);
			if (arguments == null){
				arguments = new Object[0];
			}
			for (Object o : arguments){
				log.info("QueryArgument: "+o);
			}
			QueryResults results= ksession.getQueryResults( queryName, arguments );
		
			for ( QueryResultsRow row : results ) {
				Entity e = ( Entity) row.get( "r" );
				list.add(e);
			}
			log.debug(list.size() +" entities found.");
			return list;
		}
		else {
			log.info("Unknown query: "+queryName);
			log.info("Known queries: "+getQueryNames());
			return list;
		}
	}

	@Override
	public void addRule(de.lehsten.casa.contextserver.types.Rule rule) {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(cfg);
		kbuilder.add(ResourceFactory.newReaderResource(new StringReader(rule.getRule())), ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		else kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		rules.add(rule);
	}

	@Override
	public void removeRule(de.lehsten.casa.contextserver.types.Rule rule) {
		if (kbase.getRule(
				rule.getPackageName(), 
				rule.getName()) 
				!= null){
		kbase.removeRule(rule.getPackageName(), rule.getName());
		}
		rules.remove(rule);
	}
	

	
	public void updateRule(de.lehsten.casa.contextserver.types.Rule oldRule, de.lehsten.casa.contextserver.types.Rule newRule) {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(cfg);
		kbuilder.add(ResourceFactory.newReaderResource(new StringReader(newRule.getRule())), ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (!(errors.size() > 0)) {
			if ((kbase.getRule(oldRule.getPackageName(), oldRule.getName()) != null) && rules.contains(oldRule) )
			{
			kbase.removeRule(oldRule.getPackageName(), oldRule.getName());
			rules.remove(oldRule);
			kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
			rules.add(newRule);
			}
			else throw new IllegalArgumentException("Specified rule is not present - try add as new rule.");
		}
		else for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
	}

	
	public boolean activateRule(de.lehsten.casa.contextserver.types.Rule rule){
		if (rules.contains(rule)){
			rules.get(rules.indexOf(rule)).setIsActive(true);
			return true;
		}
		else return false;
	}	
	
	public boolean deactivateRule(de.lehsten.casa.contextserver.types.Rule rule){
		if (rules.contains(rule)){
			rules.get(rules.indexOf(rule)).setIsActive(false);
			return true;
		}
		else return false;
	}
	
	public void readEntityFromXML(){
		
	}
}
