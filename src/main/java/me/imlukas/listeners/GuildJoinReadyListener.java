package me.imlukas.listeners;

import me.imlukas.Bot;
import me.imlukas.localdatabase.data.GuildPreferences;
import me.imlukas.localdatabase.json.JSONFileHandler;
import me.imlukas.slashcommands.CommandType;
import me.imlukas.slashcommands.SlashCommandManager;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildJoinReadyListener extends ListenerAdapter {
    private final Bot main;
    private final SlashCommandManager slashCommandManager;
    private final JSONFileHandler jsonFileHandler;
    public GuildJoinReadyListener(Bot main) {
        this.main = main;
        this.jsonFileHandler = main.getJsonFileHandler();
        slashCommandManager = main.getSlashCommandManager();
    }


    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        slashCommandManager.init(event.getGuild(), CommandType.GUILD);
        jsonFileHandler.createJSON(event.getGuild());

    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        slashCommandManager.init(event.getGuild(), CommandType.GUILD);
        jsonFileHandler.createJSON(event.getGuild());
    }
}
