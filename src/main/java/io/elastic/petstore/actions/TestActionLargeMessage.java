package io.elastic.petstore.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class TestActionLargeMessage implements Function {

    // Target size in bytes: 10 MB = 10 * 1024 * 1024 bytes
    private static final long MIN_BYTES = 10 * 1024 * 1024;
    // 15 MB = 15 * 1024 * 1024 bytes
    private static final long MAX_BYTES = 15 * 1024 * 1024;
    // Size of the random string in a single element (approx. 20 KB)
    private static final int CHUNK_SIZE_CHARS = 20000;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final Random random = new Random();

    protected static final Logger LOG = LoggerFactory.getLogger(TestActionLargeMessage.class);

    @Override
    public void execute(final ExecutionParameters parameters) {
        try {
            final String largeJsonString = this.generateLargeRandomJson();
            final JsonObject largeJson = parseJsonString(largeJsonString);

            LOG.info("Successfully generated large JSON message");

            final Message data = new Message.Builder().body(largeJson).build();
            parameters.getEventEmitter().emitData(data);

        } catch (IOException e) {
            LOG.error("An error occurred during JSON serialization/deserialization", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private JsonObject parseJsonString(String jsonString) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            return jsonReader.readObject();
        }
    }

    private static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public String generateLargeRandomJson() throws IOException {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("info", "Random JSON for size testing.");

        ArrayNode dataArray = rootNode.putArray("randomData");
        long currentSize = 0;
        int itemCounter = 0;

        LOG.info("Starting generation... Target size: 10-15 MB.");

        while (currentSize < MIN_BYTES) {
            ObjectNode dataBlock = mapper.createObjectNode();
            String randomContent = generateRandomString(CHUNK_SIZE_CHARS);

            dataBlock.put("id", itemCounter++);
            dataBlock.put("data_field", randomContent);

            dataArray.add(dataBlock);

            currentSize = mapper.writeValueAsString(rootNode).getBytes(StandardCharsets.UTF_8).length;

            if (itemCounter % 10 == 0) {
                LOG.info(String.format("Progress: %d items, current size: %.2f MB", itemCounter, (double) currentSize / (1024 * 1024)));
            }

            if (currentSize > MAX_BYTES && itemCounter > 1) {
                dataArray.remove(dataArray.size() - 1);
                currentSize = mapper.writeValueAsString(rootNode).getBytes(StandardCharsets.UTF_8).length;
                break;
            }
        }

        LOG.info("---");
        LOG.info(String.format("Generation finished."));
        LOG.info(String.format("Final number of elements in the array: %d", dataArray.size()));
        LOG.info(String.format("Final JSON size: %.2f MB", (double) currentSize / (1024 * 1024)));
        LOG.info("---");

        return mapper.writeValueAsString(rootNode);
    }
}