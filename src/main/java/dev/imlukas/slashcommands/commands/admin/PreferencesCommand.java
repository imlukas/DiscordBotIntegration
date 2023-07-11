package dev.imlukas.slashcommands.commands.admin;

import dev.imlukas.VeruxBot;
import dev.imlukas.database.json.JSONFileHandler;
import dev.imlukas.slashcommands.SlashCommand;
import dev.imlukas.slashcommands.SlashCommandContext;
import dev.imlukas.slashcommands.annotations.Option;
import dev.imlukas.slashcommands.annotations.SlashCommandHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;

public class PreferencesCommand implements SlashCommand {

    private final JSONFileHandler jsonFileHandler;

    public PreferencesCommand(VeruxBot main) {
        this.jsonFileHandler = main.getJsonFileHandler();
    }

    @SlashCommandHandler
    public void run(@Option(name = "preference", description = "The preference to change", required = true, autoComplete = true) String preference,
                    @Option(name = "value", description = "The value to set the preference to", required = true) String value,
                    SlashCommandContext ctx) {

       jsonFileHandler.setValue(ctx.getGuild(), "preferences." + preference, value).thenAccept((success) -> {
           if (!success) {
               ctx.getEvent().reply("Failed to set preference " + preference + " to " + value).setEphemeral(true).queue();
               return;
           }

           ctx.getEvent().reply("Set preference " + preference + " to " + value).setEphemeral(true).queue();
       });

    }

    @Override
    public String getName() {
        return "preferences";
    }

    @Override
    public DefaultMemberPermissions getPermission() {
        return DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR);
    }

    @Override
    public String getDescription() {
        return "Change the bot's preferences";
    }
}
