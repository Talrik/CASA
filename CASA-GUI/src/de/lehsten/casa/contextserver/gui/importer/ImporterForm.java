package de.lehsten.casa.contextserver.gui.importer;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;

import de.lehsten.casa.contextserver.communication.CASA_Surrogate;
import de.lehsten.casa.contextserver.gui.GuiApplication;

public class ImporterForm extends Form implements ClickListener {

	private Button start = new Button("Start", (ClickListener) this);
    private Button stop = new Button("Stop", (ClickListener) this);
    private GuiApplication app;
    private CASA_Surrogate surrogate;
	
	
	public ImporterForm(GuiApplication guiApplication) {
		 this.app = app;
    	 setWriteThrough(false);
 		 surrogate = new CASA_Surrogate();
         HorizontalLayout footer = new HorizontalLayout();
         footer.setSpacing(true);
         footer.addComponent(start);
         footer.addComponent(stop);
         footer.setVisible(true);
         
         setFooter(footer);
		
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Button source = event.getButton();
		String importerName = ((ImporterItem)((BeanItem)this.getItemDataSource()).getBean()).getName();
	       if (source == start) {
	            /* If the given input is not valid there is no point in continuing */
	            if (!isValid()) {
	                return;
	            }
	            
	            surrogate.startImporter(importerName);
	            commit();
	            setReadOnly(true);
	            
	           // TODO implement startImporter
	        } else if (source == stop) {
	        	surrogate.stopImporter(importerName);
	            discard();
	            setReadOnly(true);
	            // TODO implement stopImporter
	        }
		
	}
	
	@Override
	public void setItemDataSource(Item newDataSource) {
		if (newDataSource != null) {
			super.setItemDataSource(newDataSource);
//			getFooter().setVisible(true);
		} else {
			super.setItemDataSource(null);
//			getFooter().setVisible(false);
		}
	}

}
