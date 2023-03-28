package me.imlukas.slashcommands.commands.server;

import me.imlukas.slashcommands.SlashCommand;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.annotations.SlashCommandHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.OffsetDateTime;

public class ServerCommand implements SlashCommand {

    @SlashCommandHandler
    public void run(SlashCommandContext ctx) {
        TextChannel channel = ctx.getTextChannel();
        Guild guild = ctx.getGuild();
        Member guildOwner = guild.getOwner();
        String guildID = guild.getId();
        String guildName = guild.getName();
        String guildLogo = guild.getIcon().getUrl();
        OffsetDateTime guildCreatedDate = guild.getTimeCreated();
        int textChannels = guild.getTextChannels().size();
        int voiceChannels = guild.getVoiceChannels().size();
        int totalChannels = textChannels + voiceChannels;
        int totalMembers = guild.getMemberCount();
        int totalRoles = guild.getRoles().size();
        int onlineMembers = 0;

        for (Member member : guild.getMembers()) {
            if (member.getOnlineStatus() == OnlineStatus.OFFLINE) {
                continue;
            }
            onlineMembers += 1;
        }


        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(guildName, null, guildLogo);
        embedBuilder.setThumbnail(guildLogo);
        embedBuilder.addField(":id: Server ID:", guildID, true);
        embedBuilder.addField(":calendar: Created on:", "<t:" + guildCreatedDate.toEpochSecond() + ":R>", true);
        embedBuilder.addField(":crown: Owned by:", guildOwner.getAsMention(), true);
        embedBuilder.addField(":busts_in_silhouette: Members (" + totalMembers + ")", "Members Online: " + onlineMembers, true);
        embedBuilder.addField(":speech_balloon: Channels (" + totalChannels + ")", textChannels + " Text | " + voiceChannels + " Voice", true);
        embedBuilder.addField(":closed_lock_with_key: Roles (" + totalRoles + ")", "To see all the roles do /roles!", true);

        embedBuilder.build();


        channel.sendMessageEmbeds(embedBuilder.build()).queue();

    }

    @Override
    public String getName() {
        return "server";
    }

    @Override
    public String getDescription() {
        return "Shows the server information";
    }
}
