package me.imlukas.listeners;

import me.imlukas.slashcommands.SlashCommandManager;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildJoinReadyListener extends ListenerAdapter {

    private final SlashCommandManager slashCommandManager = new SlashCommandManager();
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        slashCommandManager.init(event.getGuild());
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        slashCommandManager.init(event.getGuild());
    }
}
