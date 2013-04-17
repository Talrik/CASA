package de.lehsten.casa.contextserver.gui.explorer.server;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

import de.lehsten.casa.contextserver.types.xml.CSMessage;
import de.lehsten.casa.utilities.communication.CamelMessenger;
import de.lehsten.casa.utilities.interfaces.Messenger;

public class ServerForm extends Form implements Button.ClickListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Button refresh = new Button("Refresh");
    private Button applyRules = new Button("Apply rules");
    private Messenger messenger;

    public ServerForm() {
         addField("Title", new TextField("Title"));
         addField("Room", new TextField("Room"));
         HorizontalLayout footer = new HorizontalLayout();
         footer.setSpacing(true);
         footer.addComponent(refresh);
         refresh.addListener((ClickListener)this);
         footer.addComponent(applyRules);
         applyRules.addListener((ClickListener)this);
         setFooter(footer);
		 messenger = new CamelMessenger("GUI","ServerForm", "vm:ServerControl");
     }

	@Override
	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton();
		if (source == refresh) {
			// TODO
		}
		if (source == applyRules) {
			messenger.send(new CSMessage().text = "applyRules");
		}
		
	}
}

