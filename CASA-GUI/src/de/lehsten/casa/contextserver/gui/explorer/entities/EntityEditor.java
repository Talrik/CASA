package de.lehsten.casa.contextserver.gui.explorer.entities;

import com.vaadin.data.Item;
import com.vaadin.ui.Panel;

import de.lehsten.casa.contextserver.gui.GuiApplication;

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
	}
	
	public Item getItemDataSource(){
		return form.getItemDataSource();
	}
	
}
