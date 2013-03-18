/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lehsten.casa.contextserver.importer.studip;

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

import de.lehsten.casa.contextserver.types.entities.event.Course;
import de.lehsten.casa.contextserver.types.entities.event.Event;
import de.lehsten.casa.contextserver.types.entities.event.Lecture;

public class StudIP_DB_Event_Gatherer {
	
	String host;
	String db_name;
	int db_port;
	String user;
	String passwd;
	
	public StudIP_DB_Event_Gatherer(String host, int db_port, String name, String user, String passwd){
		this.host = host;
		this.db_port = db_port;
		this.db_name = name;
		this.user = user;
		this.passwd = passwd;
	}

	public ArrayList<Event> getAllEvents(){
			
			Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
		    ArrayList<Event> list = new ArrayList<Event>();
		    HashMap<String,Course> course_list = new HashMap<String,Course>();
		    HashMap<String,Lecture> lecture_list = new HashMap<String,Lecture>();
		    
		    try {

		        /*  Register the driver
			The following Class.forName() statement is not necessary if using Java SE 6 (1.6) or later */

		        Class.forName("com.mysql.jdbc.Driver").newInstance();

		        try {

			/* Get a connection to the database */
		        	conn = (Connection) DriverManager.getConnection("jdbc:mysql://"+host+":"+db_port+"/"+db_name+"?" + "user="+user+"&" + "password="+passwd);
		            try {

		                stmt = (Statement) conn.createStatement();

			    /* Execute the 1st query */
		        // get events
		                rs = stmt.executeQuery("SELECT * FROM seminare");

		                /* The following 3 lines are for finding out the size of the result set */
		                rs.last();
		                int rowCount = rs.getRow();
		                rs.beforeFirst();

		                System.out.println("\nRetrieved " + rowCount + " row(s)for SELECT * FROM seminare.\n");


		                /* Retrieve the data from the result set */
		                while (rs.next()) {
		                	String seminar_id = rs.getString("Seminar_id");
		                	String title = rs.getString("Name");
		                	Long start_time = rs.getLong("start_time");
		                	 System.out.println(title+" aus DB ausgelesen");
		                	Course c = new Course(title);
		                	c.setBegin(start_time);
		                	c.getProperties().put("start_time", start_time.toString());
		                	c.setStudIP_ID(seminar_id);
		                	c.getProperties().put("Seminar_id", seminar_id);
		                	c.getProperties().put("Name", title);
		                	course_list.put(seminar_id,c);
		                   }
		        
		              //get membernames        
		                rs = stmt.executeQuery("SELECT * FROM auth_user_md5");

		                /* The following 3 lines are for finding out the size of the result set */
		                rs.last();
		                rowCount = rs.getRow();
		                rs.beforeFirst();

		                System.out.println("Retrieved " + rowCount + " row(s) for SELECT * FROM auth_user_md5.\n");

		                HashMap<String,String> person_hashmap = new HashMap<String,String>();
		                /* Retrieve the data from the result set */
		                while (rs.next()) {
		                	String user_id = rs.getString("user_id");
		                	String username = rs.getString("username");
		                	 System.out.println(user_id+" aus seminar_user ausgelesen");
		                	person_hashmap.put(user_id, username); 
		                	
		                 }
		                
		        //get members        
		                rs = stmt.executeQuery("SELECT * FROM seminar_user");

		                /* The following 3 lines are for finding out the size of the result set */
		                rs.last();
		                rowCount = rs.getRow();
		                rs.beforeFirst();

		                System.out.println("Retrieved " + rowCount + " row(s) for SELECT * FROM seminar_user.\n");


		                /* Retrieve the data from the result set */
		                while (rs.next()) {
		                	String seminar_id = rs.getString("Seminar_id");
		                	String user_id = rs.getString("user_id");
		                	String status = rs.getString("status");
		                	 System.out.println(person_hashmap.get(user_id)+" aus seminar_user ausgelesen");
		                	course_list.get(seminar_id).addMembersID(person_hashmap.get(user_id)); 
		                	
		                 }
		          //get dates      
		                rs = stmt.executeQuery("SELECT * FROM termine");

		                /* The following 3 lines are for finding out the size of the result set */
		                rs.last();
		                rowCount = rs.getRow();
		                rs.beforeFirst();

		                System.out.println("Retrieved " + rowCount + " row(s) for SELECT * FROM termine.\n");


		                /* Retrieve the data from the result set */
		                while (rs.next()) {
		                	String seminar_id = rs.getString("range_id");
		                	String termin_id = rs.getString("termin_id");
		                	Long begin = rs.getLong("date");
		                	Long end = rs.getLong("end_time");
		          //      	 System.out.println(name+" aus DB ausgelesen");
		                	Lecture lecture = new Lecture(course_list.get(seminar_id).getTitle(),begin,end, seminar_id);
		                	lecture.getProperties().put("seminar_id", seminar_id);
		                	lecture.getProperties().put("termin_id", termin_id);
		                	lecture.getProperties().put("date", begin.toString());
		                	lecture.getProperties().put("end_time", end.toString());
		                	lecture.setisAssigned(true);
		                	lecture_list.put(termin_id, lecture);
		                	course_list.get(seminar_id).addLecture(lecture);
		             //   	list.add(childevent);  
		                }
		                
		              //get ressources      
		                rs = stmt.executeQuery("SELECT * FROM resources_assign");

		                /* The following 3 lines are for finding out the size of the result set */
		                rs.last();
		                rowCount = rs.getRow();
		                rs.beforeFirst();

		                System.out.println("Retrieved " + rowCount + " row(s) for SELECT * FROM resources_assign.\n");


		                /* Retrieve the data from the result set */
		                while (rs.next()) {
		                	String resource_id = rs.getString("resource_id");
		                	String termin_id = rs.getString("assign_user_id");
		           //      	System.out.println(name+" aus DB ausgelesen");
		                	if(lecture_list.containsKey(termin_id))
		                	lecture_list.get(termin_id).addResource(resource_id);
		             //   	list.add(childevent);
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
		        ex.printStackTrace();
		    }
		    System.out.println("Events erfolgreich ausgelesen");
		    Set keys = course_list.keySet();
		    Iterator it = keys.iterator();
		    while (it.hasNext()){
		    	list.add(course_list.get(it.next()));
		    }
		    Set child_keys = lecture_list.keySet();
		    Iterator child_it = child_keys.iterator();
		    while (child_it.hasNext()){
		    	list.add(lecture_list.get(child_it.next()));
		    }
		    System.out.println("Liste mit "+list.size()+" Events übergeben.");
		    return list;
			
		};

}
