package de.lehsten.casa.contextserver.factentry;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import de.lehsten.casa.contextserver.ContextServer;
import de.lehsten.casa.contextserver.types.Entity;


public class FactEntryBean implements Processor{

	private ContextServer server; 

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
		System.out.println("Msg received by FEB "+ exchange + " from "+ exchange.getFromEndpoint());
		System.out.println("FEB: "+exchange.getIn().getBody() );
		Object e = exchange.getIn().getBody();
		if (server == null) {System.err.println("[FEB]: No ContextServer available");}		
		else if (e instanceof Entity){
			Entity ent = (Entity)e;
			server.addEntity(ent);
			System.out.println("FEB: "+ent.getClass() +" : "+ent.getProperties() );
			
		}
	}
	

}
