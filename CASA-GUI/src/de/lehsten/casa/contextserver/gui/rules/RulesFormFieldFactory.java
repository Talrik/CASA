package de.lehsten.casa.contextserver.gui.rules;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.TextArea;

public class RulesFormFieldFactory implements FormFieldFactory{

	@Override
	public Field createField(Item item, Object propertyId, Component uiContext) {
		String pid = (String) propertyId;
		System.out.println(pid);
		if (("properties".equals(pid))){
			return null;
		}
		if (("rule".equals(pid))){
			TextArea textArea = new TextArea("Rule", item.getItemProperty(propertyId));
			textArea.setHeight("100px");
			textArea.setWidth("90%");			
			return textArea;
		}
		else {
			FormFieldFactory fff = DefaultFieldFactory.get();
			Object obj = item.getItemProperty(propertyId).getValue();
			return fff.createField(item, propertyId, uiContext);
		}
	}

}


