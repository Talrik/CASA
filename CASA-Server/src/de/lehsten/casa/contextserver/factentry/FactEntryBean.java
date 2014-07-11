package de.lehsten.casa.contextserver.factentry;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lehsten.casa.contextserver.CASAContextServer;
import de.lehsten.casa.contextserver.interfaces.ContextServer;
import de.lehsten.casa.contextserver.types.Entity;


public class FactEntryBean implements Processor{

	private ContextServer server; 
	private final static Logger log = LoggerFactory.getLogger( FactEntryBean.class ); 
	   

	public FactEntryBean(){
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			server = (ContextServer) ctx.lookup("java:global/CASA-Server/CASAContextServer");
		} catch (NamingException e2) { 
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}    	

	}


	@Override
	public void process(Exchange exchange) throws Exception {
		log.debug("Msg received by FEB "+ exchange + " from "+ exchange.getFromEndpoint());
		log.debug("FEB: "+exchange.getIn().getBody() );
		Object e = exchange.getIn().getBody();
		if (server == null) {System.err.println("[FEB]: No ContextServer available");}		
		else if (e instanceof Entity){
			Entity ent = (Entity)e;
			server.addEntity(ent);
			log.debug("FEB: "+ent.getClass() +" : "+ent.getProperties() );
			
		}
	}
	

}
