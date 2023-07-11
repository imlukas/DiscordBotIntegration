package dev.imlukas.listeners;

import dev.imlukas.VeruxBot;
import dev.imlukas.slashcommands.SlashCommandManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SlashCommandListener extends ListenerAdapter {
    private final SlashCommandManager slashCommandManager;
    public SlashCommandListener(VeruxBot main) {
        slashCommandManager = main.getSlashCommandManager();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        slashCommandManager.handle(event);
    }
}
