package de.lehsten.casa.contextserver.types;

import java.util.HashMap;

public class QueryRequest  extends Request {
	
	HashMap<String,Object[]> query = new HashMap<String,Object[]>();

	public HashMap<String, Object[]> getQuery() {
		return query;
	}

	public void setQuery(HashMap<String, Object[]> query) {
		this.query = query;
	}

}
