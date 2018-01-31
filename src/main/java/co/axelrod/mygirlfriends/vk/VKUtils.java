package co.axelrod.mygirlfriends.vk;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 31.01.2018.
 */
public class VKUtils {
    public static String getHighResURL(String response, String imageId) {
        JsonElement photosJson = new JsonParser().parse(response).getAsJsonObject().get("response");

        JsonArray photosArray = photosJson.getAsJsonObject().get("items").getAsJsonArray();

        for(JsonElement photo : photosArray) {
            if(photo.getAsJsonObject().get("id").getAsString().equals(imageId)) {
                JsonArray sizes = photo.getAsJsonObject().get("sizes").getAsJsonArray();
                for(JsonElement size : sizes) {
                    if(size.getAsJsonObject().get("type").getAsString().equals("z")) {
                        return size.getAsJsonObject().get("src").getAsString();
                    }
                }
            }
        }

        return null;
    }
}
