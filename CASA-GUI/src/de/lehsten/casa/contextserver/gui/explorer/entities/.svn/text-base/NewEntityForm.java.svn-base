package de.lehsten.casa.contextserver.gui.explorer.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.vaadin.Application;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;

import de.lehsten.casa.contextserver.gui.GuiApplication;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;
import de.lehsten.casa.contextserver.types.Entity;


public class NewEntityForm extends Form implements ClickListener, ValueChangeListener{

	private Application app;
	private Button save = new Button("Save", (ClickListener) this);
	private Button cancel = new Button("Cancel", (ClickListener) this);
	private Button edit = new Button("Edit", (ClickListener) this);
	private Table select;
	Class <? extends  Entity> bean;

	public NewEntityForm(GuiApplication app) {
		this.app = app;
		setWriteThrough(false);
		// Set form caption and description texts 
		this.setCaption("Entity Information");
		this.setDescription("Please specify the relevant properties.");
		BeanItem item = new BeanItem(new Website());
		this.setItemDataSource(item);

		// Bind the bean item as the data source for the form. 

		HorizontalLayout footer = new HorizontalLayout();
		footer.setSpacing(true);
		footer.addComponent(save);
		footer.addComponent(cancel);
		footer.addComponent(edit);
		footer.setVisible(true);
		
		setFooter(footer);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	 public void valueChange(ValueChangeEvent event) {
         Property property = event.getProperty();
         System.out.println("ChangeEvent");
         if (property == select) {
             Item item = select.getItem(select.getValue());
             System.out.println(item.toString());
         }
     }

}
