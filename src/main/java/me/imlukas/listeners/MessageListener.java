package me.imlukas.listeners;

import me.duncte123.botcommons.BotCommons;
import me.imlukas.CommandManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class MessageListener extends ListenerAdapter {


    private final CommandManager manager = new CommandManager();

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        String prefix = "/";
        String raw = event.getMessage().getContentRaw();

        if (raw.startsWith(prefix)) {
            manager.handle(event);
        }
    }
}
