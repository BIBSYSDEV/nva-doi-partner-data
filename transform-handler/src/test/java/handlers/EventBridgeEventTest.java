package handlers;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.nio.file.Path;
import javax.xml.crypto.Data;
import nva.commons.utils.IoUtils;
import nva.commons.utils.JsonUtils;
import org.junit.jupiter.api.Test;

public class EventBridgeEventTest {

    @Test
    public void instantDeserialized() throws JsonProcessingException {
        String eventString = IoUtils.stringFromResources(Path.of("event.json"));
        TypeReference<EventBridgeEvent<DataciteDoiRequest>> typeref = new TypeReference<>() {};
        var event = JsonUtils.objectMapper.readValue(eventString, typeref);
        assertNotNull(event);
    }
}