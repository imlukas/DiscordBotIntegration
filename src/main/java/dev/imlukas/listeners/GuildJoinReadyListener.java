package dev.imlukas.listeners;

import dev.imlukas.VeruxBot;
import dev.imlukas.database.json.JSONFileHandler;
import dev.imlukas.util.command.CommandType;
import dev.imlukas.util.command.SlashCommandManager;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildJoinReadyListener extends ListenerAdapter {
    private final VeruxBot main;
    private final SlashCommandManager slashCommandManager;
    private final JSONFileHandler jsonFileHandler;
    public GuildJoinReadyListener(VeruxBot main) {
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
