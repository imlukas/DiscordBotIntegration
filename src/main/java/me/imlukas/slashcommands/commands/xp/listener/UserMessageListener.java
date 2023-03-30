package me.imlukas.slashcommands.commands.xp.listener;

import me.imlukas.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class UserMessageListener extends ListenerAdapter {

    // TODO: RE-WRITE XP SYSTEM
    private final Bot main;
    private final Random random = ThreadLocalRandom.current();

    public UserMessageListener(Bot main) {
        this.main = main;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        User user = event.getAuthor();
        if (user.isBot()) {
            return;
        }
        int xp = random.nextInt(5, 35);


        Guild guild = event.getGuild();
    }
}
