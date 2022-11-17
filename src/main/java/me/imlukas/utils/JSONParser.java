package me.imlukas.utils;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class JSONParser {


    /**
     * Get a json object from a url.
     * @param url the url to get the json object from.
     * @return the json object from the url.
     */
    public static JSONObject getJsonObject(URL url) {
        String json = null;
        try {
            json = IOUtils.toString(url, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
        return new JSONObject(json);
    }
}
