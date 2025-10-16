package io.elastic.petstore.actions;

import io.elastic.api.ExecutionParameters;
import io.elastic.api.Function;
import io.elastic.api.Message;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestActionLargeMessage implements Function {

    protected static final Logger LOG = LoggerFactory.getLogger(TestActionLargeMessage.class);

    @Override
    public void execute(final ExecutionParameters parameters) {
        try {
            final String jsonFilePath = "/Users/pavlovoropaiev/work/elastic/coding/petstore-component-java/lib/employees_5MB.json";
            final String largeJsonString = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            final JsonObject largeJson = parseJsonString(largeJsonString);

            LOG.info("Successfully loaded JSON message from file: {}", jsonFilePath);

            final Message data = new Message.Builder().body(largeJson).build();
            parameters.getEventEmitter().emitData(data);

        } catch (IOException e) {
            LOG.error("An error occurred during JSON loading or parsing", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private JsonObject parseJsonString(String jsonString) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            return jsonReader.readObject();
        }
    }
}