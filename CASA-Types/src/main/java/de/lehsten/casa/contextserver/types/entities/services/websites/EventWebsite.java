package de.lehsten.casa.contextserver.types.entities.services.websites;

import javax.xml.bind.annotation.XmlRootElement;

import de.lehsten.casa.contextserver.types.entities.event.Event;

@XmlRootElement
public class EventWebsite extends Website{
	
	private Event event;
	
	public EventWebsite(){
		super();
	}

	public EventWebsite(Event event){
		this.setEvent(event);
	}
	
	public Event getEvent() { 
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
		this.getProperties().put("event", event.getTitle());
	} 

}
