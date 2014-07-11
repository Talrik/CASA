package de.lehsten.casa.contextserver.interfaces;

import java.util.Collection;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.Rule;

public interface ContextServer {
	
	// server operations
	public void startServer();
	public void stopServer();
	public void storeSession();
	public void restoreSession();
	// entity handling
	public void addEntity(Entity e);
	public void removeEntity(Entity e);
	public void updateEntity(Entity oldEntity, Entity newEntity);
	public void updateEntityByProperty(String propertyKey, String propertyValue, Entity newEntity);
	// rule and query execution
	public void applyRules();
	public void applyRule(String rule);
	public void applyRule(Rule rule);	
	public Collection<Entity> getQueryResult(String queryName, Object[] arguments);
	// rule and query handling
	public void addRule(Rule rule);
	public void removeRule(Rule rule);
	public void updateRule(Rule oldRule, Rule newRule);
	// status information
	public long getFactCount();	
	public Collection<Rule> getRules();
	public Collection<String> getRuleNames();
	public Collection<String> getQueryNames();
}
