package de.lehsten.casa.contextserver.types.entities.event;

import java.util.ArrayList;
import java.util.Date;

import de.lehsten.casa.contextserver.types.entities.person.Person;
import de.lehsten.casa.contextserver.types.entities.place.Place;

public class Course extends StudIPEvent{
	
	ArrayList<Person> members = new ArrayList<Person>();
	ArrayList<String> membersID = new ArrayList<String>();
	ArrayList<Lecture> lectures = new ArrayList<Lecture>();
	ArrayList<Tutorial> tutorials = new ArrayList<Tutorial>();
	ArrayList<Meeting> meetings = new ArrayList<Meeting>();
	ArrayList<Place> locations = new ArrayList<Place>();
	
	public Course(){
		super();
	}
	
	public boolean isSuper(){
		return true;
	}
	
	
	public Course(String title){
		super.setTitle(title);
	}
	
	public String toString(){
		String ausgabe = null;
		
		ausgabe = 	"Titel: " + this.getTitle() +"\n" ;
	/*	
		if(this.getStudIP_ID() != null) ausgabe = ausgabe +	"StudIP ID: " + this.getStudIP_ID() + "\n"; 
		if(this.getBegin() != null) ausgabe = ausgabe +	"Beginn: " + new Date(this.getBegin()*1000) + "\n" ;
		if(this.getLectures() != null) ausgabe = ausgabe +	"Anz. Vorlesungen: " + this.lectures.size() + "\n" ;
	*/				
		
		return ausgabe;
	}
	
	public ArrayList<Person> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<Person> members) {
		this.members = members;
	}
	
	public void addMember(Person member) {
		this.members.add(member);
	}
	
	public ArrayList<Lecture> getLectures() {
		return lectures;
	}

	public void setLectures(ArrayList<Lecture> lectures) {
		this.lectures = lectures;
	}
	
	public void addLecture(Lecture lecture) {
		this.lectures.add(lecture);
	}

	public ArrayList<Tutorial> getTutorials() {
		return tutorials;
	}

	public void setTutorials(ArrayList<Tutorial> tutorials) {
		this.tutorials = tutorials;
	}

	public ArrayList<Meeting> getMeetings() {
		return meetings;
	}

	public void setMeetings(ArrayList<Meeting> meetings) {
		this.meetings = meetings;
	}

	public ArrayList<String> getMembersID() {
		return membersID;
	}

	public void setMembersID(ArrayList<String> membersID) {
		this.membersID = membersID;
	}
	public void addMembersID(String membersID) {
		this.membersID.add(membersID);
	}

}
