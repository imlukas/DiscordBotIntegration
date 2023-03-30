package me.imlukas.slashcommands.commands.admin;

import me.imlukas.Bot;
import me.imlukas.slashcommands.SlashCommand;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.annotations.Option;
import me.imlukas.slashcommands.annotations.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class AdminCommand implements SlashCommand {
    private final Bot main;

    public AdminCommand(Bot main) {
        this.main = main;
    }

    @SubCommand(name = "terminate", description = "terminates the bot")
    public void shutdown() {
        main.getShardManager().shutdown();
    }


    @SubCommand(name = "wipe", description = "terminates the bot")
    public void wipe(@Option(name = "user", description = "The user to wipe", type = OptionType.USER, required = true) User user, SlashCommandContext ctx) {

        if (user.isBot() || user.isSystem()) {
            ctx.getEvent().reply("You can't wipe a bot or system user").setEphemeral(true).queue();
            return;
        }

        ctx.getEvent().reply("Wiped " + user.getAsTag()).queue();
    }


    @SubCommand(name = "upload", description = "Uploads a file to the server")
    public void upload(@Option(name = "file", description = "the file to upload", type = OptionType.ATTACHMENT, required = true) Message.Attachment file, SlashCommandContext ctx) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.addField("File name", file.getFileName(), false);
        builder.addField("File size", file.getSize() + " bytes", false);
        builder.addField("File url", file.getUrl(), false);
        builder.addField("File proxy url", file.getProxyUrl(), false);
        builder.addField("File id", file.getId(), false);
        builder.addField(" ", file.toAttachmentData(1).toPrettyString(), false);

        ctx.getEvent().replyEmbeds(builder.build()).addActionRow(
                Button.link(file.getUrl(), "Download")
        ).queue();

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
