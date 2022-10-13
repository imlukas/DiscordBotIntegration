package me.imlukas.listeners;

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

    private static final List<String> WELCOME_CHANNEL_NAMES = List.of("welcome", "welcome-channel", "joins-leaves");
    private static final List<String> LEAVE_CHANNEL_NAMES = List.of("leaves", "leaves-channel", "joins-leaves", "goodbye");

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        TextChannel welcomeChannel = null;
        for (TextChannel textChannel : guild.getTextChannels()) {
            if (WELCOME_CHANNEL_NAMES.contains(textChannel.getName().toLowerCase())) {
                welcomeChannel = textChannel;
                break;
            }
        }
        if (welcomeChannel != null) {
            welcomeChannel.sendMessage("Welcome " + member.getAsMention() + " to " + guild.getName() + "!").queue();
        }
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        Guild guild = event.getGuild();
        User user = event.getUser();
        TextChannel leaveChannel = null;
        for (TextChannel textChannel : guild.getTextChannels()) {
            if (LEAVE_CHANNEL_NAMES.contains(textChannel.getName().toLowerCase())) {
                leaveChannel = textChannel;
                break;
            }
        }
        if (leaveChannel != null) {
            leaveChannel.sendMessage(user.getAsMention() + " has left " + guild.getName() + "! See you next time :)").queue();
        }
    }
}
