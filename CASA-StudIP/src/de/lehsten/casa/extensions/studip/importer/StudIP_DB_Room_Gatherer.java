/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lehsten.casa.extensions.studip.importer;

/**
 *
 * @author phil
 */
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import de.lehsten.casa.contextserver.types.entities.person.Person;
import de.lehsten.casa.contextserver.types.entities.place.rooms.Room;

public class StudIP_DB_Room_Gatherer {
	
		static HashMap<String,Person> person_hashlist = new HashMap<String,Person>();
		String host;
		String db_name;
		int db_port;
		String user;
		String passwd;
		
		public StudIP_DB_Room_Gatherer(String host, int db_port, String name, String user, String passwd){
			this.host = host;
			this.db_port = db_port;
			this.db_name = name;
			this.user = user;
			this.passwd = passwd;
		}
		
		public ArrayList<Room> getAllRooms(){
			
			Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
		    ArrayList<Room> list = new ArrayList<Room>();
		    ArrayList<String> cat_list = new ArrayList<String>();
		    HashMap<String,Room> room_list = new HashMap<String,Room>();
		    HashMap<String,String> prop_list = new HashMap<String,String>();
		    try {

		        /*  Register the driver
			The following Class.forName() statement is not necessary if using Java SE 6 (1.6) or later */

		        Class.forName("com.mysql.jdbc.Driver").newInstance();

		        try {

			/* Get a connection to the database */
		        	conn = (Connection) DriverManager.getConnection("jdbc:mysql://"+host+":"+db_port+"/"+db_name+"?" + "user="+user+"&" + "password="+passwd);
		            try {

		                stmt = (Statement) conn.createStatement();

			    /* Execute the query */
		        // get room categories
		                rs = stmt.executeQuery("SELECT * FROM resources_categories WHERE is_room = '1'");

		                /* The following 3 lines are for finding out the size of the result set */
		                rs.last();
		                int rowCount = rs.getRow();
		                rs.beforeFirst();

		                System.out.println("Retrieved " + rowCount + " row(s).\n");


		                /* Retrieve the data from the result set */
		                while (rs.next()) {
		                	String category_id = rs.getString("category_id");
		                	 System.out.println(category_id+" aus DB ausgelesen");
		                	cat_list.add(category_id);
		                }
		                
		        //get rooms        
		                int i = 0;
		                while (cat_list.size() > i){
		                	String cat = cat_list.get(i);
		                rs = stmt.executeQuery("SELECT * FROM resources_objects WHERE category_id = '"+cat+"'");

		                /* The following 3 lines are for finding out the size of the result set */
		                rs.last();
		                rowCount = rs.getRow();
		                rs.beforeFirst();

		                System.out.println("Retrieved " + rowCount + " row(s).\n");


		                /* Retrieve the data from the result set */
		                while (rs.next()) {
		                	String resource_id = rs.getString("resource_id");
		                	String title = rs.getString("name");
		                	System.out.println(title+" aus resources_objects ausgelesen");
		                	Room new_room = new Room();
		                	new_room.setTitle(title);
		                	new_room.setResource_id(resource_id);
		                	room_list.put(resource_id, new_room);
		          //      	room_list.get(seminar_id).addMember(person_hashlist.get(user_id));
		                	
		                 }
		                i++;
		                }
		          //get property types
		                rs = stmt.executeQuery("SELECT * FROM resources_properties");

		                /* The following 3 lines are for finding out the size of the result set */
		                rs.last();
		                rowCount = rs.getRow();
		                rs.beforeFirst();

		                System.out.println("Retrieved " + rowCount + " row(s).\n");


		                /* Retrieve the data from the result set */
		                while (rs.next()) {
		                	String property_id = rs.getString("property_id");
		                	String name = rs.getString("name");
		       //      	 	System.out.println(name+" aus DB ausgelesen");
		                	prop_list.put(property_id, name);
		                }
		                
		          //get properties      
		                rs = stmt.executeQuery("SELECT * FROM resources_objects_properties");

		                /* The following 3 lines are for finding out the size of the result set */
		                rs.last();
		                rowCount = rs.getRow();
		                rs.beforeFirst();

		                System.out.println("Retrieved " + rowCount + " row(s).\n");


		                /* Retrieve the data from the result set */
		                while (rs.next()) {
		                	String resource_id = rs.getString("resource_id");
		                	String property_id = rs.getString("property_id");
		                	String state = rs.getString("state");
		                	if(room_list.containsKey(resource_id) && !state.isEmpty()){
		             	 	System.out.println(prop_list.get(property_id)+" mit "+ state +" für "+ room_list.get(resource_id).getTitle() +" aus DB ausgelesen");
		                	room_list.get(resource_id).addProperty(prop_list.get(property_id), state);
		                	}
		                }
		                
		            } finally {
		                /* Release the resources */
		                if (rs != null) {
		                    try {
		                        rs.close();
		                    } catch (SQLException sqlEx) {
		                           System.out.println("SQLException: " + sqlEx.getMessage());
		                    }
		                    rs = null;
		                }

		                if (stmt != null) {
		                    try {
		                        stmt.close();
		                    } catch (SQLException sqlEx) {
		                           System.out.println("SQLException: " + sqlEx.getMessage());
		                    }

		                    stmt = null;
		                }

		                if (conn != null) {
		                    try {
		                        conn.close();
		                    } catch (SQLException sqlEx) {
		                           // Ignore
		                    }

		                    conn = null;
		                }

		            }

		        } catch (SQLException ex) {
		            System.out.println("SQLException: " + ex.getMessage());
		            System.out.println("SQLState: " + ex.getSQLState());
		            System.out.println("VendorError: " + ex.getErrorCode());
		        }

		    } catch (Exception ex) {
		        System.out.println("Exception: " + ex.getMessage());
		    }
		    System.out.println("Rooms erfolgreich ausgelesen");
		    Set keys = room_list.keySet();
		    Iterator it = keys.iterator();
		    while (it.hasNext()){
		    	list.add(room_list.get(it.next()));
		    }
		    System.out.println("Liste mit "+list.size()+" Räumen übergeben.");
		    return list;
			
		};

}

