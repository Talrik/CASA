package de.lehsten.casa.contextserver.gui;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;

import de.lehsten.casa.contextserver.gui.explorer.importer.ImporterContainer;


public class GenericList extends Table {
	public GenericList(GuiApplication app, Container container) {

		setSizeFull();
		setContainerDataSource(container);

		/*
		 * Make table selectable, react immediatedly to user events, and pass events to the
		 * controller (our main application)
		 */
		setSelectable(true);
		setImmediate(true);
		addListener((Property.ValueChangeListener) app);
		/* We don't want to allow users to de-select a row */
		setNullSelectionAllowed(false);
	}
}
