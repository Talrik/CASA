package de.lehsten.casa.utilities.communication.serializing;

import de.lehsten.casa.contextserver.types.Entity;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class EntitySerializer
  implements Processor
{
  public void process(Exchange exchange)
    throws Exception
  {
    Entity e = (Entity)exchange.getIn().getBody();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(e);
    oos.close();
    byte[] content = baos.toByteArray();
    exchange.getOut().setBody(content);
  }
}