package io.elastic.petstore.actions;

import io.elastic.api.ExecutionParameters;
import io.elastic.api.Function;
import io.elastic.api.Message;
import io.elastic.petstore.HttpClientUtils;
import jakarta.json.JsonObjectBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.json.JsonObject;
import jakarta.json.JsonString;

import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.Properties;

/**
 * Action to create a pet.
 */
public class CreatePet implements Function {
    private static final Logger logger = LoggerFactory.getLogger(CreatePet.class);

    /**
     * Executes the actions's logic by sending a request to the Petstore API and emitting response to the platform.
     *
     * @param parameters execution parameters
     */
    @Override
    public void execute(final ExecutionParameters parameters) {
        logger.info("About to create new pet");

        logger.info("--- Classpath Investigation Results ---");

        // 1. Print the full Java classpath
        logger.info(" --- [1] Java Classpath (java.class.path) ---");
        String classPath = System.getProperty("java.class.path");
        if (classPath != null && !classPath.isEmpty()) {
            String[] paths = classPath.split(System.getProperty("path.separator"));
            for (String path : paths) {
                logger.info(path);
            }
        } else {
            logger.info("Classpath is not set or is empty.");
        }

        // 2. Locate the source of the problematic class
        logger.info(" --- [2] Location of Guava's Stopwatch class ---");
        printClassLocation(com.google.common.base.Stopwatch.class);

        // 3. Locate the source of the class that uses it
        logger.info(" --- [3] Location of Guice's InternalInjectorCreator class ---");
        printClassLocation(com.google.inject.internal.InternalInjectorCreator.class);

        // 4. Print all system properties
        logger.info(" --- [4] System Properties ---");
        Properties properties = System.getProperties();
        for (Object key : properties.keySet()) {
            logger.info(key + ": " + properties.get(key));
        }

        // 5. Print all environment variables
        logger.info(" --- [5] Environment Variables ---");
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            logger.info(envName + ": " + env.get(envName));
        }

        logger.info(" --- End of Investigation ---");

        logger.info("Pet successfully created");
    }

    private static void printClassLocation(Class<?> clazz) {
        try {
            ProtectionDomain protectionDomain = clazz.getProtectionDomain();
            CodeSource codeSource = protectionDomain.getCodeSource();
            if (codeSource != null) {
                URL location = codeSource.getLocation();
                logger.info("Class '" + clazz.getName() + "' is loaded from: " + location.toExternalForm());
            } else {
                logger.info("Could not determine the source location for class '" + clazz.getName() + "'. It might be a core Java class.");
            }
        } catch (Exception e) {
            logger.info("An error occurred while trying to find the location of class '" + clazz.getName() + "': " + e.getMessage());
            e.printStackTrace();
        }
    }
}
