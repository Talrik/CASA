package de.lehsten.casa.contextserver;

import java.util.ArrayList;
import java.util.List;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.LiveQuery;
import org.drools.runtime.rule.Row;
import org.drools.runtime.rule.ViewChangedEventListener;

import de.lehsten.casa.contextserver.types.Entity;

public class UserAgent {
	StatefulKnowledgeSession ksession;
	final List<Object> updated = new ArrayList<Object>();
	final List<Object> removed = new ArrayList<Object>();
	final List<Object> added = new ArrayList<Object>();
	List<Entity> result = new ArrayList<Entity>();
	LiveQuery query;
	ViewChangedEventListener listener;
	CASAContextServer cs;

	public UserAgent(CASAContextServer cs)
	{
		this.cs = cs;
		this.ksession = cs.getSession();
		this.listener = new myListener();
		
	}

	public boolean setQuery(String queryString){
		if (cs.getQueryNames().contains(queryString)){
		try{
		query = ksession.openLiveQuery( queryString,
				new Object[] {},
				listener );
		return true;
		}
		catch(Exception e){
			return false;
		}}
		else {
			System.out.println("UserAgent: Unknown query "+queryString);
			return false;
		}
	}

	private class myListener implements ViewChangedEventListener{

		public void rowUpdated(Row row) {
			updated.add(row.get("r"));
			System.out.println("[UserAgent] Updated: "+ row.get( "r" ) );
		}

		public void rowRemoved(Row row) {
			removed.add(row.get("r"));
			result.remove(row.get("r"));
			System.out.println("[UserAgent] Removed: "+ row.get( "r" ) );
		}

		public void rowAdded(Row row) {
			added.add( row.get( "r" ) );
			result.add((Entity) row.get( "r" ));
			System.out.println("[UserAgent] Added: "+ row.get( "r" ) );

		}
		}

	public List<Entity> getResult(){
		return result;
	}
	

}
