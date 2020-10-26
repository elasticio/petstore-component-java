package io.elastic.petstore.providers;

import io.elastic.api.DynamicMetadataProvider;
import io.elastic.api.JSON;
import org.apache.commons.io.IOUtils;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.io.InputStream;

public class PetMetadataProvider implements DynamicMetadataProvider {

    @Override
    public JsonObject getMetaModel(JsonObject configuration) {
        final JsonObject in = getSchemaByFileName("/createPet.in.json");
        final JsonObject out = getSchemaByFileName("/createPet.out.json");

        return Json.createObjectBuilder()
                .add("in", in)
                .add("out", out)
                .build();
    }


    private static JsonObject getSchemaByFileName(final String name) {

        final InputStream stream = PetMetadataProvider.class.getResourceAsStream(name);

        try {
            return JSON.parseObject(IOUtils.toString(stream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
