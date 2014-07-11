package de.lehsten.casa.contextserver.gui.explorer.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TwinColSelect;

import de.lehsten.casa.contextserver.gui.GenericList;
import de.lehsten.casa.contextserver.gui.ListView;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.place.Place;
import de.lehsten.casa.contextserver.types.entities.services.Service;

public class EntityFormFieldFactory implements FormFieldFactory{

	@Override
	public Field createField(final Item item, Object propertyId, Component uiContext) {
		// TODO Auto-generated method stub
		String pid = (String) propertyId;

		if ("properties".equals(pid)){
			return null;
		}
		if ("event".equals(pid)){
			return null;
		}
		if ("places".equals(pid)){
			return null;
		}
		if ("subServices".equals(pid)){
			return null;
		}
		if ("restrictions".equals(pid)){
			/*
			final ListSelect ls = new ListSelect("Restrictions");
            ls.setNullSelectionAllowed(true);
            ls.setMultiSelect(true);
            if (item instanceof BeanItem){
            	if (((BeanItem)item).getBean() instanceof Service){
            		ls.addItem("autor");
            		ls.addItem("dozent");
            		ls.addItem("tutor");
            		ls.addListener(new Property.ValueChangeListener() {
                private static final long serialVersionUID = 6081009810084203857L;
                
                public void valueChange(ValueChangeEvent event) {
                    System.out.println(event.getProperty().getType().getName());
                    if (event.getProperty().getValue() != null)
                        System.out.println(event.getProperty().getValue().getClass().getName());
                    	Collection value = ((Collection)event.getProperty().getValue());
                    	ArrayList<String> restrictions = new ArrayList<String>();
                    	for (Object o : value){
                    		if (o instanceof String)
                    			restrictions.add((String)o);
                    	}
                    	((Service)((BeanItem)item).getBean()).setRestrictions(restrictions);
                }
            });
 //           		
            	}
            }
            
            
            ls.setImmediate(true);
            
            return ls;
            */
			return null;
		}
		else {
			FormFieldFactory fff = DefaultFieldFactory.get();
			Object obj = item.getItemProperty(propertyId).getValue();
			/*
			if (obj != null) {
				HorizontalSplitPanel hSP = (HorizontalSplitPanel) uiContext.getParent().getParent().getParent().getParent();
				Component firstComp = hSP.getFirstComponent();
				//				System.out.println("[EFFF]: lv.firstComp = " + firstComp + "; Caption: " + firstComp.getCaption());				
				ListView secComp = (ListView) hSP.getSecondComponent();
				//				System.out.println("[EFFF]: lv.secComp = " + secComp + "; Caption: " + secComp.getCaption());

				// Alle verfügbaren Entity Types auslesen
				GenericList entList = (GenericList) secComp.getFirstComponent();
				Collection<Entity> entCol= (Collection<Entity>) entList.getItemIds();
				//				System.out.println("[EFFF]: genList.ItemIDs = " + entCol);

				String propClassName= obj.getClass().getName();
				System.out.println("[EFFF]: propClass = "  + propClassName);
				//System.out.println("[EFFF]: refClass = " + (new Place()).getClass().getName());
				if("java.util.ArrayList".equals(propClassName)){
					System.out.println("[EFFF]: propID = " + pid);

					// Datentyp der ArrayList herausfinden (propClassName)
				//}		Zum weiterentwickeln auskommentiert...
				//if(propClassName.equals((new Place()).getClass().getName())){  GENERISCH!?

					// initialize ListSelect
					ListSelect liSe = new ListSelect();
					liSe.setCaption("Generics; e.g. Places");
					liSe.setNullSelectionAllowed(true);
					liSe.setMultiSelect(true);
					liSe.setRows(2);
					
					// mögliche Entities scannen
					Entity ent;
					Iterator<Entity> entIter = entCol.iterator();
					while (entIter.hasNext()) {
						ent = entIter.next();
						HashMap<String,String> entProps = ent.getProperties();
						String entName = entProps.toString();
						System.out.println("[EFFF]: Entity = " + entName + "; " + ent.getClass().getName());
						// mögliche Entities vergleichen und passende anbieten -> generisch
						// --->> Problem, da nur leere ArrayLists übergeben werden, Typ nicht auslesbar

						// generisch, statt instance of Place
						if(ent instanceof Place){
							// aus entCol (eine Liste von) Items erzeugen, die als Auswahlliste übergeben
//							liSe.addItem(ent.getProperties());	// zur schöneren Darstellung Übergabe der Props
							liSe.addItem(ent);
							System.out.println("[EFFF]: check!!!");
						}
					}

					return liSe;
//					return fff.createField(it, "latitude", uiContext);
				}
			}
*/
			return fff.createField(item, propertyId, uiContext);
		}
	}

}
