package de.lehsten.casa.contextserver.factentry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lehsten.casa.contextserver.CASAContextServer;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.Rule;
import de.lehsten.casa.contextserver.types.entities.services.Service;

public class LocalFactProvider {

	private ArrayList<Entity> factList = new ArrayList<Entity>();	
	private ArrayList<File> factFileList;
	
	private final static Logger log = LoggerFactory.getLogger( CASAContextServer.class ); 
	
	
	public LocalFactProvider(){
		log.info("Initializing LocalFactProvider");
		// check for local rule file
		lookupFactFiles();
		readFactFiles();
	}

	private void readFactFiles() {
		log.info(factFileList.size() + " fact files found");
		Class[] classes = {Entity.class};
		Class klass = Entity.class;
		URL location = klass.getResource('/'+klass.getName().replace('.', '/')+".class");
		System.out.println(location);
		 try {
			JarFile jar = new JarFile("/Users/phil/Server/Glassfish/eclipse/glassfish31/glassfish3/glassfish/domains/domain1/lib/contextserver.types-0.1.29-SNAPSHOT.jar");
			ArrayList<Class> classList = new ArrayList<Class>();
			for (Enumeration<JarEntry> entries = jar.entries() ; entries.hasMoreElements() ;)
		    {
		        JarEntry entry = entries.nextElement();
		        String file = entry.getName();
		        if(file.contains(".class") && !file.contains("package-info") && !file.contains("interfaces")){
//					System.out.println("Fact: "+f.toString());
					try {
						String classname = file.replace('/', '.').substring(0, file.length() - 6);
			            System.out.println(classname);
						classList.add(Class.forName(classname));
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		    }
			Class[] allClasses = classList.toArray(classes);
			
			JAXBContext context = JAXBContext.newInstance(allClasses);
			Unmarshaller um = context.createUnmarshaller();
			
		for (File f : factFileList){
			try {
				Object o = um.unmarshal(f);
				System.out.println(o.getClass());
//				Class<? extends Entity> x = (Class<? extends Entity>) o.getClass();
//				Object Kzh = (o.getClass().cast(o));
//				System.out.println(( (Service) Kzh).getDescription());
				System.out.println((Entity)o);	
				if(o instanceof Entity){
				factList.add((Entity)o);
				}
			} catch (Exception e) {
				log.error(e.getLocalizedMessage());
				e.printStackTrace();
			}

		}
		

	}catch(JAXBException e){
	e.printStackTrace();	
	}catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	}

	private void lookupFactFiles() {
		try {
			// Glassfish Instance Root folder system variable
			String glassfishInstanceRootPropertyName = "com.sun.aas.instanceRoot";
			 
			// "config" sub-folder name
			String glassfishDomainConfigurationFolderName = "eclipseApps/CASA-Server/facts";
			
			final String instanceRoot = System.getProperty( glassfishInstanceRootPropertyName );
		    if (instanceRoot == null)
		    {
		        throw new FileNotFoundException( "Cannot find Glassfish instanceRoot. Is the com.sun.aas.instanceRoot system property set?" );
		    }
		    
			File[] entries = new File( instanceRoot + File.separator + glassfishDomainConfigurationFolderName).listFiles();
			if (entries == null){
				glassfishDomainConfigurationFolderName = "applications/CASA-Server/facts";
				entries = new File( instanceRoot + File.separator + glassfishDomainConfigurationFolderName).listFiles();		
			}
			ArrayList<File> factsList = new ArrayList<File>();
			if(entries != null){
				
			ArrayList<File> entriesList = new ArrayList(Arrays.asList(entries));
			//		while(!entriesList.isEmpty())
			for (int i=0;i<entriesList.size();i++ ){
				File f = entriesList.get(i);
				if (f.isDirectory() && !f.isHidden()){
					entriesList.addAll(Arrays.asList(f.listFiles()));
				}else if(f.toString().contains(".xml")){
					log.debug("Fact: "+f.toString());
					factsList.add(f);
				}
			}
			}
			this.factFileList= factsList;
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<Entity> getFactList(){
		return factList;
	}
	
}
