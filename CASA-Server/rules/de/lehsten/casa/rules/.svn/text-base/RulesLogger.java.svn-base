package de.lehsten.casa.rules;

import org.drools.spi.KnowledgeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lehsten.casa.contextserver.factentry.FactEntryBean;

public class RulesLogger {
	
private final static Logger log = LoggerFactory.getLogger( FactEntryBean.class ); 

    public static void log(final KnowledgeHelper drools, final String message){
    	
    	log.info("Rule triggered: " + drools.getRule().getName() + " - "+message);
       
//        System.out.println(drools.getActivation().getFactHandles());
    }

}