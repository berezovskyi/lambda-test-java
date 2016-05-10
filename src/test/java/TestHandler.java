import com.amazonaws.services.dynamodbv2.document.Item;
import com.google.gson.JsonObject;
import me.berezovskyi.aws.testlambda.StreamHandler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestHandler {

    String jsonString = "{\n" +
            "  \"operation\": \"create\",\n" +
            "  \"tableName\": \"evts-at-rest-python\",\n" +
            "  \"payload\": {\n" +
            "    \"Item\": {\n" +
            "      \"name\": \"FTW\",\n" +
            "      \"count\": \"1\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    @Test
    public void json_is_parsed() throws Exception {
        JsonObject jsonObject = StreamHandler.parseJsonObject(jsonString);

        String jsonElement = jsonObject.get("operation").getAsString();

        assertEquals("create", jsonElement);
    }

    @Test
    public void item_is_built() throws Exception {
        JsonObject jsonObject = StreamHandler.parseJsonObject(jsonString);
        Item item = StreamHandler.buildItem(StreamHandler.getPayload(jsonObject));

        String itemString = item.getString("name");

        assertEquals("FTW", itemString);
    }
}
