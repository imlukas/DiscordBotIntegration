package dev.imlukas.listeners;

import dev.imlukas.VeruxBot;
import dev.imlukas.database.json.JSONFileHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EnterLeaveListener extends ListenerAdapter {


    private final JSONFileHandler jsonFileHandler;

    public EnterLeaveListener(VeruxBot main) {
        this.jsonFileHandler = main.getJsonFileHandler();
    }

    private static final List<String> WELCOME_CHANNEL_NAMES = List.of("welcome", "welcome-channel", "joins-leaves");
    private static final List<String> LEAVE_CHANNEL_NAMES = List.of("leaves", "leaves-channel", "joins-leaves", "goodbye");

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();

        jsonFileHandler.getString(guild, "preferences.welcome-channel").thenAccept((channelId) -> {
            TextChannel targetChannel = guild.getTextChannelById(channelId);

            if (targetChannel == null) {
                for (TextChannel textChannel : guild.getTextChannels()) {
                    if (WELCOME_CHANNEL_NAMES.contains(textChannel.getName().toLowerCase())) {
                        targetChannel = textChannel;
                        break;
                    }
                }
            }

            targetChannel.sendMessage("Welcome " + member.getAsMention() + " to " + guild.getName() + "!").queue();
        });

    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        Guild guild = event.getGuild();
        User user = event.getUser();

        jsonFileHandler.getString(guild, "preferences.leaves-channel").thenAccept((channelId) -> {
            TextChannel targetChannel = guild.getTextChannelById(channelId);

            if (targetChannel == null) {
                for (TextChannel textChannel : guild.getTextChannels()) {
                    if (LEAVE_CHANNEL_NAMES.contains(textChannel.getName().toLowerCase())) {
                        targetChannel = textChannel;
                        break;
                    }
                }
            }

            targetChannel.sendMessage(user.getAsMention() + " has left " + guild.getName() + "! See you next time :)").queue();
        });

    }
}
