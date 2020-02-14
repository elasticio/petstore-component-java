package io.elastic.petstore.triggers;

import io.elastic.api.*;
import io.elastic.petstore.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class InitStartShutdownTrigger implements Module {

    private static final Logger logger = LoggerFactory.getLogger(InitStartShutdownTrigger.class);

    @Override
    public void execute(ExecutionParameters parameters) {
        new GetPetsByStatusJaxRs().execute(parameters);
    }


    public  JsonObject startup(final StartupParameters parameters) {
        logger.info("About to start the trigger");

        final JsonObject brownie = getBrownie(1, parameters.getConfiguration().getString("apiKey"));

        logger.info("Found {}", brownie);

        logger.info("Trigger successfully started");

        return brownie;
    }

    public void init(final InitParameters parameters) {
        logger.info("About to init the trigger");
        final JsonObject configuration = parameters.getConfiguration();

        final JsonObject brownie = getBrownie(1, parameters.getConfiguration().getString("apiKey"));

        logger.info("Found {}", brownie);

        logger.info("Trigger successfully init");
    }

    public  void shutdown(final ShutdownParameters parameters) {
        logger.info("About to shut down the trigger");

        final JsonObject state = parameters.getState();

        final int id = state.getInt("id");

        final JsonObject brownie = getBrownie(id, parameters.getConfiguration().getString("apiKey"));

        logger.info("Found {}", brownie);

        logger.info("Trigger successfully shut down");
    }

    public static final JsonObject getBrownie(final int id, final String apiKey) {
        return ClientBuilder.newClient()
                .target(Constants.PETSTORE_API_BASE_URL)
                .path("v2/pet/" + id)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(Constants.API_KEY_HEADER, apiKey)
                .get(JsonObject.class);
    }

}
