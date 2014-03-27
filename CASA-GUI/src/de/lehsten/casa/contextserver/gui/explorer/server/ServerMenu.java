package de.lehsten.casa.contextserver.gui.explorer.server;

import com.vaadin.Application;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.MenuBar;

import de.lehsten.casa.contextserver.communication.CASA_Surrogate;
import de.lehsten.casa.contextserver.gui.GuiApplication;
import de.lehsten.casa.contextserver.types.xml.CSMessage;
import de.lehsten.casa.utilities.communication.CamelMessenger;
import de.lehsten.casa.utilities.interfaces.Messenger;

public class ServerMenu extends MenuBar{
	
	GuiApplication app;
	CASA_Surrogate surrogate;
	
	
	public ServerMenu(GuiApplication app){
		
		surrogate = new CASA_Surrogate();	
		this.app = app;
		this.setImmediate(false);
		this.setWidth("100%");
		this.setHeight("-1px");
		MenuBar.MenuItem mServer = this.addItem("Server", null);
		mServer.setIcon(new ThemeResource("Oxygen-Places-network-server-icon.png"));
		MenuBar.MenuItem mConnectServer = mServer.addItem("Connect to Server", connectServer);
		MenuBar.MenuItem mStartServer = mServer.addItem("(Re)Start Server", restartServer);
		MenuBar.MenuItem mStoreSession = mServer.addItem("Store Session", storeSession);		
		MenuBar.MenuItem mRestoreSession = mServer.addItem("Restore Session", restoreSession);
		MenuBar.MenuItem mSstopServer = mServer.addItem("Stop Server", stopServer);
		
		MenuBar.MenuItem mRules = this.addItem("Rules / Queries", null);
		mRules.setIcon(new ThemeResource("edit-list-order-icon.png"));
		MenuBar.MenuItem mViewRules = mRules.addItem("View rules", viewRules);
		MenuBar.MenuItem mUploadRules = mRules.addItem("Upload Rules", uploadRules);
		MenuBar.MenuItem mApplyAllRules = mRules.addItem("Apply all rules", applyAllRules);
		MenuBar.MenuItem mApplyARule = mRules.addItem("Apply rule", applyRule);
		mApplyARule.setEnabled(false);
		MenuBar.MenuItem mApplyAQuery = mRules.addItem("Apply query", applyQuery);
		mApplyAQuery.setEnabled(false);
		
		MenuBar.MenuItem mEntities = this.addItem("Entities", null);
		mEntities.setIcon(new ThemeResource("Folder-Generic-Silver-icon.png"));
		MenuBar.MenuItem mViewEntities = mEntities.addItem("View entities", viewEntities);
		MenuBar.MenuItem mAddEntity = mEntities.addItem("Add entity", addEntity);
		mAddEntity.setEnabled(true);
		MenuBar.MenuItem mRemoveEntity = mEntities.addItem("Remove entity", removeEntity);
		mRemoveEntity.setEnabled(false);
		MenuBar.MenuItem mUpdateEntity = mEntities.addItem("Update entity", updateEntity);
		mUpdateEntity.setEnabled(false);
		
		
		MenuBar.MenuItem mImporter = this.addItem("Importer", null);
		mImporter.setIcon(new ThemeResource("database-import-icon.png"));
		MenuBar.MenuItem mConnectImporter = mImporter.addItem("Connect to importer", connectImporter);
		MenuBar.MenuItem mViewImporter = mImporter.addItem("View importer", viewImporter);
		MenuBar.MenuItem mAddImporter = mImporter.addItem("Add importer", addImporter);
		mAddImporter.setEnabled(false);
		MenuBar.MenuItem mRemoveImporter = mImporter.addItem("Remove importer", removeImporter);
		mRemoveImporter.setEnabled(false);
	}
	
	MenuBar.Command mycommand = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    }  
	};
	
	MenuBar.Command connectServer = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    	app.connectToServer();
	    }  
	};
	
	MenuBar.Command restartServer = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    	surrogate.stopServer();
	    	surrogate.startServer();
	    }  
	};
	
	MenuBar.Command storeSession = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    	surrogate.storeSession();
	    }  
	};
	
	MenuBar.Command restoreSession = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    	surrogate.restoreSession();
	    }  
	};

	MenuBar.Command stopServer = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    	surrogate.stopServer();
	    }  
	};

	MenuBar.Command viewRules = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    	app.showRules();
	    }  
	};
	
	MenuBar.Command uploadRules = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    	app.showRulesUpload();
	    }  
	};
	
	MenuBar.Command applyAllRules = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    	surrogate.applyRules();
	    }  
	};
	
	MenuBar.Command applyRule = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    	app.showRules();
	    }  
	};
	
	MenuBar.Command applyQuery = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    	app.showQueries();
	    }  
	};
	
	MenuBar.Command viewEntities = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    	app.showEntities();
	    }  
	};
	MenuBar.Command addEntity = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    	app.showEntities();
	    }  
	};
	MenuBar.Command removeEntity = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    	app.showEntities();
	    }  
	};
	MenuBar.Command updateEntity = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    	app.showEntities();
	    }  
	};
	MenuBar.Command connectImporter = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    	app.connectToImporter();
	    	//JMSMessenger messenger = new JMSMessenger("ServerMenu", "jms.queue.importer.controller");
	    	//messenger.send("viewImporter");
	    	
	    	//mainApp.showImporter();
	    }  
	};
	MenuBar.Command viewImporter = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
	    	//JMSMessenger messenger = new JMSMessenger("ServerMenu", "jms.queue.importer.controller");
	    	//messenger.send("viewImporter");
	    	app.showImporter();
	    }  
	};
	MenuBar.Command addImporter = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
//	    	importerMessenger.send(new CSMessage().text="addImporter");

	     }  
	};
	MenuBar.Command removeImporter = new MenuBar.Command() {
	    public void menuSelected(MenuItem selectedItem) {
	    	System.out.println(selectedItem.getText());
//	    	importerMessenger.send(new CSMessage().text="removeImporter");
	    }  
	};
}
