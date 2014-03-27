package de.lehsten.casa.contextserver.gui.rules;

import java.util.Arrays;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TextArea;

import de.lehsten.casa.contextserver.communication.CASA_Surrogate;
import de.lehsten.casa.contextserver.gui.GuiApplication;
import de.lehsten.casa.contextserver.gui.explorer.entities.EntityContainer;
import de.lehsten.casa.contextserver.gui.explorer.entities.EntityFormFieldFactory;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.Rule;

public class RulesForm extends Form implements ClickListener{

	private Button newRule = new Button("New", (ClickListener) this);
    private Button save = new Button("Save", (ClickListener) this);
    private Button cancel = new Button("Cancel", (ClickListener) this);
    private Button edit = new Button("Edit", (ClickListener) this);
    private Button apply = new Button("Apply", (ClickListener) this);
    private Button remove = new Button("Remove", (ClickListener) this);
    private TextArea text = new TextArea();
    private GuiApplication app;

    public RulesForm(GuiApplication app) {
    	 this.app = app;
    	 this.setFormFieldFactory(new RulesFormFieldFactory());
    	 setWriteThrough(false);
         HorizontalLayout footer = new HorizontalLayout();
         footer.setSpacing(true);
         footer.addComponent(newRule);
         footer.addComponent(save);
         save.setVisible(false);
         footer.addComponent(apply);
         apply.setVisible(false);
         footer.addComponent(edit);
         edit.setVisible(false);
         footer.setVisible(true);
         footer.addComponent(remove);
         remove.setVisible(false);
         setFooter(footer);
     }

	@Override
	 public void buttonClick(ClickEvent event) {
        Button source = event.getButton();

       if (source == save) {
            if (!isValid()) {
                return;
            }
            commit();
            try {
				CASA_Surrogate cs = (CASA_Surrogate) new InitialContext().lookup("CASA_Surrogate");			
				cs.addRule(((Rule)((BeanItem)this.getItemDataSource()).getBean()));
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
            setReadOnly(true);
       } else if (source == newRule) {
    	   this.setItemDataSource(new BeanItem(new Rule()));
       } else if (source == apply) {
           try {
				CASA_Surrogate cs = (CASA_Surrogate) new InitialContext().lookup("CASA_Surrogate");			
				cs.applyRule(  (Rule)((BeanItem)this.getItemDataSource()).getBean()  );
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       
       } else if (source == cancel) {
            discard();
            setReadOnly(true);
        } else if (source == edit) {
            setReadOnly(false);
        } else if (source == remove) {
        	try {
				CASA_Surrogate cs = (CASA_Surrogate) new InitialContext().lookup("CASA_Surrogate");			
				cs.removeRule(  (Rule)((BeanItem)this.getItemDataSource()).getBean()  );
				System.out.println("DEBUG");
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
    }
       
    }
	
	 @Override
     public void setItemDataSource(Item newDataSource) {
         if (newDataSource != null) {
             List<Object> orderedProperties = Arrays.asList(RulesContainer.COMPLETE_NATURAL_COL_ORDER);
             super.setItemDataSource(newDataSource, orderedProperties);
             setReadOnly(true);
             edit.setVisible(true);
             apply.setVisible(true);
             getFooter().setVisible(true);
             
         } else {
             super.setItemDataSource(null);
             getFooter().setVisible(false);
         }
     }

    @Override
     public void setReadOnly(boolean readOnly) {
         super.setReadOnly(readOnly);
         save.setVisible(!readOnly);
         cancel.setVisible(!readOnly);
         apply.setVisible(readOnly);
         edit.setVisible(readOnly);
         remove.setVisible(readOnly);
     }
}
