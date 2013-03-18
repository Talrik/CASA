/**
 * 
 */
package de.lehsten.casa.contextserver.factentry;

import java.util.ArrayList;

import java.util.List;

import javax.ejb.Singleton;
import javax.inject.Named;

import org.drools.runtime.ClassObjectFilter;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;

import de.lehsten.casa.contextserver.importer.studip.StudIP_DB_Event_Gatherer;
import de.lehsten.casa.contextserver.importer.studip.StudIP_DB_Inventory_Gatherer;
import de.lehsten.casa.contextserver.importer.studip.StudIP_DB_Person_Gatherer;
import de.lehsten.casa.contextserver.importer.studip.StudIP_DB_Room_Gatherer;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.event.Event;
import de.lehsten.casa.contextserver.types.entities.place.rooms.Room;
@Singleton
@Named("StudIPFactEnrty")
/**
 * @author phil
 *
 */
public class FactEntryStudIP {

	private StatefulKnowledgeSession ksession;
	private StatefulKnowledgeSession StudIPEntryPoint;
//	private WorkingMemoryEntryPoint StudIPEntryPoint;
	
	public FactEntryStudIP(StatefulKnowledgeSession ksession) {
		this.ksession = ksession;
//		StudIPEntryPoint =  ksession.getWorkingMemoryEntryPoint("StudIPEntryPoint"); 
		StudIPEntryPoint =  ksession; 
	}
	
	public void initialGather(){
		if (StudIPEntryPoint==null){
			System.out.println("No EntryPoint");
		}else{
		String host = "localhost";
		String dbUser = "studip";
		String dbName = "studip20";
		String dbPass = "studippw"; 
		
	        StudIP_DB_Person_Gatherer g1 = new StudIP_DB_Person_Gatherer(host, 3306, dbName, dbUser, dbPass);
		List<Entity> person_list = g1.getAllUsers();
		Entity[] persons = new Entity[person_list.size()];
		person_list.toArray( persons );
		for(Entity p : persons){
			p.setSource("Stud.IP");
			StudIPEntryPoint.insert(p);
		}
/*	   
		StudIP_DB_Room_Gatherer g2 = new StudIP_DB_Room_Gatherer("localhost", 3306, "studip20", "studip", "studippw");
		ArrayList<Room> room_list = g2.getAllRooms();
		Room[] rooms = new Room[room_list.size()];
		room_list.toArray( rooms );
		for(Room r : rooms){
			r.setSource("Stud.IP");
			StudIPEntryPoint.insert(r);
		}
*/	    
		StudIP_DB_Inventory_Gatherer g3 = new StudIP_DB_Inventory_Gatherer("localhost", 3306, "studip20", "studip", "studippw");
		ArrayList<Entity> inventory_list = g3.getAllResources();
		Entity[] entities = new Entity[inventory_list.size()];
		inventory_list.toArray( entities );
		for(Entity e : entities){
			e.setSource("Stud.IP");
			StudIPEntryPoint.insert(e); 
		}

		StudIP_DB_Event_Gatherer g4 = new StudIP_DB_Event_Gatherer("localhost", 3306, "studip20", "studip", "studippw");
		List<Event> event_list = g4.getAllEvents(); 
		Event[] events = new Event[event_list.size()];
		event_list.toArray( events );
		for(Event e : events){
			e.setSource("Stud.IP");
			StudIPEntryPoint.insert(e);
		}

		
		}
		System.out.println("FactEntryStudip: Gathering done.");
		
	}
	    
	    

}
