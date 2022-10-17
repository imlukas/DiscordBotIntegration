package me.imlukas.slashcommands.commands.member;

import me.imlukas.slashcommands.ISlashCommand;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.annotations.Option;
import me.imlukas.slashcommands.annotations.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.concurrent.TimeUnit;


public class BanSlashCommand implements ISlashCommand {

    @SlashCommand
    public void run(@Option(name = "user", description = "User to ban", type = OptionType.USER, required = true) User banUser,
                           @Option(name = "reason", description = "Reason for ban") String reason,
                           SlashCommandContext ctx) {
        if (reason == null) {
            reason = "No reason provided";
        }
        Guild guild = ctx.getGuild();
        guild.ban(banUser, 0, TimeUnit.SECONDS).reason(reason).queue();
        guild.pruneMemberCache();
    }



    // TODO: have some fun with annotations to make command options.

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public DefaultMemberPermissions getPermission() {
        return DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS);
    }

    @Override
    public String getDescription() {
        return "Ban a player";
    }
}
