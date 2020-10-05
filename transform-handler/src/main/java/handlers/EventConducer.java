package handlers;

import com.amazonaws.services.eventbridge.AmazonEventBridge;
import com.amazonaws.services.eventbridge.AmazonEventBridgeClientBuilder;
import com.amazonaws.services.eventbridge.model.PutEventsRequest;
import com.amazonaws.services.eventbridge.model.PutEventsRequestEntry;
import com.amazonaws.services.eventbridge.model.PutEventsResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import nva.commons.utils.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventConducer implements RequestStreamHandler {

    private static final Logger logger = LoggerFactory.getLogger(EventConducer.class);
    private final Environment environment;
    private AmazonEventBridge client;

    public EventConducer() {
        this(AmazonEventBridgeClientBuilder.defaultClient(), new Environment());
    }

    public EventConducer(AmazonEventBridge client, Environment environment) {
        this.client = client;
        this.environment = environment;
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        DataciteDoiRequest dataciteDoiRequest = newEventContext();

        var requestEntry = new PutEventsRequestEntry()
            .withEventBusName(getEventBus())
            .withDetail(dataciteDoiRequest.toString())
            .withDetailType(dataciteDoiRequest.getType())
            .withSource("Oresis Conducer");
        PutEventsRequest request = new PutEventsRequest().withEntries(requestEntry);
        PutEventsResult result = this.client.putEvents(request);
        logger.info("Conducer result:" + result.toString());
    }

    public String getEventBus() {
        return environment.readEnv("EVENT_BUS");
    }

    private DataciteDoiRequest newEventContext() {
        return DataciteDoiRequest.newBuilder()
            .withExistingDoi(URI.create("http://somedoi.org"))
            .withPublicationId(URI.create("https://somepublication.com"))
            .withXml("Somexml")
            .withType("MyType")
            .build();
    }
}
