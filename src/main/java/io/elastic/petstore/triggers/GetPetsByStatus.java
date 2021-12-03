package io.elastic.petstore.triggers;

import io.elastic.api.ExecutionParameters;
import io.elastic.api.Function;
import io.elastic.api.Message;
import io.elastic.petstore.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;


/**
 * Trigger to get pets by status.
 */
public class GetPetsByStatus implements Function {
    private static final Logger logger = LoggerFactory.getLogger(GetPetsByStatus.class);

    /**
     * Executes the trigger's logic by sending a request to the Petstore API and emitting response to the platform.
     *
     * @param parameters execution parameters
     */
    @Override
    public void execute(final ExecutionParameters parameters) {
        final JsonObject configuration = parameters.getConfiguration();

        final Message message = parameters.getMessage();

        final JsonObject body = message.getBody();
        final JsonObject query = message.getQuery();
        final String method = message.getMethod();
        final String url = message.getUrl();
        final String originalUrl = message.getOriginalUrl();

        logger.info("Entire message: " + message);
        logger.info("body: " + body.toString());
        logger.info("query: " + query.toString());
        logger.info("method: " + method);
        logger.info("url: " + url);
        logger.info("originalUrl: " + originalUrl);

        JsonObject result = Json.createObjectBuilder()
            .add("body", body)
            .add("query", query)
            .add("method", method)
            .add("url", url)
            .add("originalUrl", originalUrl)
            .build();

        final Message data
            = new Message.Builder().body(result).build();
        parameters.getEventEmitter().emitData(data);
    }
}
