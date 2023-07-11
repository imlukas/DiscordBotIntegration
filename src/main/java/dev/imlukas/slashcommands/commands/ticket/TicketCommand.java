package dev.imlukas.slashcommands.commands.ticket;

import dev.imlukas.VeruxBot;
import dev.imlukas.database.json.JSONFileHandler;
import dev.imlukas.slashcommands.SlashCommand;
import dev.imlukas.slashcommands.SlashCommandContext;
import dev.imlukas.slashcommands.annotations.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class TicketCommand implements SlashCommand {

    private final JSONFileHandler jsonFileHandler;

    public TicketCommand(VeruxBot main) {
        this.jsonFileHandler = main.getJsonFileHandler();
    }

    @SubCommand(name = "create", description = "Creates the ticket embed")
    public void ticket(SlashCommandContext ctx) {

        EmbedBuilder builder = new EmbedBuilder();

        Button button = Button.primary("Support", "Support").withEmoji(Emoji.fromUnicode("\uD83D\uDCF2"));

        jsonFileHandler.getString(ctx.getGuild(), "preferences.ticketTitle").thenAccept(builder::setTitle);
        jsonFileHandler.getString(ctx.getGuild(), "preferences.ticketDescription").thenAccept(builder::setDescription);

        jsonFileHandler.getString(ctx.getGuild(), "preferences.ticketChannel").thenAccept((channelId) -> {

            TextChannel textChannel = ctx.getGuild().getTextChannelById(channelId);

            if (textChannel == null) {
                textChannel = ctx.getGuild().getTextChannelsByName(channelId, true).get(0);

                if (textChannel == null) {
                    ctx.getEvent().reply("Invalid channel! Use /preferences ticketChannel!").setEphemeral(true).queue();
                    return;
                }
            }

            ctx.getEvent().reply("Created ticket in " + textChannel.getAsMention()).setEphemeral(true).queue();
            textChannel.sendMessageEmbeds(builder.build()).setActionRow(button).queue();
        }).exceptionally((exception) -> {
            ctx.getEvent().reply("Failed to create ticket").setEphemeral(true).queue();
            return null;
        });
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
