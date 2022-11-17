package me.imlukas.listeners;

import me.imlukas.Bot;
import me.imlukas.slashcommands.SlashCommandManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SlashCommandListener extends ListenerAdapter {
    private final SlashCommandManager slashCommandManager;
    public SlashCommandListener(Bot main) {
        slashCommandManager = main.getSlashCommandManager();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        slashCommandManager.handle(event);
    }
}
