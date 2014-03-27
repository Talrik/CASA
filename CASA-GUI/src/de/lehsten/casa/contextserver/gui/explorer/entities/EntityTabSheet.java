package de.lehsten.casa.contextserver.gui.explorer.entities;

import com.vaadin.ui.TabSheet;

public class EntityTabSheet extends TabSheet{
	
	public EntityTabSheet(){
		this.addTab(new EntityEditor(), "New Entity");
		this.addTab(new EntityUploader(this.getApplication()), "Upload Entity");
	}

}
