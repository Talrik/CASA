package de.lehsten.casa.utilities.communication.serializing;

import de.lehsten.casa.contextserver.types.xml.CSMessage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class CSMessageConverter
  implements Processor
{
  public void process(Exchange exchange)
    throws Exception
  {
    if ((exchange.getIn().getBody() instanceof CSMessage))
    {
      CSMessage e = (CSMessage)exchange.getIn().getBody();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(e);
      oos.close();
      byte[] content = baos.toByteArray();
      exchange.getOut().setBody(content);
    }
    else if ((exchange.getIn().getBody() instanceof byte[]))
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
}
