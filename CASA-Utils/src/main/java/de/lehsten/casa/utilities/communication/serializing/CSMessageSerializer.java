package de.lehsten.casa.utilities.communication.serializing;

import de.lehsten.casa.contextserver.types.xml.CSMessage;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class CSMessageSerializer
  implements Processor
{
  public void process(Exchange exchange)
    throws Exception
  {
    CSMessage e = (CSMessage)exchange.getIn().getBody();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(e);
    oos.close();
    byte[] content = baos.toByteArray();
    exchange.getOut().setBody(content);
  }
}