package de.lehsten.casa.contextserver.communication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import de.lehsten.casa.contextserver.types.xml.CSMessage;

public class CSMessageConverter implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		if (exchange.getIn().getBody() instanceof CSMessage){
			CSMessage e = (CSMessage) exchange.getIn().getBody();
			System.out.println("Converting CSMessage ("+e.text+") to Byte[]");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(e);
			oos.close();
			byte[] content = baos.toByteArray();
			exchange.getOut().setBody(content);
		}else if (exchange.getIn().getBody() instanceof byte[]){
			Object o = exchange.getIn().getBody();
			byte[] content = (byte[]) o;
			ByteArrayInputStream baos = new ByteArrayInputStream(content);
			ObjectInputStream ois = new ObjectInputStream(baos);
			o = ois.readObject();
			ois.close();
			CSMessage e = (CSMessage)o;
			System.out.println("Converting Byte[] to CSMessage ("+ e.text+")");
			exchange.getOut().setBody(e);
		}else {
			System.out.println("ERROR->");
			System.out.println(exchange.getIn());
			System.out.println(exchange.getIn().getBody());
			System.out.println("ERROR->");
		}
	}

}
