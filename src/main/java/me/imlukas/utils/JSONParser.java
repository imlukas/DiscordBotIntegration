package me.imlukas.utils;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class JSONParser {

    public static JSONObject getJsonObject(URL url) {
        String json = null;
        try {
            json = IOUtils.toString(url, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new JSONObject(json);
    }
}
