package me.imlukas.localdatabase.data;

import lombok.Data;
import net.dv8tion.jda.api.entities.Guild;
import org.json.simple.JSONObject;

@Data
public class GuildPreferences {

    private JSONObject preferences;
    private Guild guild;
    private String prefix;


    public GuildPreferences() {
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public void setPreferences(JSONObject preferences) {
        this.preferences = preferences;
    }


}
