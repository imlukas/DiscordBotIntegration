package me.imlukas.slashcommands.commands.ticket;

import me.imlukas.slashcommands.ISlashCommand;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.annotations.Option;
import me.imlukas.slashcommands.annotations.SlashCommandHandler;
import me.imlukas.slashcommands.annotations.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class TicketCommand implements ISlashCommand {

    @SubCommand(name = "create", description = "Creates the ticket embed")
    public void ticket(@Option(name= "channel-name", description =  "The name of the channel to create this ticket", required = false) String channelName, SlashCommandContext ctx){

        EmbedBuilder builder = new EmbedBuilder();

        Button button = Button.primary("Support", "Support").withEmoji(Emoji.fromUnicode("\uD83D\uDCF2"));

        builder.setAuthor("Ticket System");

        if (channelName != null) {
            TextChannel textChannel = ctx.getGuild().getTextChannelsByName(channelName, true).get(0);

            if (textChannel == null) {
                ctx.getEvent().reply("Invalid channel name").setEphemeral(true).queue();
            }

            textChannel.sendMessageEmbeds(builder.build()).setActionRow(button).queue();
            ctx.getEvent().reply("Created ticket in " + textChannel.getAsMention()).setEphemeral(true).queue();
            return;
        }

        ctx.getEvent().replyEmbeds(builder.build()).addActionRow(button).queue();
    }

    @Override
    public String getName() {
        return "ticket";
    }

    @Override
    public String getDescription() {
        return "Ticket commands";
    }
}
