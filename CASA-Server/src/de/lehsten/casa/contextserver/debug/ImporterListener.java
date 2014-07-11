package de.lehsten.casa.contextserver.debug;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lehsten.casa.contextserver.CASAContextServer;
import de.lehsten.casa.contextserver.interfaces.ImporterManager;

public class ImporterListener implements ServiceListener{

	JmDNS jmdns;
	private final static Logger log = LoggerFactory.getLogger( ImporterListener.class ); 
	
    public ImporterListener(CASAContextServer casaContextServer) {
    	
    	 // Activate these lines to see log messages of JmDNS
    	
        boolean logg = true;
        if (logg) {
        	java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JmDNS.class.getName());
            ConsoleHandler handler = new ConsoleHandler();
            logger.addHandler(handler);
            logger.setLevel(Level.FINER);
            handler.setLevel(Level.FINER);
        }
    	/*
        try {
        	
        	jmdns = JmDNS.create();
        	log.info("Host: " + jmdns.getHostName() );
            log.info("Interface: " + jmdns.getInterface() );
            String type = "_casa._tcp.local.";
           jmdns.addServiceListener(type, new SampleListener());

        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
    
	@PreDestroy
	private void stopImporterListener(){
		try {
			jmdns.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Override
    public void serviceAdded(ServiceEvent event) {
        log.info("Service added   : " + event.getName() + "." + event.getType());
        event.getDNS().getServiceInfo(event.getType(), event.getName()).toString();
        		 
    }

    @Override
    public void serviceRemoved(ServiceEvent event) {
    	log.info("Service removed : " + event.getName() + "." + event.getType());
    }

    @Override
    public void serviceResolved(ServiceEvent event) {
    	log.info("Service resolved: " + event.getInfo());
    }
}