package de.lehsten.casa.contextserver.gui.explorer.entities;

import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Panel;

import de.lehsten.casa.contextserver.gui.GuiApplication;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.place.Place;

public class EntityEditor extends Panel{
	
	EntityTypeSelect select = new EntityTypeSelect();
	EntityForm form = new EntityForm((GuiApplication) this.getApplication());

	public EntityEditor(){
		this.setCaption("Choose Type");
		this.addComponent(select);
		this.addComponent(form);
	}
	
	public void setItemDataSource(Item item){
		form.setItemDataSource(item);
/*		if (((BeanItem)item).getBean() instanceof Place){
			System.out.println("It's a place!");
			
			 final OpenLayersMap map = new OpenLayersMap();
		        map.setWidth("100%");
		        map.setHeight("500px");

		        // Add an open street layer
		        OpenStreetMapLayer osm = new OpenStreetMapLayer();
		        map.addLayer(osm);

		        // Set the center point
		        map.setCenter(22.30083, 60.452541);

		        // Set the zoom level
		        map.setZoom(8);

		        this.addComponent(map);
			
		}
*/	}
	
	public Item getItemDataSource(){
		return form.getItemDataSource();
	}
	
}
