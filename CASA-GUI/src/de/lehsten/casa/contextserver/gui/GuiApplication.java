package de.lehsten.casa.contextserver.gui;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.CamelExchangeException;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.*; 
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

import de.lehsten.casa.contextserver.communication.CASA_Surrogate;
import de.lehsten.casa.contextserver.communication.GUIRouteBuilder;
import de.lehsten.casa.contextserver.gui.explorer.entities.EntityContainer;
import de.lehsten.casa.contextserver.gui.explorer.entities.EntityEditor;
import de.lehsten.casa.contextserver.gui.explorer.entities.EntityForm;
import de.lehsten.casa.contextserver.gui.explorer.entities.EntityTabSheet;
import de.lehsten.casa.contextserver.gui.explorer.importer.ImporterContainer;
import de.lehsten.casa.contextserver.gui.explorer.server.ServerMenu;
import de.lehsten.casa.contextserver.gui.importer.ImporterForm;
import de.lehsten.casa.contextserver.gui.importer.ImporterItem;
import de.lehsten.casa.contextserver.gui.rules.RulesContainer;
import de.lehsten.casa.contextserver.gui.rules.RulesForm;
import de.lehsten.casa.contextserver.gui.rules.RulesUploader;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.Rule;

public class GuiApplication extends Application implements 	Button.ClickListener, 
															ValueChangeListener,
															ItemClickListener{
	
	private static final long serialVersionUID = -2430687456618808298L; 
	private ListView mainViewRight = new ListView();
	private BeanItemContainer dataSource;
	private CASA_Surrogate surrogate = null;
	private NavigationTree tree = new NavigationTree(this);
	private EntityContainer entityDataSource = null;
	private RulesContainer rulesDataSource = null;
	private ImporterContainer importerDataSource = null; 
	private boolean isConnectedToServer= false;
	private boolean isConnectedToImporter= false;
	private GUIRouteBuilder builder;
	 private ListView listView = null;
	 private GenericList genericList = null;
	 private EntityTabSheet entitySheet = null;
	private ImporterForm importerForm = null;
	private RulesForm rulesForm = null;
	 
	 
	 
	 public void setDataSource(BeanItemContainer dataSource){
		 this.dataSource = dataSource;
	 }

	 /*
	 public BeanItemContainer getDataSource() {
		 tree.createClassStructure(entityDataSource.getItemIds());
          return entityDataSource;
    }
    */
	 
	 public BeanItemContainer getDataSource(){
		 return this.dataSource;
	 }
	 private AbsoluteLayout mainLayout;
	 private HorizontalSplitPanel mainView = new HorizontalSplitPanel();
	 
	 @Override
	public void init() {
		 this.setTheme("casa-guitheme");
		 builder = new GUIRouteBuilder(this);
		 if (isConnectedToServer && isConnectedToImporter){
		 try {
			 Object o = new InitialContext().lookup("CASA_Surrogate");
			 if (o instanceof CASA_Surrogate){
				 surrogate = (CASA_Surrogate) o;
			 } 
			 else {
				 new InitialContext().unbind("CASA_Surrogate");
				 surrogate = new CASA_Surrogate();
			 }
		 }catch(NamingException e){
			 surrogate = new CASA_Surrogate();
		 }catch(ClassCastException e){
			 surrogate = new CASA_Surrogate();
		 }
		 try {
			entityDataSource = EntityContainer.createWithCASAData();
		} catch (Exception e) {
			this.getMainWindow().showNotification("Not connected to server","<br>You are currently not connected to a CASA server", Window.Notification.TYPE_WARNING_MESSAGE);
	}
		 rulesDataSource = RulesContainer.createWithCASAData();
		 importerDataSource =  ImporterContainer.createWithCASAData();		 
		 }else{
			 entityDataSource = EntityContainer.createWithData();
		 }
		 buildMainLayout();
	}
	
	
	
	private void buildMainLayout(){
		setMainWindow(new Window("CASA node"));
        mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
        mainLayout.addComponent(new ServerMenu(this));
        mainLayout.addComponent(createMainView(), "top:23.0px;left:0.0px;");
        getMainWindow().setContent(mainLayout);
        getMainWindow().addListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Shutting down application");
				builder.shutdownContext();
				
			}
		});

   }
	
	 private void setMainViewComponent(Component c) {
         mainView.setSecondComponent(c);
     }
	 
	 private void setMainViewUpperComponent(Component c){
		 this.mainViewRight.setUpperComponent(c);
	 }
	 
	 private void setMainViewLowerComponent(Component c){
		 this.mainViewRight.setLowerComponent(c);
	 }
	 
	 private void setMainTreeComponent(Component c) {
         mainView.setFirstComponent(c);
     }

	 private HorizontalSplitPanel createMainView(){
		 mainView.setImmediate(false);
		 mainView.setMargin(false);
		 mainView.setFirstComponent(tree);
		 this.mainViewRight = getEntityListView();
		 mainView.setSecondComponent(this.mainViewRight);
		 return mainView; 
	 }
	 
// ListView 	 
	 
	 private ListView getEntityListView() {
	      if (listView == null) {
	    	  try{
	 		  tree.createClassStructure(entityDataSource.getItemIds());
	 		  if (isConnectedToServer){
	          genericList = new GenericList(this, EntityContainer.createWithCASAData());
	          }else{
	        	  genericList = new GenericList(this, EntityContainer.createWithData());
	          }
	          entitySheet = new EntityTabSheet();
	          listView = new ListView(genericList, entitySheet);
	      }catch(CamelExchangeException e){
	    	  }
	      }
	      return listView;
	  }
	 
	 private ListView getRulesListView() {
	      if (listView == null) {
	    	  genericList = new GenericList(this, RulesContainer.createWithCASAData());
	    	  rulesForm = new RulesForm(this);
	         listView = new ListView(genericList, rulesForm);
	      }
	      return listView;
	  }

		private ListView getImporterListView() {
			
			importerForm = new ImporterForm(this);
			importerDataSource = new ImporterContainer().createWithCASAData();
			genericList = new GenericList(this, importerDataSource);
			genericList.setVisibleColumns(importerDataSource.NATURAL_COL_ORDER);
			genericList.setColumnHeaders(importerDataSource.COL_HEADERS_ENGLISH);
			listView = new ListView(genericList, importerForm);
			return listView;
		}
	 
	 public void showRules(){
		 genericList = new GenericList(this, RulesContainer.createWithCASAData());
		  genericList.setVisibleColumns(RulesContainer.BASIC_NATURAL_COL_ORDER);
    	  genericList.setColumnHeaders(RulesContainer.TABLE_COL_HEADERS_ENGLISH);
		 this.mainViewRight.setUpperComponent(genericList);
		 rulesForm = new RulesForm(this);
		 this.mainViewRight.setLowerComponent(rulesForm);
		 this.setMainViewComponent(mainViewRight);
	 }
	 
	 public void showEntities(){
		 try{
		 entityDataSource = EntityContainer.createWithCASAData();
		 genericList = new GenericList(this, entityDataSource);
		 genericList.setVisibleColumns(entityDataSource.NATURAL_COL_ORDER);
		 genericList.setColumnHeaders(entityDataSource.COL_HEADERS_ENGLISH);

		 tree.createClassStructure(entityDataSource.getItemIds());
		 this.mainViewRight.setUpperComponent(genericList);
		 this.mainViewRight.setLowerComponent(entitySheet);
		 this.setMainViewComponent(mainViewRight);
		 }
		 catch(Exception e){
				this.getMainWindow().showNotification("Not connected to server","<br>You are currently not connected to a CASA server", Window.Notification.TYPE_WARNING_MESSAGE);
		 }
	 }
	 
	 public void showRulesUpload(){
		 this.mainView.setSecondComponent(new RulesUploader(this));
	 }
	 
	 public void showAddEntity(){
		 this.mainView.setSecondComponent(new RulesUploader(this));
	 }
	 
	 public void showQueries(){
		 
	 }
	 
		public void showImporter(){
			this.setMainViewComponent(getImporterListView());
		}
	 
@Override
public void buttonClick(ClickEvent event) {
	final Button source = event.getButton();
}

@Override
public void valueChange(ValueChangeEvent event) {
	Property property = event.getProperty();
/*	System.out.println(property);
	System.out.println(event.toString());
	System.out.println("Value: " + genericList.getValue());
*/	BeanItem item = null;
	if (genericList.getValue() != null){
		item = (BeanItem) genericList.getItem(genericList.getValue());
		System.out.println(genericList.getItem(genericList.getValue()));
		System.out.println(genericList.getValue().getClass());
		item = new BeanItem(item.getBean());
		if (item.getBean() instanceof ImporterItem){
			importerForm.setItemDataSource(item);
		}	
		else if(item.getBean() instanceof Rule){
			rulesForm.setItemDataSource(item);
		}else 
		
			if (item.getBean() instanceof Entity){
				EntityEditor entityEditor = new EntityEditor();
				this.mainViewRight.setLowerComponent(entityEditor);
				if( item != entityEditor.getItemDataSource()) {
					entityEditor.setItemDataSource(item); 
				}
			}
	}
}
		
	





@Override
public void itemClick(ItemClickEvent event) {
    if(event.getSource() == tree) {
        Object itemId = event.getItemId();
        if (itemId != null) {

        }
    }
}

public void setServerStatus(boolean value){
	this.isConnectedToServer = value;
}

public void setImporterStatus(boolean value){
	this.isConnectedToImporter = value;
}

public void connectToServer(){
	builder.connectToServer();
}
public void connectToImporter(){
	builder.connectToImporter();
}

private void refreshEntityContainer(){
	 try {
		entityDataSource = EntityContainer.createWithCASAData();
	} catch (CamelExchangeException e) {
		this.getMainWindow().showNotification("Not connected to server","<br>You are currently not connected to a CASA server", Window.Notification.TYPE_WARNING_MESSAGE);
	}
}

}
