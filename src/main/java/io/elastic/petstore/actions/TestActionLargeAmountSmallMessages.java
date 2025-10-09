package io.elastic.petstore.actions;

import io.elastic.api.ExecutionParameters;
import io.elastic.api.Function;
import io.elastic.api.Message;
import jakarta.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestActionLargeAmountSmallMessages implements Function {


    protected static final Logger LOG = LoggerFactory.getLogger(TestActionLargeAmountSmallMessages.class);

    @Override
    public void execute(final ExecutionParameters parameters) {
        try {
            final JsonObject configuration = parameters.getConfiguration();
            final JsonString numberOfMessages = configuration.getJsonString("numberOfMessages");
            for (int i = 0; i < Integer.parseInt(numberOfMessages.getString()); i++){
                final Message data = new Message.Builder().body(Json.createObjectBuilder().add("foo", "bar").build()).build();
                parameters.getEventEmitter().emitData(data);
            }
        } catch (Exception e) {
            LOG.error("An error occurred", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}