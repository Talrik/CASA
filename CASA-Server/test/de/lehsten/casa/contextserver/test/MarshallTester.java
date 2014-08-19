package de.lehsten.casa.contextserver.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.jar.JarFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.AssertBehaviorOption;
import org.drools.conf.EventProcessingOption;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lehsten.casa.contextserver.CASAContextServer;
import de.lehsten.casa.contextserver.types.Entity;
import de.lehsten.casa.contextserver.types.entities.person.Person;
import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.entities.services.websites.EventWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.LocationWebsite;
import de.lehsten.casa.contextserver.types.entities.services.websites.Website;

public class MarshallTester {


	CASAContextServer cs;
	KnowledgeBase kbase; 
	StatefulKnowledgeSession ksession;


	@Before
	public void setUp() throws Exception {
		setupServer();
	}
	
	@After
	public void tearDown() throws Exception {
		   long l = ksession.getFactCount();
           ksession.dispose();	 
    	   System.out.println("Knowledge session with "+ l +" facts disposed.");
	}

	private void setupServer(){
		try {
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			kbuilder.add(ResourceFactory.newClassPathResource("de/lehsten/casa/rules/studip/EventService_NextCourse.drl"),
					ResourceType.DRL);			
			KnowledgeBuilderErrors errors = kbuilder.getErrors();
			if (errors.size() > 0) {
				for (KnowledgeBuilderError error : errors) {
					System.err.println(error);
				}
				throw new IllegalArgumentException("Could not parse knowledge.");
			}
			KnowledgeBaseConfiguration config = KnowledgeBaseFactory
				.newKnowledgeBaseConfiguration();
			config.setOption(EventProcessingOption.STREAM);
			config.setOption(AssertBehaviorOption.EQUALITY);
			kbase = KnowledgeBaseFactory.newKnowledgeBase(config);
			kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		ksession = kbase.newStatefulKnowledgeSession();
		KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
				.newFileLogger(ksession, "Log_ContextServer");

	}


	@Test
	public void test() {
		LocationWebsite kzh = new LocationWebsite();
		kzh.setSource("predefined");
		kzh.setDescription("Seite f�r das KZH");
		kzh.setTargetURL("http://www.google.de");
		kzh.setTitle("Webseite des Konrad-Zuse-Hauses");
		JAXBContext context;
		try {
		context = JAXBContext.newInstance( LocationWebsite.class );
		Marshaller m = context.createMarshaller();
		m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		m.marshal( kzh, System.out );
		File f = new File("out.xml");
		m.marshal( kzh, f);
		Unmarshaller um = context.createUnmarshaller();
		LocationWebsite Kzh = (LocationWebsite) um.unmarshal(f);
		m.marshal( Kzh, System.out );
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void addXEntity() {
		JAXBContext context;
		try {
		context = JAXBContext.newInstance(Website.class, LocationWebsite.class );
		File f = new File("test.xml");
		Unmarshaller um = context.createUnmarshaller();
		Object o = um.unmarshal(f);
		System.out.println(o.getClass());
		Class<? extends Entity> x = (Class<? extends Entity>) o.getClass();
		Object Kzh = (o.getClass().cast(o));
		System.out.println(( (Service) Kzh).getDescription());
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void addAllEntities(){
		JAXBContext context;
		Class[] classes = {Website.class, LocationWebsite.class};
//		try {
			Class klass = Entity.class;
			URL location = klass.getResource('/'+klass.getName().replace('.', '/')+".class");
			System.out.println(location);
			String path = (location.getPath());
			path.indexOf("Entity");
			path = path.replace("Entity.class", "");
			System.out.println(path);
			File dir = new File(path);
			File[] entries = dir.listFiles();
			ArrayList<File> entriesList = new ArrayList(Arrays.asList(entries));
			ArrayList<Class> classList = new ArrayList<Class>();
			//		while(!entriesList.isEmpty())
			for (int i=0;i<entriesList.size();i++ ){
				File f = entriesList.get(i);
				if (f.isDirectory() && !f.isHidden()){
					entriesList.addAll(Arrays.asList(f.listFiles()));
				}else if(f.toString().contains(".class") && !f.toString().contains("package-info")){
//					System.out.println("Fact: "+f.toString());
					try {
						String classPath = f.getPath();
						classPath = classPath.substring(classPath.indexOf("de"), classPath.length()-6);
						classPath = classPath.replace("/", ".");
						System.out.println(classPath);
						classList.add(Class.forName(classPath));
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			Class[] allClasses = classList.toArray(classes);
//			this.factFileList= factsList;
//			File[] entries = new File( location.toString()).listFiles();
//			JarFile jar = new JarFile("contextserver.types-0.1.28-SNAPSHOT.jar");
/*		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
*/		try {
		context = JAXBContext.newInstance(allClasses);
		File f = new File("test.xml");
		Unmarshaller um = context.createUnmarshaller();
		Object o = um.unmarshal(f);
		System.out.println(o.getClass());
		Class<? extends Entity> x = (Class<? extends Entity>) o.getClass();
		Object Kzh = (o.getClass().cast(o));
		System.out.println(( (Service) Kzh).getDescription());
		}
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
