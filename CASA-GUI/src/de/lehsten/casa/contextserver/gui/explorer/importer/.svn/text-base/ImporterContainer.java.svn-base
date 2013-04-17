package de.lehsten.casa.contextserver.gui.explorer.importer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;

import de.lehsten.casa.contextserver.communication.CASA_Surrogate;
import de.lehsten.casa.contextserver.gui.importer.ImporterItem;
import de.lehsten.casa.contextserver.importer.interfaces.Importer;

public class ImporterContainer extends BeanItemContainer<ImporterItem> implements
Serializable {
	

	public static final Object[] NATURAL_COL_ORDER = new Object[] {
		"name"};
	 
	public static final String[] COL_HEADERS_ENGLISH = new String[] {
	"Name"};

	public ImporterContainer()
			throws IllegalArgumentException {
		super(ImporterItem.class);
		// TODO Auto-generated constructor stub
	}

	public static ImporterContainer createWithCASAData(){
		System.out.println("Creating ImporterContainer");
		ImporterContainer ic = new ImporterContainer();
		CASA_Surrogate ci;
		try {
			ci = (CASA_Surrogate) new InitialContext().lookup("CASA_Surrogate");
			ArrayList<String> list = ci.getImporter();
			System.out.println("ImporterContainer: " + list);
			for(String imp : list){
				ic.addItem(new ImporterItem(imp));
			}

			return ic;	  
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		return ic;	  
	}

}
