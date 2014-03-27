package de.lehsten.casa.contextserver.gui.rules;

import java.io.Serializable;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.vaadin.data.util.BeanItemContainer;

import de.lehsten.casa.contextserver.communication.CASA_Surrogate;
import de.lehsten.casa.contextserver.gui.explorer.entities.EntityContainer;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.Rule;

public class RulesContainer extends BeanItemContainer<Rule> implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final Object[] BASIC_NATURAL_COL_ORDER = new Object[] {
		"name", "packageName", "isActive"};
	public static final Object[] COMPLETE_NATURAL_COL_ORDER = new Object[] {
		"name", "packageName", "isActive", "rule"};
	public static final String[] TABLE_COL_HEADERS_ENGLISH = new String[] {
		"Name", "Package Name", "Active" };
	public static final String[] FORM_COL_HEADERS_ENGLISH = new String[] {
		"Name", "Package Name", "Active", "Rule" };
	
	public RulesContainer() throws InstantiationException,	IllegalAccessException {
		super(Rule.class);
	}

	public static RulesContainer createWithCASAData(){
		RulesContainer rc = null;
		try {
			rc = new RulesContainer();
			CASA_Surrogate ci;
			try {
				ci = (CASA_Surrogate) new InitialContext().lookup("CASA_Surrogate");
				rc.addAll(ci.getRules());
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				System.err.println(e.getMessage());
			}
			
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return rc;	  
	}

}
