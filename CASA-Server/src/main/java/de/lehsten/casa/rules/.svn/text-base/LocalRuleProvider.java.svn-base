package de.lehsten.casa.rules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

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
import de.lehsten.casa.contextserver.types.Rule;

public class LocalRuleProvider {
	
	private ArrayList<File> ruleFileList;
	private ArrayList<Rule> ruleList = new ArrayList<Rule>();
	private PackageBuilderConfiguration cfg;
	private final static Logger log = LoggerFactory.getLogger( CASAContextServer.class ); 
	   
	public ArrayList<Rule> getRuleList() {
		return ruleList;
	}

	public LocalRuleProvider(){
		Properties properties = new Properties();
    	properties.setProperty( "drools.dialect.java.compiler.lnglevel","1.6" );
    	cfg =   new PackageBuilderConfiguration( properties );
    	
		// check for local rule file
		lookupRuleFiles();
		readRuleFiles();
	}
	

	private void lookupRuleFiles(){
		try {
			// Glassfish Instance Root folder system variable
			String glassfishInstanceRootPropertyName = "com.sun.aas.instanceRoot";
			 
			// "config" sub-folder name
			String glassfishDomainConfigurationFolderName = "eclipseApps/CASA-Server/rules";
			
			final String instanceRoot = System.getProperty( glassfishInstanceRootPropertyName );
		    if (instanceRoot == null)
		    {
		        throw new FileNotFoundException( "Cannot find Glassfish instanceRoot. Is the com.sun.aas.instanceRoot system property set?" );
		    }
		    
			File[] entries = new File( instanceRoot + File.separator + glassfishDomainConfigurationFolderName).listFiles();
			if (entries == null){
				glassfishDomainConfigurationFolderName = "applications/CASA-Server/rules";
				entries = new File( instanceRoot + File.separator + glassfishDomainConfigurationFolderName).listFiles();		
			}

			ArrayList<File> entriesList = new ArrayList(Arrays.asList(entries));
			ArrayList<File> rulesList = new ArrayList<File>();
			//		while(!entriesList.isEmpty())
			for (int i=0;i<entriesList.size();i++ ){
				File f = entriesList.get(i);
				if (f.isDirectory() && !f.isHidden()){
					entriesList.addAll(Arrays.asList(f.listFiles()));
				}else if(f.toString().contains(".drl")){
					log.debug("Rule: "+f.toString());
					rulesList.add(f);
				}
			}
			this.ruleFileList= rulesList;
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void readRuleFiles() {
		log.info(ruleFileList.size() + " rules found");
		for (File f : ruleFileList){
			try {
				KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(cfg);
		
				  FileReader reader = new FileReader(f);
				  StringWriter buffer = new StringWriter();
				  PrintWriter out = new PrintWriter( buffer );

				  for ( int c; ( c = reader.read() ) != -1; )
				  out.print( (char) c);
				  
				  String result = buffer.toString();
				kbuilder.add(ResourceFactory.newReaderResource(new StringReader(result)), ResourceType.DRL);
		
				KnowledgeBuilderErrors errors = kbuilder.getErrors();
				if (errors.size() > 0) {
					for (KnowledgeBuilderError error : errors) {
						System.err.println(error);
					}
					throw new IllegalArgumentException("Could not parse knowledge.");
				}
				for (KnowledgePackage kp : kbuilder.getKnowledgePackages()){
					for (org.drools.definition.rule.Rule r : kp.getRules()){
						Rule rule = new Rule();
						rule.setPackageName(r.getPackageName());
						rule.setName(r.getName());
						rule.setMetaData(r.getMetaData());
						rule.setRule(result);
						ruleList.add(rule);
						log.debug("Rule: "+rule.getName()+" Package: "+rule.getPackageName());
					}
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		

	}

	
}



