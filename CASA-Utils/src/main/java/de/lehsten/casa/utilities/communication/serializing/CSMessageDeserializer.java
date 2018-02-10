package de.lehsten.casa.utilities.communication.serializing;

import de.lehsten.casa.contextserver.types.xml.CSMessage;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class CSMessageDeserializer
  implements Processor
{
  public void process(Exchange exchange)
    throws Exception
  {
    Object o = exchange.getIn().getBody();
    byte[] content = (byte[])o;
    ByteArrayInputStream baos = new ByteArrayInputStream(content);
    ObjectInputStream ois = new ObjectInputStream(baos);
    o = ois.readObject();
    ois.close();
    CSMessage e = (CSMessage)o;
    exchange.getOut().setBody(e);
  }
}

