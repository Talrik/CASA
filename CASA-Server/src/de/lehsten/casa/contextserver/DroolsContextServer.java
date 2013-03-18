package de.lehsten.casa.contextserver;

import org.drools.runtime.StatefulKnowledgeSession;

public interface DroolsContextServer extends ContextServer{

	public StatefulKnowledgeSession getSession();

}
