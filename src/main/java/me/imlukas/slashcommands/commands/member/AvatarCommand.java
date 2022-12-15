package me.imlukas.slashcommands.commands.member;

import me.imlukas.slashcommands.ISlashCommand;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.annotations.Option;
import me.imlukas.slashcommands.annotations.SlashCommandHandler;
import me.imlukas.slashcommands.annotations.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.awt.*;

public class AvatarCommand implements ISlashCommand {

    @SlashCommandHandler
    public void run() {

    }

    @SubCommand(name = "user", description = "Get a user's main avatar")
    public void userAvatar(@Option(name = "user", description = "User to get avatar of", type = OptionType.USER, required = true) User user,
                           SlashCommandContext ctx) {

        SlashCommandInteractionEvent event = ctx.getEvent();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(user.getAsTag(), null, user.getAvatarUrl());
        embed.setTitle("User avatar");
        embed.setImage(user.getAvatarUrl() + "?size=2048");
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
