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
import java.util.UUID;
import nva.commons.utils.Environment;

public class EventConducer implements RequestStreamHandler {

    private AmazonEventBridge client;
    private Environment environment;
    private String eventBus;

    public  EventConducer(){
        this(AmazonEventBridgeClientBuilder.defaultClient(), new Environment());
    }

    public EventConducer(AmazonEventBridge client, Environment environment){
        this.client =client;
        this.environment = environment;
    }



    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        DataciteDoiRequest dataciteDoiRequest = newEventContext();

        var requestEntry = new PutEventsRequestEntry()
            .withEventBusName(eventBus)
            .withDetail(dataciteDoiRequest.toString())
            .withDetailType(dataciteDoiRequest.getType())
            .withSource("Oresis Conducer");
        PutEventsRequest request = new PutEventsRequest().withEntries(requestEntry);
        PutEventsResult result = this.client.putEvents(request);

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
