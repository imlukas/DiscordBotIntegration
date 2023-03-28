package me.imlukas.database.json.json;

import com.google.gson.*;
import me.imlukas.database.json.data.GuildStorage;
import net.dv8tion.jda.api.entities.Guild;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class JSONFileHandler {


    public String getTable(Guild guild) {
        return "server_" + guild.getId();
    }

    public void createJSON(Guild guild) {

        // create a new JSON file for the server

        if (parseJSON(guild) != null) {
            System.out.println("JSON file already exists");
            return;
        }

        JSONObject index = new JSONObject();

        JSONObject serverInfo = new JSONObject();
        serverInfo.put("guildId", guild.getId());
        serverInfo.put("members", guild.getMembers().size());

        JSONObject serverPreferences = new JSONObject();
        serverPreferences.put("ticketChannel", "tickets");
        serverPreferences.put("ticketTitle", "Ticket System");
        serverPreferences.put("ticketDescription", "Click on support to create a text support channel");

        index.put("information", serverInfo);
        index.put("preferences", serverPreferences);

        try (FileWriter file = new FileWriter("src/main/java/me/imlukas/database/json" + File.separator + getTable(guild) + ".json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(index, file);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public JsonObject parseJSON(Guild guild) {
        // get the JSON file for the server
        try (FileReader reader = new FileReader("src/main/java/me/imlukas/database/json" + File.separator + getTable(guild) + ".json")) {
            return JsonParser.parseReader(reader).getAsJsonObject();

        } catch (IOException e) {
            System.out.println("Error parsing JSON file");
            return null;
        }
    }

    public JsonObject getJSONSection(Guild guild, String section) {
        JsonObject jsonObject = parseJSON(guild);

        if (jsonObject == null) {
            System.out.println("Failed to parse JSON");
            return null;
        }

        return jsonObject.get(section).getAsJsonObject();
    }

    public GuildStorage getStorage(Guild guild) {
        // get the preferences class from the JSON file

        JsonObject preferencesParameters = getJSONSection(guild, "preferences");

        Gson gson = new Gson();

        return gson.fromJson(preferencesParameters.getAsString(), GuildStorage.class);
    }


    public String[] getPreferencesArray(Guild guild) {
        JsonObject preferencesParameters = getJSONSection(guild, "preferences");

        return preferencesParameters.keySet().toArray(new String[0]);
    }

    public void updatePreferences(Guild guild, GuildStorage preferences) {
        // update the preferences class in the JSON file

    }


    public CompletableFuture<String> getString(Guild guild, String key) {
        return getValue(guild, key).thenApply(JsonPrimitive::getAsString);
    }

    public CompletableFuture<Integer> getInt(Guild guild, String key) {
        return getValue(guild, key).thenApply(JsonPrimitive::getAsInt);
    }

    public CompletableFuture<JsonPrimitive> getValue(Guild guild, String key) {

        return CompletableFuture.supplyAsync(() -> {
            String section = key.substring(0, key.indexOf("."));
            String value = key.substring(key.indexOf(".") + 1);

            JsonObject sectionObject = getJSONSection(guild, section);

            return sectionObject.get(value).getAsJsonPrimitive();
        }).exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });
    }

    public boolean setValue(Guild guild, String key, String newValue) {
        String section = key.substring(0, key.indexOf("."));
        String value = key.substring(key.indexOf(".") + 1);

        JsonObject sectionObject = getJSONSection(guild, section);

        if (sectionObject.get(value) == null) {
            System.out.println("That preference does not exist");
            return false;
        }

        sectionObject.addProperty(value, newValue);

        JsonObject json = parseJSON(guild);

        try (FileWriter file = new FileWriter("src/main/java/me/imlukas/database/json" + File.separator + getTable(guild) + ".json")) {

            if (json == null) {
                System.out.println("Failed to parse JSON");
                return false;
            }

            json.add(section, sectionObject);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(json, file);
            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public void resetPreferences(Guild guild) {
        // reset all preferences in the JSON file
    }

    public void resetAll(Guild guild) {
        // reset all values in the JSON file
    }

    public void resetValue(Guild guild, String key) {
        // reset a value in the JSON file
    }


}
