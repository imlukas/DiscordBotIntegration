package dev.imlukas.slashcommands.commands.admin;

import dev.imlukas.VeruxBot;
import dev.imlukas.slashcommands.SlashCommand;
import dev.imlukas.slashcommands.SlashCommandContext;
import dev.imlukas.slashcommands.annotations.Option;
import dev.imlukas.slashcommands.annotations.SubCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class AdminCommand implements SlashCommand {
    private final VeruxBot main;

    public AdminCommand(VeruxBot main) {
        this.main = main;
    }

    @SubCommand(name = "resetserver", description = "Reset this server's data")
    public void reset(SlashCommandContext ctx) {

        main.getJsonFileHandler().resetAll(ctx.getGuild());
        ctx.getEvent().reply("Server's data has been reset.").queue();
    }

    @SubCommand(name = "clearchannel", description = "Clears the channel")
    public void clear(@Option(name = "channel", description = "The channel to clear", type = OptionType.CHANNEL) String channel, SlashCommandContext ctx) {
        Channel eventChannel = ctx.getEvent().getChannel();

        if (!(eventChannel instanceof TextChannel textChannel)) {
            return;
        }

        if (channel != null) {
            TextChannel targetChannel = ctx.getGuild().getTextChannelById(channel);

            if (targetChannel == null) {
                targetChannel = ctx.getGuild().getTextChannelsByName(channel, true).get(0);
            }

            if (targetChannel == null) {
                ctx.getEvent().reply("Invalid channel!").setEphemeral(true).queue();
                return;
            }

            copyAndClear(targetChannel);
        }

        copyAndClear(textChannel);
    }

    public void copyAndClear(TextChannel textChannel) {
        textChannel.createCopy().queue((newChannel) -> {
            textChannel.delete().queue();
            newChannel.sendMessage("Channel cleared!").queue();
        });
    }

    @Override
    public String getName() {
        return "admin";
    }

    @Override
    public String getDescription() {
        return "Admin commands";
    }

    @Override
    public DefaultMemberPermissions getPermission() {
        return DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR);
    }
}
