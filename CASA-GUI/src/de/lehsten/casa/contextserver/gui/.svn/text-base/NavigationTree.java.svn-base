package de.lehsten.casa.contextserver.gui;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.vaadin.Application;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Tree;
import com.vaadin.data.util.HierarchicalContainer;

import de.lehsten.casa.contextserver.importer.interfaces.Importer;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.event.Event;

public class NavigationTree extends Tree {
     public static final Object SHOW_ALL = "Show all";
    	 
     

    public NavigationTree(Application app) {
             addItem(SHOW_ALL);
             setSelectable(true);
             setNullSelectionAllowed(false);
            // Make application handle item click events
             addListener((ItemClickListener) app);
     }
    
    public void createClassStructure(Collection<Entity> c){
    	Iterator it = c.iterator();
    	while (it.hasNext()){
    		Entity e = (Entity)it.next(); 
    		int i = 0;
    		Class cla = e.getClass();
//			System.out.println("Handling Class "+i+": "+cla.getSimpleName());
    		if (this.containsId(cla.getSimpleName())){
//    			System.out.println(cla.getSimpleName() + " already known.");
    		}else{
    			String clas = cla.getSimpleName();
    			this.addItem(clas);
    			this.setChildrenAllowed(cla, false);
    			while (cla.getSuperclass() != null){
        			clas = cla.getSimpleName();
    				//item exists && super class exists
    				if(this.containsId(cla.getSuperclass().getSimpleName())){
    					String superclass = cla.getSuperclass().getSimpleName();
    					this.setChildrenAllowed(superclass, true);
 //   					System.out.println(superclass +" exists and " +clas+" is now child of "+superclass+ " "+
    					this.setParent(clas, superclass);
//    					);
        			}
    				//create super class
    				else {
    					String superclass = cla.getSuperclass().getSimpleName();
    					this.addItem(superclass);
    					this.setChildrenAllowed(superclass, true);
    					this.addItem(clas);
    					
//    					System.out.println(superclass +" created and "+clas+" is now child of "+superclass+ " "+
    					this.setParent(clas, superclass);
    				}
//    				System.out.println("Class "+i+": "+cla.getSimpleName());
    				cla = cla.getSuperclass();
    				i++;
    			}
    		}
    	}
    	this.setChildrenAllowed(this.getItem(SHOW_ALL), true);
    	this.setParent(this.getItem("Object"), SHOW_ALL);
    }
    
    public void createImporterClassStructure(Collection<Importer> c){
    	Iterator it = c.iterator();
    	while (it.hasNext()){
    		Entity e = (Entity)it.next(); 
    		int i = 0;
    		Class cla = e.getClass();
//			System.out.println("Handling Class "+i+": "+cla.getSimpleName());
    		if (this.containsId(cla.getSimpleName())){
//    			System.out.println(cla.getSimpleName() + " already known.");
    		}else{
    			String clas = cla.getSimpleName();
    			this.addItem(clas);
    			this.setChildrenAllowed(cla, false);
    			while (cla.getSuperclass() != null){
        			clas = cla.getSimpleName();
    				//item exists && super class exists
    				if(this.containsId(cla.getSuperclass().getSimpleName())){
    					String superclass = cla.getSuperclass().getSimpleName();
    					this.setChildrenAllowed(superclass, true);
 //   					System.out.println(superclass +" exists and " +clas+" is now child of "+superclass+ " "+
    					this.setParent(clas, superclass);
//    					);
        			}
    				//create super class
    				else {
    					String superclass = cla.getSuperclass().getSimpleName();
    					this.addItem(superclass);
    					this.setChildrenAllowed(superclass, true);
    					this.addItem(clas);
    					
//    					System.out.println(superclass +" created and "+clas+" is now child of "+superclass+ " "+
    					this.setParent(clas, superclass);
    				}
//    				System.out.println("Class "+i+": "+cla.getSimpleName());
    				cla = cla.getSuperclass();
    				i++;
    			}
    		}
    	}
    	this.setChildrenAllowed(this.getItem(SHOW_ALL), true);
    	this.setParent(this.getItem("Object"), SHOW_ALL);
    }
   
 }