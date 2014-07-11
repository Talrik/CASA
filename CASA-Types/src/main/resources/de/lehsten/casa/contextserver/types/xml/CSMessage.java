package de.lehsten.casa.contextserver.types.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import de.lehsten.casa.contextserver.types.Entity;

public class CSMessage implements Serializable{

	private static final long serialVersionUID = 1L;
	public String sender;
	public String text;
	public HashMap<String,String> parameter = new HashMap<String,String>();
	public ArrayList<Entity> payload = new ArrayList<Entity>(); 

}
