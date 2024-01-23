package dev.imlukas.commands.member;

import dev.imlukas.util.command.SlashCommand;
import dev.imlukas.util.command.SlashCommandContext;
import dev.imlukas.util.command.annotations.Option;
import dev.imlukas.util.command.annotations.SlashCommandHandler;
import dev.imlukas.util.command.annotations.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;

public class AvatarCommand implements SlashCommand {

    @SlashCommandHandler
    public void run() {

    }

    @SubCommand(name = "user", description = "Get a user's main avatar")
    public void userAvatar(@Option(name = "user", description = "User to get avatar of", type = OptionType.USER, required = true) User user,
                           SlashCommandContext ctx) {

        SlashCommandInteractionEvent event = ctx.getEvent();
        String avatarUrl = user.getAvatarUrl();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(user.getAsTag(), null, avatarUrl);
        embed.setTitle("User avatar");
        embed.setImage(avatarUrl + "?size=2048");
        event.replyEmbeds(embed.build()).queue();
    }

    @SubCommand(name = "guild", description = "Get the user's guild icon, if he has one.")
    public void guildAvatar(@Option(name = "user", description = "User to get avatar of", type = OptionType.USER, required = true) User user,
                            SlashCommandContext ctx) {
        SlashCommandInteractionEvent event = ctx.getEvent();
        String guildAvatar = event.getGuild().getMember(user).getAvatarUrl();
        EmbedBuilder embed = new EmbedBuilder();

        if (guildAvatar == null) {
            embed.setColor(Color.red);
            embed.setDescription(":x: " + user.getAsTag() + " does not have a server avatar.");
        } else {
            embed.setAuthor(user.getAsTag(), null, user.getAvatarUrl());
            embed.setTitle("User's server avatar");
            embed.setImage(guildAvatar + "?size=2048");
        }
        event.replyEmbeds(embed.build()).queue();

    }

    @SubCommand(name = "server", description = "Get the server's icon, if there's one.")
    public void serverAvatar(SlashCommandContext ctx) {
        SlashCommandInteractionEvent event = ctx.getEvent();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(ctx.getGuild().getName(), null, ctx.getGuild().getIconUrl());
        embed.setTitle("Server icon");

        if (ctx.getGuild().getIconUrl() == null) {
            embed.setDescription(":x: This server doesn't have an icon.");
        } else {
            embed.setImage(ctx.getGuild().getIconUrl() + "?size=2048");
        }
        event.replyEmbeds(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "avatar";
    }

    @Override
    public String getDescription() {
        return "Avatar related commands";
    }
}
