package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import nva.commons.utils.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventHandler implements RequestStreamHandler {

    private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        String inputString = IoUtils.streamToString(input);
        logger.info(inputString);
    }
}
