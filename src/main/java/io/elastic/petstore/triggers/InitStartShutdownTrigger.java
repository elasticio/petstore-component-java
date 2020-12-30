package io.elastic.petstore.triggers;

import io.elastic.api.*;
import io.elastic.petstore.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class InitStartShutdownTrigger implements Function {

    private static final Logger logger = LoggerFactory.getLogger(InitStartShutdownTrigger.class);

    @Override
    public void execute(ExecutionParameters parameters) {
        try {

            URL url = new URL("https://in.eio.ninja/hook/5e95b291dde7de0011ceb849");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String input = "{\"msg\":\"execute\"}";

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

            String output;
            System.out.println("Output from EXECUTE .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        new GetPetsByStatusJaxRs().execute(parameters);
    }


    public  JsonObject startup(final StartupParameters parameters) {
        logger.info("About to start the trigger");

        final JsonObject brownie = getBrownie(1, parameters.getConfiguration().getString("apiKey"));

        try {

            URL url = new URL("https://in.eio.ninja/hook/5e95b291dde7de0011ceb849");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String input = "{\"msg\":\"startup\"}";

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

            String output;
            System.out.println("Output from STARTUP .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return brownie;
    }

    public void init(final InitParameters parameters) {
        logger.info("About to init the trigger");

        try {

            URL url = new URL("https://in.eio.ninja/hook/5e95b291dde7de0011ceb849");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String input = "{\"msg\":\"init\"}";

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

            String output;
            System.out.println("Output from INIT .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }



    public  void shutdown(final ShutdownParameters parameters) {
        logger.info("About to shut down the trigger");
        try {

            URL url = new URL("https://in.eio.ninja/hook/5e95b291dde7de0011ceb849");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String input = "{\"msg\":\"shutdown\"}";

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

            String output;
            System.out.println("Output from SHUTDOWN .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            throw new RuntimeException(e);

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

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
