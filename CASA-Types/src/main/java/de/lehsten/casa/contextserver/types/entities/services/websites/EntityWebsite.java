package de.lehsten.casa.contextserver.types.entities.services.websites;

import java.util.ArrayList;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.place.Place;

public class EntityWebsite extends Website {
	
	private ArrayList<Entity> anchors;

	public ArrayList<Entity> getAnchors() {
		return anchors;
	}

	public void setAnchor(Entity anchor) {
		if(!anchors.contains(anchor)){
		this.anchors.add(anchor);
		this.addProperty("anchor", this.getProperties().get("anchor")+" "+anchor.hashCode());
		}
	}
	
	public void setAnchors(ArrayList<Entity> anchors){
		for (Entity e :anchors){
		this.setAnchor(e);
		}
	}
}
