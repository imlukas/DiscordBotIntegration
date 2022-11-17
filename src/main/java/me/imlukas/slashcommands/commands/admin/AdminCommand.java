package me.imlukas.slashcommands.commands.admin;

import me.imlukas.Bot;
import me.imlukas.slashcommands.ISlashCommand;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.annotations.Option;
import me.imlukas.slashcommands.annotations.SubCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class AdminCommand implements ISlashCommand {
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
        main.getSqlHandler().wipeUser(ctx.getGuild(), user.getIdLong());
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
