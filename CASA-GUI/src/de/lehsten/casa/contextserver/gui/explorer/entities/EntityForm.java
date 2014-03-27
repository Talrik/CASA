package de.lehsten.casa.contextserver.gui.explorer.entities;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;

import de.lehsten.casa.contextserver.communication.CASA_Surrogate;
import de.lehsten.casa.contextserver.gui.GuiApplication;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;

public class EntityForm extends Form implements ClickListener{

	private Button save = new Button("Save", (ClickListener) this);
	private Button update = new Button("Update", (ClickListener) this);
	private Button cancel = new Button("Cancel", (ClickListener) this);
	private Button edit = new Button("Edit", (ClickListener) this);
	private Button remove = new Button("Remove", (ClickListener) this);
	private GuiApplication app;
	private Entity oldEntity = new Entity(); 

	public EntityForm(GuiApplication app) {
		this.app = app;
		setWriteThrough(false);
		this.setFormFieldFactory(new EntityFormFieldFactory());
		// Create a form and use FormLayout as its layout.

		// Set form caption and description texts 
		this.setCaption("Entity Info");
		this.setDescription("Please specify the properties of the entity");

		// Create the custom bean. 
		Website bean = new Website();

		// Create a bean item that is bound to the bean. 
		BeanItem item = new BeanItem(bean);

		// Bind the bean item as the data source for the form. 
		this.setItemDataSource(item);

		HorizontalLayout footer = new HorizontalLayout();
		footer.setSpacing(true);
		footer.addComponent(save);
		footer.addComponent(update);
		footer.addComponent(cancel);
		footer.addComponent(edit);
		footer.addComponent(remove);
		footer.setVisible(true);

		setFooter(footer);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Button source = event.getButton();

		if (source == save) {
			/* If the given input is not valid there is no point in continuing */
			if (!isValid()) {
				return;
			}
			//commit();
			try {
				CASA_Surrogate cs = (CASA_Surrogate) new InitialContext().lookup("CASA_Surrogate");
				commit();				
				cs.addEntity((Entity)((BeanItem)this.getItemDataSource()).getBean());

			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setReadOnly(true);
		} else if (source == update) {
			/* If the given input is not valid there is no point in continuing */
			if (!isValid()) {
				return;
			}
			//commit();
			try {
				CASA_Surrogate cs = (CASA_Surrogate) new InitialContext().lookup("CASA_Surrogate");
				commit();				
				cs.updateEntity(oldEntity, (Entity)((BeanItem)this.getItemDataSource()).getBean());

			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setReadOnly(true);
		
		} else if (source == cancel) {
			discard();
			setReadOnly(true);
		} else if (source == remove) {
			setReadOnly(true);
			try {
				CASA_Surrogate cs = (CASA_Surrogate) new InitialContext().lookup("CASA_Surrogate");
				commit();				
				cs.removeEntity((Entity)((BeanItem)this.getItemDataSource()).getBean());
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			discard();
			this.setItemDataSource(null);
		
		} else if (source == edit) {
			Entity currentEntity = new Entity();
			currentEntity = ((Entity)((BeanItem)this.getItemDataSource()).getBean());
			oldEntity = new Entity();
			oldEntity = currentEntity.clone();
			oldEntity.getProperties().put("ID", currentEntity.getProperties().get("ID"));
			setReadOnly(false);
	}
	}

	@Override
	public void setItemDataSource(Item newDataSource) {
		if (newDataSource != null) {
			super.setItemDataSource(newDataSource);
			/*
			JAXBContext context;
			try {
			context = JAXBContext.newInstance( ((BeanItem)newDataSource).getBean().getClass());
			Marshaller m = context.createMarshaller();
			m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
//			File out = new File("out.xml");
			m.marshal( ((BeanItem)newDataSource).getBean(), Update.out );} 
			catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			getFooter().setVisible(true);
			setReadOnly(true);
		} else {
			super.setItemDataSource(null);
			getFooter().setVisible(false);
		}
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		save.setVisible(!readOnly);
		if (edit.isVisible()){
			update.setVisible(!readOnly);
		}
		cancel.setVisible(!readOnly);
		edit.setVisible(readOnly);
		remove.setVisible(readOnly);
	}
	
	}
