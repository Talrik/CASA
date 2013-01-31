package webservices;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import de.lehsten.casa.contextserver.types.xml.CSMessage;

public class BrokerExceptionHandler implements Processor {

	@Override
    public void process(Exchange exchange) throws Exception {
        // the caused by exception is stored in a property on the exchange
        Throwable caused = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
        // here you can do what you want, but Camel regard this exception as handled, and
        // this processor as a failurehandler, so it wont do redeliveries. So this is the
        // end of this route. But if we want to route it somewhere we can just get a
        // producer template and send it.
		CSMessage reply = new CSMessage();
		System.out.println("[BEH]: Exception handled.");
		reply.payload = new ArrayList<Object>();
        // send it to our mock endpoint
        exchange.getContext().createProducerTemplate().send("mock:myerror", exchange);
        exchange.getOut().setBody(reply);
    }

}
