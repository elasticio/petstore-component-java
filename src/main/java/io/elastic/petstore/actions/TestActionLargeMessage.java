package io.elastic.petstore.actions;

import io.elastic.api.ExecutionParameters;
import io.elastic.api.Function;
import io.elastic.api.Message;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


public class TestActionLargeMessage implements Function {

    protected static final Logger LOG = LoggerFactory.getLogger(TestActionLargeMessage.class);

    @Override
    public void execute(final ExecutionParameters parameters) {
        final String filePath = "/Users/pavlovoropaiev/work/elastic/coding/petstore-component-java/lib/employees_5MB.json";
        try (InputStream is = Files.newInputStream(Paths.get(filePath))) {
            if (is == null) {
                throw new IOException("File not found: " + filePath);
            }
            LOG.info("Attempting to read JSON from file: {}", filePath);

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            final String largeJsonString = sb.toString(); // Read from InputStream using BufferedReader

            LOG.info("Successfully read file. Content length: {} bytes", largeJsonString.length());

            final JsonObject largeJson = parseJsonString(largeJsonString);

            LOG.info("Successfully parsed JSON message from file: {}", filePath);

            final Message data = new Message.Builder().body(largeJson).build();
            parameters.getEventEmitter().emitData(data);

        } catch (IOException e) {
            LOG.error("An IOException occurred while reading the file {}: {}", filePath, e.getMessage(), e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        } catch (JsonException e) { // Catch JsonException for parsing errors
            LOG.error("A JsonException occurred while parsing the JSON from file {}: {}", filePath, e.getMessage(), e);
            throw new RuntimeException("Failed to parse JSON from file: " + filePath, e);
        } catch (Exception e) { // Catch any other unexpected exceptions
            LOG.error("An unexpected error occurred: {}", e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    private JsonObject parseJsonString(String jsonString) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            return jsonReader.readObject();
        }
    }
}