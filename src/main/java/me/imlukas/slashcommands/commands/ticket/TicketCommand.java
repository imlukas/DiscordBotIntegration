package me.imlukas.slashcommands.commands.ticket;

import me.imlukas.Bot;
import me.imlukas.database.json.json.JSONFileHandler;
import me.imlukas.slashcommands.ISlashCommand;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.annotations.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class TicketCommand implements ISlashCommand {

    private final Bot main;
    private final JSONFileHandler jsonFileHandler;

    public TicketCommand(Bot main) {
        this.main = main;
        this.jsonFileHandler = main.getJsonFileHandler();
    }

    @SubCommand(name = "create", description = "Creates the ticket embed")
    public void ticket(SlashCommandContext ctx) {

        EmbedBuilder builder = new EmbedBuilder();

        Button button = Button.primary("Support", "Support").withEmoji(Emoji.fromUnicode("\uD83D\uDCF2"));

        jsonFileHandler.getString(ctx.getGuild(), "preferences.ticketTitle").thenAccept(builder::setTitle);

        jsonFileHandler.getString(ctx.getGuild(), "preferences.ticketDescription").thenAccept(builder::setDescription);

        jsonFileHandler.getString(ctx.getGuild(), "preferences.ticketChannel").thenAccept((channelNameFromJson) -> {
            TextChannel textChannel = ctx.getGuild().getTextChannelsByName(channelNameFromJson, true).get(0);

            if (textChannel == null) {
                ctx.getEvent().reply("Invalid channel name").setEphemeral(true).queue();
                return;
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
