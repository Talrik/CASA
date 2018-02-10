package de.lehsten.casa.contextserver;

import org.drools.runtime.StatefulKnowledgeSession;

import de.lehsten.casa.contextserver.interfaces.ContextServer;

public interface DroolsContextServer extends ContextServer{

	public StatefulKnowledgeSession getSession();

}
