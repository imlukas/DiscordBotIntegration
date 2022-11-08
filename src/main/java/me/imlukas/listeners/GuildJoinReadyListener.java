package me.imlukas.listeners;

import me.imlukas.Bot;
import me.imlukas.slashcommands.CommandType;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.SlashCommandManager;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildJoinReadyListener extends ListenerAdapter {
    private final SlashCommandManager slashCommandManager;
    public GuildJoinReadyListener(Bot main) {
        slashCommandManager = main.getSlashCommandManager();
    }


    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        slashCommandManager.init(event.getGuild(), CommandType.GUILD);
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        slashCommandManager.init(event.getGuild(), CommandType.GUILD);
    }
}
