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

public class UnbanSlashCommand implements ISlashCommand {

    @SlashCommand
    public void run(@Option(name = "user", description = "User to unban", type = OptionType.USER, required = true) User banUser,
                    @Option(name = "reason", description = "Reason for ban") String reason,
                    SlashCommandContext ctx) {

        Guild guild = ctx.getGuild();
        guild.unban(banUser).reason(reason).queue();
    }

    @Override
    public String getName() {
        return "unban";
    }

    @Override
    public DefaultMemberPermissions getPermission() {
        return DefaultMemberPermissions.ENABLED;
    }

    @Override
    public String getDescription() {
        return "Unban a user";
    }
}
