package io.elastic.petstore;


import io.elastic.api.CredentialsVerifier;
import io.elastic.api.InvalidCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;

/**
 * Implementation of {@link CredentialsVerifier} used to verfy that credentials provide by user
 * are valid. This is accomplished by sending a simple request to the Petstore API.
 * In case of a successful response (HTTP 200) we assume credentials are valid. Otherweise invalid.
 */
public class ApiKeyVerifier implements CredentialsVerifier {

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyVerifier.class);

    @Override
    public void verify(final JsonObject configuration) throws InvalidCredentialsException {
        logger.info("About to verify the provided API key by retrieving the user");
        logger.info("======="+configuration.getString("apiKey"));
        logger.info("=======2"+configuration.getString("apiKeyTwo"));
        logger.info("=======3"+configuration.getString("apiKeyThree"));
     if (configuration.getString("apiKey").equals("one")) {
         throw new InvalidCredentialsException("Failed to verify credentials because of blablab");
     }
     if (configuration.getString("apiKey").equals("two")) {
         return;
     }
     if (configuration.getString("apiKey").equals("four")) {
         throw new RuntimeException("Failed to verify credentials because of ti blablablbal");
     }
            logger.info("User {} successfully retrieved. Credentials are valid");
    }
}
