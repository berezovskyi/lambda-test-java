package me.berezovskyi.aws.testlambda;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class StreamHandler {

    static DynamoDB dynamoDB = new DynamoDB(Regions.EU_WEST_1);

    public static JsonObject parseJsonObject(String input) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(input);
        if (element.isJsonObject()) {
            JsonObject req = element.getAsJsonObject();
            return req;
        }
        throw new IllegalArgumentException("Malformed JSON");
    }

    public static JsonObject parseJsonObject(InputStream inputStream) {
        JsonParser parser = new JsonParser();
        InputStreamReader reader = new InputStreamReader(inputStream);
        JsonElement element = parser.parse(reader);
        if (element.isJsonObject()) {
            JsonObject req = element.getAsJsonObject();
            return req;
        }
        throw new IllegalArgumentException("Malformed JSON");
    }


    public static void handler(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        String input = IOUtils.toString(inputStream);
        context.getLogger().log("Input: " + input);
        IOUtils.write(input, outputStream);

        JsonObject req = parseJsonObject(input);
        String operation = req.get("operation").getAsString();
        JsonObject payload = getPayload(req);
        String tableName = req.get("tableName").getAsString();
        if (operation.equalsIgnoreCase("create")) {
            Item item = buildItem(payload);
            putItem(tableName, item);
        }
    }

    public static JsonObject getPayload(JsonObject req) {
        return req.getAsJsonObject("payload").getAsJsonObject("Item");
    }

    public static Item buildItem(JsonObject payload) {
        Item item = Item.fromJSON(payload.toString());
        return item;
    }

    private static void putItem(String tableName, Item item) {
        Table table = dynamoDB.getTable(tableName);
        table.putItem(item);
    }
}

