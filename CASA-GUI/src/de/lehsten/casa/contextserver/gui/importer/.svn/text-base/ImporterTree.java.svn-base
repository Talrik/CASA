package de.lehsten.casa.contextserver.gui.importer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.vaadin.Application;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Tree;

import de.lehsten.casa.contextserver.ContextServer;
import de.lehsten.casa.contextserver.importer.interfaces.ImporterManager;


public class ImporterTree extends Tree{
	
	private static final long serialVersionUID = 1L;
	public static final Object IMPORTER_MANAGER = "Importer Manager X";
    public static final Object IMPORTER = "Importer";
    ImporterManager impMgr;
	
    public ImporterTree(Application app) {
        addItem(IMPORTER_MANAGER);
        addItem(IMPORTER);
        setSelectable(true);
        setNullSelectionAllowed(false);
       // Make application handle item click events
        addListener((ItemClickListener) app);
        createImporterTree();
}

    private void createImporterTree(){
		// TODO
    }


}
