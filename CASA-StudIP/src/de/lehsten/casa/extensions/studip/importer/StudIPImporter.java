package de.lehsten.casa.extensions.studip.importer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.lehsten.casa.utilities.communication.CASAMessenger;
import de.lehsten.casa.contextserver.interfaces.Importer;
import de.lehsten.casa.contextserver.interfaces.ImporterManager;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.event.Event;


public class StudIPImporter implements Importer{
	
	private ImporterManager mgr;
	private CASAMessenger messenger;
	private String title;
	private String requiredVersionOfCASATypes;
	private String developer;
	private String description;
	
	public StudIPImporter( ImporterManager mgr){ 
		this.mgr = mgr;
		messenger = (CASAMessenger) mgr.getMessenger("StudIPImporter");
		this.title = "StudIPImporter"; 
		this.requiredVersionOfCASATypes = "0.1.5";
		this.developer = "phil";
		this.description = "Importer for StudIP";
	}
	
	@Override
	public boolean startImport(ImporterManager mgr,
			HashMap<String, Object> properties) {
		if (messenger==null){
			System.out.println("No Messenger");
			return false;
		}else{
		String host = (String) properties.get("host");
		String dbName = (String) properties.get("dbName");
		String dbUser = (String) properties.get("dbUser");
		String dbPass = (String) properties.get("dbPass"); 

		this.sendPersons(host, 3306, dbName, dbUser,dbPass);
		this.sendInventory(host, 3306, dbName, dbUser,dbPass);
		this.sendEvents(host, 3306, dbName, dbUser,dbPass);

		
		System.out.println("StudIPImporter: Gathering done.");
		return true;
		}
	}
	

	@Override
	public boolean startImport(ImporterManager mgr, Object... parameter) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean startImport(ImporterManager mgr) {
		
		if (messenger==null){
			System.out.println("No Messenger");
		}else{
		String host = "localhost";
		String dbName = "studip23";
		String dbUser = "studip";
		String dbPass = "studippw"; 

		sendPersons(host, 3306, dbName, dbUser,dbPass);
		sendInventory(host, 3306, dbName, dbUser,dbPass);
		sendEvents(host, 3306, dbName, dbUser,dbPass);

		}
		System.out.println("StudIPImporter: Gathering done.");
		return true;
	}

	@Override
	public boolean stopImport() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateImports(ImporterManager mgr) {
		// TODO Auto-generated method stub
		return false;
	}

	private void sendEvents(String host, int port,String dbName,String dbUser,String dbPass){
		System.out.println("Sending Events");
		StudIP_DB_Event_Gatherer g4 = new StudIP_DB_Event_Gatherer(host, port, dbName, dbUser, dbPass);
		List<Event> event_list = g4.getAllEvents(); 
		Event[] events = new Event[event_list.size()];
		event_list.toArray( events );
		for(Event e : events){
			e.setSource("Stud.IP");
			messenger.send(e);
		}
		
	}
	
	private void sendPersons(String host, int port,String dbName,String dbUser,String dbPass){

		StudIP_DB_Person_Gatherer g1 = new StudIP_DB_Person_Gatherer(host, port, dbName, dbUser, dbPass);
		List<Entity> person_list = g1.getAllUsers();
		Entity[] persons = new Entity[person_list.size()];
		person_list.toArray( persons );
		for(Entity p : persons){
			p.setSource("Stud.IP");
			messenger.send(p);
		}

	}
	
	private void sendInventory(String host, int port,String dbName,String dbUser,String dbPass){

		StudIP_DB_Inventory_Gatherer g3 = new StudIP_DB_Inventory_Gatherer(host, port, dbName, dbUser, dbPass);
		ArrayList<Entity> inventory_list = g3.getAllResources();
		Entity[] entities = new Entity[inventory_list.size()];
		inventory_list.toArray( entities );
		for(Entity e : entities){
			e.setSource("Stud.IP");
			messenger.send(e); 
		}

	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDeveloper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequiredVersionOfCASATypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}



}
