package me.imlukas.slashcommands.commands.xp.listener;

import me.imlukas.Bot;
import me.imlukas.database.mysql.data.ColumnType;
import me.imlukas.database.mysql.data.DataType;
import me.imlukas.utils.XpUtil;
import net.dv8tion.jda.api.entities.Guild;
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

        DataType<Integer> xpData = new DataType<>(ColumnType.INT, "xp");

        Guild guild = event.getGuild();

        main.getSqlHandler().fetch(xpData, event.getGuild(), user).thenAccept((oldXp) -> {
            main.getSqlHandler().addXp(xpData, xp, guild, user);

            main.getSqlHandler().fetch(xpData, guild, user).thenAccept((newXp) -> {
                int xpNeeded = XpUtil.getXpToLevel(XpUtil.getLevelFromXp(oldXp) + 1);
                if (newXp >= xpNeeded) {
                    event.getChannel().sendMessage("You leveled up! Your current level is " + XpUtil.getLevelFromXp(newXp)).queue();
                }
            });
        });


    }
}
