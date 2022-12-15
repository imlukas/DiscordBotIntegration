package me.imlukas.slashcommands.commands.xp.listener;

import me.imlukas.Bot;
import me.imlukas.utils.XpUtil;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class UserMessageListener extends ListenerAdapter {

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

        main.getSqlHandler().getXp(event.getGuild(), user).thenAccept((oldXp) -> {
            main.getSqlHandler().addXp(event.getGuild(), xp, user);
            main.getSqlHandler().getXp(event.getGuild(), user).thenAccept((newXp) -> {
                int xpNeeded = XpUtil.getXpToLevel(XpUtil.getLevelFromXp(oldXp) + 1);
                if (newXp >= xpNeeded) {
                    event.getChannel().sendMessage("You leveled up! Your current level is " + XpUtil.getLevelFromXp(newXp)).queue();
                }
            });
        });


    }
}
