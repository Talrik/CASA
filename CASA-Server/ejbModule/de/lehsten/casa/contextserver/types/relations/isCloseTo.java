package de.lehsten.casa.contextserver.types.relations;

import de.lehsten.casa.contextserver.types.entities.person.Person;
import de.lehsten.casa.contextserver.types.entities.place.Place;

public class isCloseTo {
	
	Person person;
	long distance;
	Place place;
	
	public isCloseTo(Person person, long distance, Place place){
		this.person = person;
		this.distance = distance;
		this.place = place;
	}

}
