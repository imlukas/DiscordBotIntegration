package dev.imlukas.slashcommands.commands.member;

import dev.imlukas.slashcommands.SlashCommand;
import dev.imlukas.slashcommands.SlashCommandContext;
import dev.imlukas.slashcommands.annotations.Option;
import dev.imlukas.slashcommands.annotations.SlashCommandHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class UnbanSlashCommand implements SlashCommand {

    @SlashCommandHandler
    public void run(@Option(name = "user", description = "User to unban", type = OptionType.USER, required = true) User banUser,
                    @Option(name = "reason", description = "Reason for unban") String reason,
                    SlashCommandContext ctx) {

        if (reason == null) {
            reason = "No reason provided";
        }
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
