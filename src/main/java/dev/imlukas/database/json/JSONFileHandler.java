package dev.imlukas.database.json;

import com.google.gson.*;
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
        CompletableFuture.runAsync(() -> {
            parseJSON(guild).thenAccept((jsonObject) -> {
                if (jsonObject != null) {
                    return;
                }

                JSONObject index = new JSONObject();

                JSONObject serverInfo = new JSONObject();
                serverInfo.put("guildName", guild.getName());
                serverInfo.put("guildId", guild.getId());
                serverInfo.put("members", guild.getMembers().size());

                JSONObject serverPreferences = new JSONObject();
                serverPreferences.put("ticketChannel", "tickets");
                serverPreferences.put("ticketTitle", "Ticket System");
                serverPreferences.put("ticketDescription", "Click on support to create a text support channel");
                serverPreferences.put("welcomeChannel", "welcome");
                serverPreferences.put("leaveChannel", "leaves");

                index.put("information", serverInfo);
                index.put("preferences", serverPreferences);

                try (FileWriter file = new FileWriter(getTable(guild) + ".json")) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    gson.toJson(index, file);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }


    public CompletableFuture<JsonObject> parseJSON(Guild guild) {
        return CompletableFuture.supplyAsync(() -> {

            try (FileReader reader = new FileReader(getTable(guild) + ".json")) {
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                return jsonObject;
            } catch (IOException e) {
                return null;
            }
        });
    }

    public CompletableFuture<JsonObject> getJSONSection(Guild guild, String section) {
        return parseJSON(guild).thenApply(jsonObject -> {
            if (jsonObject != null) {
                return jsonObject.get(section).getAsJsonObject();
            }
            return null;
        });
    }

    public CompletableFuture<String[]> getPreferencesArray(Guild guild) {
        return getJSONSection(guild, "preferences").thenApply((preferences) ->
                preferences.keySet().toArray(new String[0]));
    }

    public CompletableFuture<String> getString(Guild guild, String key) {
        return getValue(guild, key).thenApply(JsonPrimitive::getAsString);
    }

    public CompletableFuture<Integer> getInt(Guild guild, String key) {
        return getValue(guild, key).thenApply(JsonPrimitive::getAsInt);
    }

    public CompletableFuture<JsonPrimitive> getValue(Guild guild, String key) {
        String section = key.substring(0, key.indexOf("."));
        String value = key.substring(key.indexOf(".") + 1);
        return getJSONSection(guild, section).thenApply((jsonObject) -> jsonObject.get(value).getAsJsonPrimitive());
    }

    public CompletableFuture<Boolean> setValue(Guild guild, String key, String newValue) {
        String section = key.substring(0, key.indexOf("."));
        String value = key.substring(key.indexOf(".") + 1);

        return getJSONSection(guild, section).thenApply((jsonObject) -> {
            if (jsonObject != null) {
                jsonObject.addProperty(value, newValue);
            }

            return parseJSON(guild).thenApply((parsedJson) -> {
                try (FileWriter file = new FileWriter(getTable(guild) + ".json")) {

                    if (parsedJson == null) {
                        System.out.println("Failed to parse JSON");
                        return false;
                    }

                    parsedJson.add(section, jsonObject);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    gson.toJson(parsedJson, file);
                    return true;

                } catch (IOException e) {
                    return false;
                }
            }).join();
        });
    }

    public void resetPreferences(Guild guild) {
        // reset all preferences in the JSON file
    }

    public void resetAll(Guild guild) {
        // Delete JSON and create a new one

        CompletableFuture.runAsync(() -> {
            File file = new File(getTable(guild) + ".json");
            file.delete();

            createJSON(guild);
        });
    }

    public void resetValue(Guild guild, String key) {
        // reset a value in the JSON file
    }
}
