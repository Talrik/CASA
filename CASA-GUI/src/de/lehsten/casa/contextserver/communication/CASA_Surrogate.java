package de.lehsten.casa.contextserver.communication;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PreDestroy;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;

import de.lehsten.casa.contextserver.ContextServer;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.Rule;
import de.lehsten.casa.contextserver.types.entities.event.Event;
import de.lehsten.casa.contextserver.types.entities.event.Lecture;
import de.lehsten.casa.contextserver.types.entities.person.Person;
import de.lehsten.casa.contextserver.types.xml.CSMessage;
import de.lehsten.casa.utilities.communication.CamelMessenger;
import de.lehsten.casa.utilities.interfaces.Messenger;

public class CASA_Surrogate implements ContextServer{

	String ContextServerIP;
	String layer;
	int ContextServerPort;
	InitialContext ctx;
	CamelContext camelContext;
	ProducerTemplate serverProducer;
	ProducerTemplate importerProducer;

	public CASA_Surrogate(){
		init();
	}

	private void init(){		
		try {
			this.layer = "GUI";
			ctx = new InitialContext();
			new InitialContext().rebind("CASA_Surrogate", this);
			startContext();
			
			importerProducer.setDefaultEndpoint(camelContext.getEndpoint("direct:Importer"));
			serverProducer.setDefaultEndpoint(camelContext.getEndpoint("direct:CASA_Server"));
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		serverMessenger = new CamelMessenger("GUI", "CASA_Surrogate_Server_Messenger", "vm:ServerControl");
//		importerMessenger = new CamelMessenger("GUI", "CASA_Surrogate_Importer_Messenger", "vm:ImporterControl");
		
	
	}
	
	public ArrayList<Rule> getRules(){
		ArrayList<Rule> rulesList = new ArrayList<Rule>();
		try 
		{
			CSMessage rulesMsg = (CSMessage) serverRequest("getRules");
			ArrayList<Object> objectList = rulesMsg.payload;
			for (Object o : objectList){
				if (o instanceof Rule){
					rulesList.add((Rule)o);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return rulesList;
	}	

	public ArrayList<Entity> getEntities() throws CamelExecutionException{
		
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		try 
		{
			CSMessage entityMsg = (CSMessage) serverRequest("getQueryResult","GetAllEntities", null);
			ArrayList<Object> objectList = entityMsg.payload;
			for (Object o : objectList){
				if (o instanceof Entity){
					entityList.add((Entity)o);
				}
			}
		}catch(CamelExecutionException e){ 
			throw e;
		}
		catch(Exception e){
			e.printStackTrace();
		}return entityList ;

	}

	public ArrayList<String> getImporter() throws CamelExecutionException{
		ArrayList<String> importerList = new ArrayList<String>();
		try{
			CSMessage importerMsg = (CSMessage) importerRequest("getImporter", null);			
			ArrayList<Object> importerNames = importerMsg.payload;		
			for (Object o : importerNames){
				importerList.add((String)o);
			}
			System.out.println("[CASA_Surrogate]: response = " + importerList.size() + " Importer found");
			
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("[CASA_Surrogate]: getImporter Exception " + e.getMessage());
		}
		return importerList;
	}
	
	public boolean startImporter(String importerName){
		try{
			CSMessage importerMsg = (CSMessage) importerRequest("startImporter", importerName);;
			System.out.println("[CASA_Surrogate]: response =  Importer "+importerName+" started " +importerMsg.text);
			return importerMsg.text.contains("true");
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("[CASA_Surrogate]: startImporter Exception " + e.getMessage());
		}
		return false;
	}


	@Override
	public void addEntity(Entity arg0) {
		try 
		{
			serverCommand("addEntity", arg0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}



	@Override
	public void addRule(Rule arg0) {
		try 
		{
			serverCommand("addRule", arg0);
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public void applyRule(Rule arg0) {
		try 
		{
			serverCommand("applyRule", arg0);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	@Override
	public void applyRule(String arg0) {
		try 
		{
			serverCommand("applyRule", arg0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void updateRule(Rule arg0, Rule arg1) {
		try 
		{
			serverCommand("updateRule", arg0, arg1);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void applyRules() {
		try 
		{
			serverCommand("applyRules");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public long getFactCount() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public Collection<String> getQueryNames() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Collection<Entity> getQueryResult(String query, Object[] params) {
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		try 
		{	
			CSMessage entityMsg = (CSMessage) serverRequest("getQueryResult",query,params );;
			ArrayList<Object> objectList = entityMsg.payload;
			for (Object o : objectList){
				if (o instanceof Entity){
					entityList.add((Entity)o);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}return entityList ;

	}



	@Override
	public Collection<String> getRuleNames() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void removeEntity(Entity arg0) {
		try 
		{
			serverCommand("removeEntity", arg0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}



	@Override
	public void removeRule(Rule arg0) {
		try 
		{
			serverCommand("removeRule", arg0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}



	@Override
	public void startServer() {
		try 
		{
			serverCommand("startServer", null);
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	@Override
	public void stopServer() {
		try 
		{
			serverCommand("stopServer", null);
		}catch(Exception e){
			e.printStackTrace();
		}

	}



	@Override
	public void updateEntity(Entity arg0, Entity arg1) {
		try 
		{
			serverCommand("updateEntity", arg0, arg1);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	@Override
	public void updateEntityByProperty(String arg0, String arg1, Entity arg2) {
		try 
		{
			serverCommand("updateEntityByProperty", arg0, arg1, arg2);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void startContext(){
		try { 
			System.out.println("Lookup context...");
			ctx = new InitialContext();
			this.camelContext = (CamelContext) ctx.lookup(layer+"Context");
			System.out.println("Context found: "+ this.camelContext);
		} catch (NamingException e1 ) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
		}
		if (this.camelContext == null){
			System.out.println("No Context found - defining new context..."); 
			DefaultCamelContext camelContext = new DefaultCamelContext();
			System.out.println("Starting context...");
			try {
				//		camelContext.setRegistry(createRegistry());
//				camelContext.addRoutes(this);
				camelContext.start();

				System.out.println("Context started...");
				System.out.println("CamelContextID: "+camelContext.getName());
				ctx.rebind(layer+"Context", camelContext);
				System.out.println(ctx.lookup(layer+"Context"));
				this.camelContext = (CamelContext) ctx.lookup(layer+"Context");
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}	
		}
		
		serverProducer = this.camelContext.createProducerTemplate();
		importerProducer = this.camelContext.createProducerTemplate();
			
	}

	public boolean stopImporter(String importerName) {
		try{
			CSMessage importerMsg = (CSMessage) importerRequest("stopImporter", importerName);			
			System.out.println("[CASA_Surrogate]: response =  Importer "+importerName+" started " +importerMsg.text);
			return importerMsg.text.contains("true");
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("[CASA_Surrogate]: startImporter Exception " + e.getMessage());
		}
		return false;
		
	}

	@Override
	public void storeSession()  throws CamelExecutionException{
		try 
		{
			serverCommand("storeSession",null);
		}catch(CamelExecutionException e){
			throw e;
		}

	}


	@Override
	public void restoreSession() throws CamelExecutionException{
		try 
		{
			serverCommand("restoreSession",null);
		}catch(CamelExecutionException e){
			throw e;
		}

	}
	
	private void serverCommand(String text, Object... payload) throws CamelExecutionException{
		try{
		CSMessage msg = new CSMessage();
		msg.text = text;
		if (payload != null){
			for (Object o : payload){
			msg.payload.add(o);
			}
		}
		serverProducer.sendBody(msg);
		}catch(CamelExecutionException e){
			throw e;
		}
	}
	
	private void importerCommand(String text, Object... payload) throws CamelExecutionException{
		try{
		CSMessage msg = new CSMessage();
		msg.text = text;
		if (payload != null){
			for (Object o : payload){
			msg.payload.add(o);
			}
		}
		importerProducer.sendBody(msg);
	}catch(CamelExecutionException e){
		throw e;
	}
	}

	private Object serverRequest(String text, Object... payload) throws CamelExecutionException{
		try{
			CSMessage msg = new CSMessage();
			msg.text = text;
			if (payload != null){
				for (Object o : payload){
				msg.payload.add(o);
				}
			}
			return serverProducer.requestBody(msg);
		}catch(CamelExecutionException e){
			throw e;
		}
	}

	private Object importerRequest(String text, Object... payload) throws CamelExecutionException{
		try{
			CSMessage msg = new CSMessage();
			msg.text = text;
			if (payload != null){
				for (Object o : payload){
				msg.payload.add(o);
				}
			}
			return importerProducer.requestBody(msg);

		}catch(CamelExecutionException e){
			throw e;
		}
	}

}
