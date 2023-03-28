package me.imlukas.slashcommands.commands.admin;

import me.imlukas.Bot;
import me.imlukas.database.json.json.JSONFileHandler;
import me.imlukas.slashcommands.SlashCommand;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.annotations.Option;
import me.imlukas.slashcommands.annotations.SlashCommandHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;

public class PreferencesCommand implements SlashCommand {

    private final Bot main;
    private final JSONFileHandler jsonFileHandler;

    public PreferencesCommand(Bot main) {
        this.main = main;
        this.jsonFileHandler = main.getJsonFileHandler();
    }

    @SlashCommandHandler
    public void run(@Option(name = "preference", description = "The preference to change", required = true, autoComplete = true) String preference,
                    @Option(name = "value", description = "The value to set the preference to", required = true) String value,
                    SlashCommandContext ctx) {

        boolean success = jsonFileHandler.setValue(ctx.getGuild(), "preferences." + preference, value);


        if (!success) {
            ctx.getEvent().reply("Failed to set preference " + preference + " to " + value).setEphemeral(true).queue();
            return;
        }

        ctx.getEvent().reply("Set preference " + preference + " to " + value).setEphemeral(true).queue();
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
