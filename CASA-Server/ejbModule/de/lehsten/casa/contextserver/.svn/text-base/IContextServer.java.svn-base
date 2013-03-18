package de.lehsten.casa.contextserver;

import org.drools.runtime.StatefulKnowledgeSession;

import de.lehsten.casa.contextserver.types.Entity;

public interface IContextServer {
	
	public StatefulKnowledgeSession getSession();
	// server operations
	public void startServer();
	public void stopServer();
	// entity handling
	public void addEntity(Entity e);
	public void removeEntity(Entity e);
	public void updateEntity(Entity oldEntity, Entity newEntity);
	// rule execution
	public void applyRules();
	// status information
	public long getFactCount();

}
