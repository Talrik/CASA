package de.lehsten.casa.contextserver.gui.explorer.entities;

import java.io.Serializable;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.CamelExchangeException;

import com.vaadin.data.util.BeanItemContainer;

import de.lehsten.casa.contextserver.communication.CASA_Surrogate;
import de.lehsten.casa.contextserver.types.Entity;

public class EntityContainer extends BeanItemContainer<Entity> implements
		Serializable {

	private static final long serialVersionUID = 1L;
	public static final Object[] NATURAL_COL_ORDER = new Object[] {
        "properties", "source"};
public static final String[] COL_HEADERS_ENGLISH = new String[] {
        "Properties", "Source" };
	public EntityContainer() throws InstantiationException,
			IllegalAccessException {
		super(Entity.class);
	}
	
	public static EntityContainer createWithCASAData() throws CamelExchangeException{
		EntityContainer ec = null;
		try {
			ec = new EntityContainer();
			CASA_Surrogate ci;
			try {
				ci = (CASA_Surrogate) new InitialContext().lookup("CASA_Surrogate");
				ec.addAll(ci.getEntities());
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
		return ec;	  
	}


	public static EntityContainer createWithData() {
		EntityContainer ec = null;
		try {
			ec = new EntityContainer();

			Entity e1 = new Entity();
			e1.setSource("Test Entity Source 1");
			ec.addItem(e1);

			Entity e2 = new Entity();
			e2.setSource("Test Entity Source 2");
			ec.addItem(e2);
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ec;
	}
}
