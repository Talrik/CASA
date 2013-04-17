package de.lehsten.casa.contextserver.gui.explorer.entities;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickListener;

import de.lehsten.casa.contextserver.types.Entity;

public class EntityTypeSelect extends Panel implements ClickListener{
	
	ListSelect select;
	
	public EntityTypeSelect(){
		this.setCaption("Select Entity Type");
		
		// Create the selection component
		select = new ListSelect("Entity Types");
		        
		CodeSource src = Entity.class.getProtectionDomain().getCodeSource();
		try{
		if (src != null){
			URL jar = src.getLocation();
			ZipInputStream zip = new ZipInputStream(jar.openStream());
			ZipEntry ze = null;
			while ((ze = zip.getNextEntry())!= null){
				String name = ze.getName();
				name = name.replace("/", ".");
				if (name.startsWith("de.lehsten.casa.contextserver.types.entities") && name.endsWith(".class")){
					name = name.replace(".class", "");
					select.addItem(name);
				}
			}
			zip.close();
		}
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		// Add some items
		
		 
		select.setNullSelectionAllowed(false);
		 
		// Show 5 items and a scrollbar if there are more
		select.setRows(2);
		this.addComponent(select);
		Button selectButton = new Button("Select", (ClickListener) this);
		this.addComponent(selectButton);
		
	}
	
	private Property.ValueChangeListener listener = new Property.ValueChangeListener(){

		@Override
		public void valueChange(ValueChangeEvent event) {
			System.out.println(event.getProperty());
			System.out.println(select.getValue());
		}
		
	};

	@Override
	public void buttonClick(ClickEvent event) {
		System.out.println(select.getValue());
		try{
		if (this.getParent().getParent() instanceof EntityEditor){
			((EntityEditor)this.getParent().getParent()).setItemDataSource(new BeanItem(Class.forName((String) select.getValue()).newInstance()));
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	

}
