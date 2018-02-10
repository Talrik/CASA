package de.lehsten.casa.utilities.communication;

import java.util.List;
import javax.annotation.PreDestroy;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.PollingConsumer;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelMessenger
  extends RouteBuilder
  implements CASAMessenger
{
  InitialContext ctx;
  ProducerTemplate producer;
  PollingConsumer consumer;
  String owner;
  String layer;
  Endpoint receiver;
  Endpoint sender;
  private CamelContext camelContext;
  private static final Logger log = LoggerFactory.getLogger(CamelMessenger.class);
  
  public CamelMessenger()
  {
    startContext();
  }
  
  public CamelMessenger(String layer, String owner, String receiver)
  {
    log.info("Startup for " + owner);
    this.layer = layer;
    startContext();
    setOwner(owner);
    setReceiver(receiver);
  }
  
  private void startContext()
  {
    try
    {
      log.debug("Lookup context...");
      this.ctx = new InitialContext();
      this.camelContext = ((CamelContext)this.ctx.lookup(this.layer + "Context"));
      log.debug("Context found: " + this.camelContext);
    }
    catch (NamingException e1)
    {
      log.error(e1.getMessage());
    }
    if (this.camelContext == null)
    {
      log.debug("No Context found - defining new context...");
      DefaultCamelContext camelContext = new DefaultCamelContext();
      log.debug("Starting context...");
      try
      {
        camelContext.start();
        
        log.debug("Context started...");
        log.debug("CamelContextID: " + camelContext.getName());
        this.ctx.rebind(this.layer + "Context", camelContext);
        log.debug(this.ctx.lookup(this.layer + "Context").toString());
        this.camelContext = ((CamelContext)this.ctx.lookup(this.layer + "Context"));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    this.producer = this.camelContext.createProducerTemplate();
    log.info(this.producer + " created by " + this.owner);
  }
  
  @PreDestroy
  public void stopMessenger()
  {
    try
    {
      this.camelContext.stop();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    log.debug("Context stopped...");
  }
  
  public List<Object> receiveObjects()
  {
    return null;
  }
  
  public void receive()
  {
    log.debug(this.owner + " received message");
  }
  
  public void receive(String value)
  {
    log.debug(this.owner + " received message " + value);
  }
  
  public void send(Object o)
  {
    if (this.producer == null) {
      log.debug(this.owner + " has no producer.");
    } else if (this.receiver == null) {
      log.debug(this.owner + " has no receiver.");
    } else {
      this.producer.sendBody(this.receiver, o);
    }
  }
  
  public void sendText(String arg0)
  {
    if (this.producer == null) {
      log.debug(this.owner + " has no producer.");
    } else if (this.receiver == null) {
      log.debug(this.owner + " has no receiver.");
    } else {
      this.producer.sendBody(this.receiver, arg0);
    }
  }
  
  public void setOwner(String owner)
  {
    try
    {
      this.owner = owner;
      this.ctx.rebind(owner, this);
      this.sender = this.camelContext.getEndpoint("direct:" + owner);
      log.info(this.camelContext.hasEndpoint(new StringBuilder("direct:").append(owner).toString()) + " was registered.");
      this.camelContext.addRouteDefinition((RouteDefinition)from(this.sender).to("bean:" + owner + "?method=receive"));
      log.info(owner + " was created. " + this.sender.getEndpointUri());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public void setLocalReceiver(String receiver)
  {
    String receiverURI = "direct:" + receiver;
    setReceiver(receiverURI);
  }
  
  public void setRemoteReceiver(String receiver)
  {
    String receiverURI = "vm:" + receiver;
    setReceiver(receiverURI);
  }
  
  public void configure()
    throws Exception
  {}
  
  public void setReceiver(String receiver)
  {
    String receiverURI = receiver;
    this.receiver = this.camelContext.hasEndpoint(receiverURI);
    if (this.receiver == null) {
      log.error(this.owner + " was not able to find " + receiverURI);
    } else {
      log.debug(this.owner + " has found it's receiver " + receiverURI);
    }
  }
  
  public Object request(Object o)
  {
    if (this.receiver != null) {
      return this.producer.requestBody(this.receiver, o);
    }
    return null;
  }
}

