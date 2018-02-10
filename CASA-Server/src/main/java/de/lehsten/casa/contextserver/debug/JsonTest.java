package de.lehsten.casa.contextserver.debug;

import java.util.ArrayList;

import com.google.gson.Gson;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.Request;
import de.lehsten.casa.contextserver.types.StudIPRequest;
import de.lehsten.casa.contextserver.types.entities.event.Event;
import de.lehsten.casa.contextserver.types.entities.person.identity.Identity;
import de.lehsten.casa.contextserver.types.entities.person.identity.StudIPIdentity;
import de.lehsten.casa.contextserver.types.entities.place.Place;

public class JsonTest {

	public static void main(String[] args) {
		Request request	= new Request();
		Event reqLecture = new Event();
		reqLecture.setTitle("Veranstaltung");
		StudIPIdentity reqIdentity = new StudIPIdentity();
		reqIdentity.setStudip_role("dozent");
		Place reqPlace = new Place();
		reqPlace.setTitle("SmartLab");
		ArrayList<Entity> restrictions = new ArrayList<Entity>();
		request.setRestrictions(restrictions);
		StudIPRequest studRequest = new StudIPRequest(request);
		studRequest.setRequestId("4711");
		System.out.println("StudIPRequest "+studRequest.getRequestId()+" contains "+studRequest.getLocations().size()+" locations, "+studRequest.getLectures().size()+" events, and "+studRequest.getRoles().size()+" roles");
		ArrayList<Identity> roles = new ArrayList<Identity>();
		roles.add(reqIdentity);
		studRequest.setRoles(roles);
		ArrayList<Place> locations = new ArrayList<Place>();
		locations.add(reqPlace);
		studRequest.setLocations(locations);;
		ArrayList<Event> lectures = new ArrayList<Event>();
		lectures.add(reqLecture);
		studRequest.setLectures(lectures);
		System.out.println("StudIPRequest "+studRequest.getRequestId()+" contains "+studRequest.getLocations().size()+" locations, "+studRequest.getLectures().size()+" events, and "+studRequest.getRoles().size()+" roles");
		Gson gson = new Gson();
		String gsonString = gson.toJson(studRequest);
		System.out.println(gsonString); // May not serialize foo.value correctly
		StudIPRequest studRequest2 = gson.fromJson(gsonString, StudIPRequest.class);
		System.out.println(studRequest2.getLectures().get(0).getTitle());

	}

}
