package me.imlukas.localdatabase.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import me.imlukas.localdatabase.data.GuildPreferences;
import net.dv8tion.jda.api.entities.Guild;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class JSONFileHandler {


    public String getTable(Guild guild){
        return "server_" + guild.getId();
    }

    public void createJSON(Guild guild) {

        // create a new JSON file for the server

        if (parseJSON(guild) != null) {
            return;
        }

        JSONObject index = new JSONObject();

        JSONObject serverInfo = new JSONObject();
        serverInfo.put("guildId", guild.getId());
        serverInfo.put("members", guild.getMembers().size());


        JSONObject serverPreferences = new JSONObject();

        index.put("Information", serverInfo);
        index.put("Preferences", serverPreferences);

        try (FileWriter file = new FileWriter("src/main/java/me/imlukas/localdatabase" + File.separator + getTable(guild) + ".json")) {


            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(index, file);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public JSONObject parseJSON(Guild guild){
        // get the JSON file for the server
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("src/main/java/me/imlukas/localdatabase" + File.separator + getTable(guild) + ".json")){

            Object object = parser.parse(reader);

            return (JSONObject) object;

        } catch (IOException | ParseException e) {
            System.out.println("Error parsing JSON file");
            return null;
        }
    }

    public JSONObject getJSONSection(Guild guild, String section)  {
        JSONObject array = parseJSON(guild);

        if (array == null) {
            System.out.println("Failed to parse JSON");
            return null;
        }

        return (JSONObject) array.get(section);
    }

    public GuildPreferences getPreferences(Guild guild){
        // get the preferences class from the JSON file

        JSONObject preferencesParameters = getJSONSection(guild, "Preferences");

        Gson gson = new Gson();

        return gson.fromJson(preferencesParameters.toJSONString(), GuildPreferences.class);
    }

    public void updatePreferences(Guild guild, GuildPreferences preferences){
        // update the preferences class in the JSON file
        JSONObject index = parseJSON(guild);
        JSONObject preferencesObject = getJSONSection(guild, "Preferences");

        Gson gson = new Gson();

        JsonElement json = gson.toJsonTree(preferences);
        // json.getAsJsonObject().add("Preferences");

        System.out.println(json);

        preferencesObject.put("Preferences", json);

        try (FileWriter file = new FileWriter("src/main/java/me/imlukas/localdatabase" + File.separator + getTable(guild) + ".json")) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public Object getValue(Guild guild, String key){
        // get a value from the JSON file
        return null;
    }


    public void updateValue(Guild guild, String key, String value){
        // update a value in the JSON file
    }


    public void resetPreferences(Guild guild){
        // reset all preferences in the JSON file
    }

    public void resetAll(Guild guild){
        // reset all values in the JSON file
    }

    public void resetValue(Guild guild, String key){
        // reset a value in the JSON file
    }




}
