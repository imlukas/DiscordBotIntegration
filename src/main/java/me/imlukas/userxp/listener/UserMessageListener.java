package me.imlukas.userxp.listener;

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
        int xp = random.nextInt(10, 40);
        event.getChannel().sendMessage("Added " + xp + "XP").queue();
        main.getSqlHandler().addXp(event.getGuild(), xp, user);
        int playerXp = main.getSqlHandler().getXp(event.getGuild(), user).join();

        if (playerXp > XpUtil.getLevelXp(XpUtil.getLevelFromXp(playerXp))) {
            event.getChannel().sendMessage("You leveled up to level " + XpUtil.getLevelFromXp(playerXp)).queue();
        }

    }
}
