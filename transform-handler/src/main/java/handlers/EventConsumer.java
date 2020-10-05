package handlers;

import static nva.commons.utils.attempt.Try.attempt;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import nva.commons.utils.IoUtils;
import nva.commons.utils.JsonUtils;
import nva.commons.utils.attempt.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class EventConsumer implements RequestStreamHandler {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    public static final String PARSING_INPUT_ERROR_LOG_MESSAGE = "Failure parsing string: ";
    public static final String SAMPLE_OUTPUT = "HelloWorld";

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        String inputString = IoUtils.streamToString(input);

        EventBridgeEvent<String> event = parseEvent(inputString);
        var outputObject=processInput(event);

    }

    private String processInput(EventBridgeEvent<String> event) {
        logger.info("We are processing an event now!!!");
        logger.info(event.toString());
        return SAMPLE_OUTPUT;
    }

    private EventBridgeEvent<String> parseEvent(String inputString) {
        TypeReference<EventBridgeEvent<String>> typeReference = new TypeReference<>() {};
        return attempt(()->JsonUtils.objectMapper.readValue(inputString,typeReference))
            .orElseThrow(fail->handleParsingException(inputString,fail));
    }

    private RuntimeException handleParsingException(String inputString,Failure<EventBridgeEvent<String>> fail) {
         logger.error(PARSING_INPUT_ERROR_LOG_MESSAGE +inputString);
         throw new RuntimeException(fail.getException());
    }
}
