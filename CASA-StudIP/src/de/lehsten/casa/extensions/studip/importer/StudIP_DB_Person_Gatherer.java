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
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.person.Person;
import de.lehsten.casa.contextserver.types.entities.person.identity.Identity;
import de.lehsten.casa.contextserver.types.entities.person.identity.StudIPIdentity;

public class StudIP_DB_Person_Gatherer {
	
	String host;
	String db_name;
	int db_port;
	String user;
	String passwd;
	
	public StudIP_DB_Person_Gatherer(String host, int db_port, String name, String user, String passwd){
		this.host = host;
		this.db_port = db_port;
		this.db_name = name;
		this.user = user;
		this.passwd = passwd;
	}
	
public List<Entity> getAllUsers(){
		
		Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    ArrayList<Entity> list = new ArrayList<Entity>();
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
	        // get ressorce_id
	                rs = stmt.executeQuery("SELECT * FROM auth_user_md5");

	                /* The following 3 lines are for finding out the size of the result set */
	                rs.last();
	                int rowCount = rs.getRow();
	                rs.beforeFirst();

	                System.out.println("Retrieved " + rowCount + " row(s) for SELECT * FROM auth_user_md5.\n");


	                /* Retrieve the data from the result set */
	                while (rs.next()) {
	                	Identity e = new Identity();
	                	int i;
	                	for(i=1; i<=7;i++){
	                	e.getProperties().put(rs.getMetaData().getColumnLabel(i), rs.getString(i));
	                	};
	                	StudIPIdentity id = new StudIPIdentity(e.getProperties().get("user_id"));
	                	id.setIdentityDomain("StudIP");
	                	id.setIdentityUserName(e.getProperties().get("username"));
	                	id.setFirstname(e.getProperties().get("Vorname"));
	                	id.setLastname(e.getProperties().get("Nachname"));
	                	id.setEmail(e.getProperties().get("Email"));
	                	id.getRights().add(e.getProperties().get("perms"));
	                	id.setProperties(e.getProperties());
	                	id.setSource("Stud.IP");
	                 	list.add(id);
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
	    return list;
		
	};

}

