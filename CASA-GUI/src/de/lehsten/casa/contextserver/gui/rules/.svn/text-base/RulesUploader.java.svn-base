package de.lehsten.casa.contextserver.gui.rules;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.vaadin.Application;
import com.vaadin.data.util.TextFileProperty;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.*;

import de.lehsten.casa.contextserver.communication.CASA_Surrogate;
import de.lehsten.casa.contextserver.types.Rule;

public class RulesUploader extends CustomComponent
                        implements Upload.SucceededListener,
                                   Upload.FailedListener,
                                   Upload.Receiver {

    Panel root;         // Root element for contained components.
    Panel rulePanel;   // Panel that contains the uploaded image.
    File  file;         // File to write to.
    Application a;

    public RulesUploader(Application a) {
    	this.a = a;
        root = new Panel("Upload new rules");
        setCompositionRoot(root);

        // Create the Upload component.
        final Upload upload =
                new Upload("Upload the DRL file here", this);

        // Use a custom button caption instead of plain "Upload".
        upload.setButtonCaption("Upload Now");

        // Listen for events regarding the success of upload.
        upload.addListener((Upload.SucceededListener) this);
        upload.addListener((Upload.FailedListener) this);

        root.addComponent(upload);
        root.addComponent(new Label("Click 'Browse' to "+
                "select a file and then click 'Upload'."));

        // Create a panel for displaying the uploaded rule.
        rulePanel = new Panel("Uploaded rule");
        rulePanel.addComponent(
                         new Label("No rule uploaded yet"));
        root.addComponent(rulePanel);
    }

    // Callback method to begin receiving the upload.
    public OutputStream receiveUpload(String filename,
                                      String MIMEType) {

        System.out.println("Receiving Upload");
        FileOutputStream fos = null; // Output stream to write to
        try { 
        file = new File(a.getContext().getBaseDirectory() +"/uploads/"+ filename);
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
    public void uploadSucceeded(Upload.SucceededEvent event) {
        // Log the upload on screen.
        root.addComponent(new Label("File " + event.getFilename()
                + " of type '" + event.getMIMEType()
                + "' uploaded."));
        final FileResource fileResource =
                new FileResource(file, getApplication());
        TextFileProperty text = new TextFileProperty(file);
        this.rulePanel.removeAllComponents();
        Label ruleText = new Label();
        ruleText.setContentMode(1);
        ruleText.setValue(text.getValue());
        rulePanel.addComponent(ruleText);
        Rule rule = new Rule();
        rule.setRule((String)text.getValue());
       CASA_Surrogate ci;
		try {
			ci = (CASA_Surrogate) new InitialContext().lookup("CASA_Surrogate");
			ci.addRule(rule);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}

    }

    // This is called if the upload fails.
    public void uploadFailed(Upload.FailedEvent event) {
        // Log the failure on screen.
        root.addComponent(new Label("Uploading "
                + event.getFilename() + " of type '"
                + event.getMIMEType() + "' failed."));
    }
}
