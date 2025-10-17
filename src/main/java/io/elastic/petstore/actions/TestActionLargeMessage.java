package io.elastic.petstore.actions;

import io.elastic.api.ExecutionParameters;
import io.elastic.api.Function;
import io.elastic.api.Message;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonException;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParser.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class TestActionLargeMessage implements Function {

    protected static final Logger LOG = LoggerFactory.getLogger(TestActionLargeMessage.class);

    @Override
    public void execute(final ExecutionParameters parameters) {
        final String resourcePath = "employees_5MB.json"; // Changed to resource path
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            LOG.info("Attempting to read JSON from resource: {}", resourcePath);

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            final String largeJsonString = sb.toString(); // Read from InputStream using BufferedReader

            LOG.info("Successfully read resource. Content length: {} bytes", largeJsonString.length());

            final JsonObject largeJson = parseJsonString(largeJsonString);

            // Log the size of the JsonObject when serialized back to a string
            String serializedLargeJson = largeJson.toString();
            LOG.info("Serialized JsonObject size: {} bytes", serializedLargeJson.getBytes(StandardCharsets.UTF_8).length);

            LOG.info("Successfully parsed JSON message from resource: {}", resourcePath);

            final Message data = new Message.Builder().body(largeJson).build();
            parameters.getEventEmitter().emitData(data);

        } catch (IOException e) {
            LOG.error("An IOException occurred while reading the resource {}: {}", resourcePath, e.getMessage(), e);
            throw new RuntimeException("Failed to read JSON resource: " + resourcePath, e);
        } catch (JsonException e) { // Catch JsonException for parsing errors
            LOG.error("A JsonException occurred while parsing the JSON from resource {}: {}", resourcePath, e.getMessage(), e);
            throw new RuntimeException("Failed to parse JSON from resource: " + resourcePath, e);
        } catch (Exception e) { // Catch any other unexpected exceptions
            LOG.error("An unexpected error occurred: {}", e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    private JsonObject parseJsonString(String jsonString) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            // Peek at the first event to determine if it's an object or an array
            jakarta.json.stream.JsonParser parser = Json.createParser(new StringReader(jsonString));
            if (parser.hasNext()) {
                jakarta.json.stream.JsonParser.Event event = parser.next();
                if (event == jakarta.json.stream.JsonParser.Event.START_ARRAY) {
                    jakarta.json.JsonArray originalJsonArray = jsonReader.readArray();
                    jakarta.json.JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                    // Add the original array elements twenty-five times to get approximately 50MB
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    originalJsonArray.forEach(arrayBuilder::add);
                    jakarta.json.JsonArray vigintupledJsonArray = arrayBuilder.build();
                    return Json.createObjectBuilder().add("employees", vigintupledJsonArray).build();
                } else if (event == jakarta.json.stream.JsonParser.Event.START_OBJECT) {
                    // It's an object, read it as an object
                    return jsonReader.readObject();
                } else {
                    throw new JsonException("Unexpected JSON event: " + event);
                }
            } else {
                throw new JsonException("Empty JSON string");
            }
        }
    }
}