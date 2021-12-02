package io.elastic.petstore.triggers;

import io.elastic.api.ExecutionParameters;
import io.elastic.api.Function;
import io.elastic.api.Message;
import io.elastic.petstore.HttpClientUtils;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Trigger to get pets by status.
 */
public class TestSailor implements Function {
    private static final Logger logger = LoggerFactory.getLogger(TestSailor.class);

    @Override
    public void execute(final ExecutionParameters parameters) {
        final JsonObject configuration = parameters.getConfiguration();

        final Message message = parameters.getMessage();

        final JsonObject body1 = message.getBody();
        final JsonObject query = message.getQuery();
        final String method = message.getMethod();
        final String url = message.getUrl();
        final String originalUrl = message.getOriginalUrl();

        logger.info("==========================");
        logger.info("body: " + body1.toString());
        logger.info("==========================");
        logger.info("query: " + query.toString());
        logger.info("==========================");
        logger.info("method: " + method);
        logger.info("==========================");
        logger.info("url: " + url);
        logger.info("==========================");
        logger.info("originalUrl: " + originalUrl);
        logger.info("==========================");

    }
}
