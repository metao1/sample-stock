package sample.metao.com.sampleapp.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sample.metao.com.sampleapp.models.Item;

import java.util.ArrayList;

/**
 * Created by metao on 3/8/2017.
 */
public class Util {

    public static Item parseData(String data) throws JSONException{
        Item item = new Item();
        JSONObject jsonObject = new JSONObject(data);
        if (jsonObject.has("face")) {
            item.setFace(jsonObject.getString("face"));
        }
        if (jsonObject.has("type")) {
            item.setType(jsonObject.getString("type"));
        }
        if (jsonObject.has("id")) {
            item.setId(jsonObject.getString("id"));
        }
        if (jsonObject.has("face")) {
            item.setPrice(jsonObject.getInt("price"));
        }
        if (jsonObject.has("stock")) {
            item.setStock(jsonObject.getInt("stock"));
        }
        if (jsonObject.has("size")) {
            item.setSize(jsonObject.getInt("size"));
        }
        if (jsonObject.has("tags")) {
            ArrayList<String> tags = new ArrayList<>();
            JSONArray tagsArray = jsonObject.getJSONArray("tags");
            for (int i = 0; i < tagsArray.length(); i++) {
                tags.add((String) tagsArray.get(i));
            }
            item.setTags(tags);
        }
        return item;
    }
}
