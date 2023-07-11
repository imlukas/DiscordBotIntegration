package dev.imlukas.slashcommands.commands.member;

import dev.imlukas.slashcommands.SlashCommand;
import dev.imlukas.slashcommands.SlashCommandContext;
import dev.imlukas.slashcommands.annotations.Option;
import dev.imlukas.slashcommands.annotations.SlashCommandHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.concurrent.TimeUnit;


public class BanSlashCommand implements SlashCommand {

    @SlashCommandHandler
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
