package me.berezovskyi.aws.testlambda;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class StreamHandler {
	
	static DynamoDB dynamoDB = new DynamoDB(Regions.EU_WEST_1);
	
	public static void handler(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
    	String input = IOUtils.toString(inputStream);
    	context.getLogger().log("Input: " + input);
    	IOUtils.write(input, outputStream);    
    	
    	JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(input);
        if (element.isJsonObject()) {
            JsonObject req = element.getAsJsonObject();
            String operation = req.get("operation").getAsString();
            String tableName = req.get("tableName").getAsString();  // TODO if exists
            
            Table table = dynamoDB.getTable(tableName);
            
            JsonObject payload = req.get("payload").getAsJsonObject();
            
            if(operation == "create") {
            	table.putItem(Item.fromJSON(payload.toString()));  // how bad can it be? }:)
            }
        }
    }
}
