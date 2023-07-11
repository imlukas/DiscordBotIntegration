package dev.imlukas.slashcommands.commands.admin.listener;

import dev.imlukas.VeruxBot;
import dev.imlukas.database.json.JSONFileHandler;
import dev.imlukas.slashcommands.SlashCommandManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoCompleteListener extends ListenerAdapter {

    private final SlashCommandManager slashCommandManager;
    private final JSONFileHandler jsonFileHandler;

    public AutoCompleteListener(VeruxBot plugin) {
        this.slashCommandManager = plugin.getSlashCommandManager();
        this.jsonFileHandler = plugin.getJsonFileHandler();
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        String commandName = event.getName();
        String focusedOption = event.getFocusedOption().getName();
        Guild guild = event.getGuild();

        if (commandName.equalsIgnoreCase("preferences") & focusedOption.equalsIgnoreCase("preference")) {

            jsonFileHandler.getPreferencesArray(guild).thenAccept(words -> {
                List<Command.Choice> options = Stream.of(words)
                        .filter(word -> word.startsWith(event.getFocusedOption().getValue())) // only display words that start with the user's current input
                        .map(word -> new Command.Choice(word, word)) // map the words to choices
                        .collect(Collectors.toList());
                event.replyChoices(options).queue();
            });
        }


    }
}
