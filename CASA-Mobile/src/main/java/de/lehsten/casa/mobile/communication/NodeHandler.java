package de.lehsten.casa.mobile.communication;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
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

import de.lehsten.casa.contextserver.interfaces.ImporterManager;
import de.lehsten.casa.mobile.data.Node;
import de.lehsten.casa.mobile.gui.CASAUI;

public class NodeHandler implements ServiceListener{

	JmDNS jmdns;
	private final static Logger log = LoggerFactory.getLogger( NodeHandler.class ); 
	private ArrayList<Node> nodes = new ArrayList<Node>();
	
    public NodeHandler(CASAUI casaui) {
    	
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
    
    public ArrayList<Node> getNodes(){
    	return nodes;
    }
    
	@PreDestroy
	private void stopNodeHandler(){
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
    	ServiceInfo eventInfo = event.getInfo();
    	log.info("Service resolved: " + eventInfo.getName());
    	Node newNode = new Node();
    	newNode.setDeveloper(eventInfo.getPropertyString("developer"));
    	newNode.setDescription(eventInfo.getPropertyString("description"));
    	newNode.setTitle(eventInfo.getPropertyString("description"));
    	newNode.setRequiredVersionOfCASATypes(eventInfo.getPropertyString("requiredVersionOfCASATypes"));
    	newNode.setDomainName(eventInfo.getPropertyString("domainName"));
    	newNode.setDomainType(eventInfo.getPropertyString("domainType"));
    	newNode.setEndpoint(eventInfo.getPropertyString("endpointURI"));
    	nodes.add(newNode);
    	log.info("Node "+newNode.toString()+" added to list.");
    	}
    
    public void updateNode(Node oldNode, Node newNode){
    	if(nodes.contains(oldNode)){
    		nodes.remove(oldNode);
    		nodes.add(newNode);
    	}
    }
    	
    }
