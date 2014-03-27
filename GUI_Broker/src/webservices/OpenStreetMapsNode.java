package webservices;
/*
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import services.Haltestellen;

import de.lehsten.casa.contextserver.types.entities.services.Service;
import de.lehsten.casa.contextserver.types.xml.CSMessage;
import de.lehsten.casa.utilities.communication.CamelMessenger;
import de.lehsten.casa.utilities.communication.serializing.CSMessageConverter;
import de.lehsten.casa.utilities.communication.serializing.EntitySerializer;
import de.lehsten.casa.utilities.services.ServiceProxy;

@WebService(serviceName = "OpenStreetMapsNode")
@Stateless()
public class OpenStreetMapsNode {
	

	String ContextServerIP;
	String layer;
	int ContextServerPort;
	InitialContext ctx;
	CamelContext camelContext;
	ProducerTemplate serverProducer;
	ProducerTemplate factEntryProducer;
	CamelMessenger messenger;
	HashMap<String,ServiceProxy> proxys = new HashMap<String,ServiceProxy>();
	private final static Logger log = LoggerFactory.getLogger( OpenStreetMapsNode.class ); 

	
	public OpenStreetMapsNode(){
		init();
		proxys.put("Haltestellen", new Haltestellen());
	}
	
	private void init(){		
		try { 
			this.layer = "OSM";
			ctx = new InitialContext();
			new InitialContext().rebind("OSMNode", this);
			startContext();
			serverProducer.setDefaultEndpoint(camelContext.getEndpoint("direct:CASA_Server"));
			
		} catch (NamingException e) {
			e.printStackTrace();
		}
}
	private void startContext(){
		try { 
			System.out.println("Lookup context...");
			ctx = new InitialContext();
			this.camelContext = (CamelContext) ctx.lookup(layer+"Context");
			System.out.println("Context found: "+ this.camelContext);
			this.camelContext = null;
		} catch (NamingException e1 ) {
			System.out.println(e1.getMessage());
		}
		if (this.camelContext == null){
			DefaultCamelContext camelContext = new DefaultCamelContext();
			System.out.println("Starting context...");
			try {
				final Endpoint serverControl = (Endpoint)ctx.lookup("vm:ServerControl"); 	
				camelContext.addRoutes(new RouteBuilder(){

					@Override
					public void configure() throws Exception {
						from("direct:CASA_Server").process(new CSMessageConverter()).to(serverControl).process(new CSMessageConverter());
//						from("direct:FactEntry").process(new EntitySerializer()).to((Endpoint)(new InitialContext().lookup("vm:ServerFactEntry")));	
					}});
				camelContext.start();

				System.out.println("Context started...");
				System.out.println("CamelContextID: "+camelContext.getName());
				ctx.rebind("OSMContext", camelContext);
				System.out.println("Context registered: "+ ctx.lookup("OSMContext"));
				this.camelContext = (CamelContext) ctx.lookup("OSMContext");
			} catch (Exception e) {
				e.printStackTrace(); 

			}	
		}

		serverProducer = this.camelContext.createProducerTemplate();
	}
	
	@WebMethod(operationName="getService") 
//	@WebResult(name = "getGUIResult") 
	public Service getService(String proxyTitle, Object[] params) {
		log.info("Requested Proxy: "+ proxyTitle);
		log.info("Requested Params: "+ params.length);
		if (proxys.containsKey(proxyTitle)){
			ServiceProxy sp = proxys.get(proxyTitle);
			if (params.length == 0) {
				params = sp.getParams();
				log.info("Using default params");
			}	
				CSMessage msg = new CSMessage();
				msg.text = "getQueryResult";
				msg.payload.add(sp.getQuery());
				msg.payload.add(params);
				CSMessage importerMsg = (CSMessage) serverProducer.requestBody(msg);

				ArrayList<Object> services = importerMsg.payload;
				Service result;
				try {
					result = sp.getServieType().newInstance();		
					result.setDescription(sp.getDescription());
					result.setTitle(sp.getTitle());
					for (Object o : services){
						if (o instanceof Service){
							result.addSubService((Service)o);
						}
					}
					return result;
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}else{
			log.error("ServiceProxy unknown...");
		}
			return new Service();		
		
	}
}
*/
