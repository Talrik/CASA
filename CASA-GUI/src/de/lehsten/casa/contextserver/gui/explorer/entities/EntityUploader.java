package de.lehsten.casa.contextserver.gui.explorer.entities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.vaadin.Application;
import com.vaadin.data.util.TextFileProperty;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.SucceededEvent;

import de.lehsten.casa.contextserver.communication.CASA_Surrogate;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.Rule;
import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;

public class EntityUploader extends CustomComponent 
							implements 	Upload.SucceededListener,
										Upload.FailedListener,
										Upload.Receiver {
	
    Panel root;         	// Root element for contained components
    Panel entityPanel;   	// Panel that contains the uploaded entity
    File  file;         	// File to write to.
    Application a;

    public EntityUploader(Application a){
    	this.a = a;
        root = new Panel("Upload new entities");
        setCompositionRoot(root);

        // Create the Upload component.
        final Upload upload =
                new Upload("Upload the XML file here", this);

        // Use a custom button caption instead of plain "Upload".
        upload.setButtonCaption("Upload Now");

        // Listen for events regarding the success of upload.
        upload.addListener((Upload.SucceededListener) this);
        upload.addListener((Upload.FailedListener) this);

        root.addComponent(upload);
        root.addComponent(new Label("Click 'Browse' to "+
                "select a file and then click 'Upload'."));

        // Create a panel for displaying the uploaded rule.
        entityPanel = new Panel("Uploaded entity");
        entityPanel.addComponent(
                         new Label("No entity uploaded yet"));
        root.addComponent(entityPanel);
    }
    
    @Override
    public OutputStream receiveUpload(String filename,
    		String MIMEType) {

    	System.out.println("Receiving Upload");
    	FileOutputStream fos = null; // Output stream to write to
    	try { 
    		file = new File(/*a.getContext().getBaseDirectory() +"/uploads/"+*/ filename);
    		System.out.println("Saved as "+file);
    		// Open the file for writing.
    		fos = new FileOutputStream(file);
    	} catch (final java.io.FileNotFoundException e) {
    		// Error while opening the file. Not reported here.
    		e.printStackTrace();
    		return null;
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return fos; // Return the output stream to write to
    }

    // This is called if the upload is finished.
    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        // Log the upload on screen.
        root.addComponent(new Label("File " + event.getFilename()
                + " of type '" + event.getMIMEType()
                + "' uploaded."));
        final FileResource fileResource =
                new FileResource(file, getApplication());
        TextFileProperty text = new TextFileProperty(file);
        this.entityPanel.removeAllComponents();
        Label entityText = new Label();
        entityText.setContentMode(1);
        entityText.setValue(text.getValue());
        entityPanel.addComponent(entityText);
        JAXBContext context;
		try {
		context = JAXBContext.newInstance(Website.class, LocationWebsite.class );
		Unmarshaller um = context.createUnmarshaller();
		Object o = um.unmarshal(file);
		Entity entity = (Entity)o;
		 CASA_Surrogate ci;
			try {
				ci = (CASA_Surrogate) new InitialContext().lookup("CASA_Surrogate");
				ci.addEntity(entity);
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				System.err.println(e.getMessage());
			}
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    // This is called if the upload fails.
    @Override
    public void uploadFailed(Upload.FailedEvent event) {
        // Log the failure on screen.
        root.addComponent(new Label("Uploading "
                + event.getFilename() + " of type '"
                + event.getMIMEType() + "' failed."));
    }

}
