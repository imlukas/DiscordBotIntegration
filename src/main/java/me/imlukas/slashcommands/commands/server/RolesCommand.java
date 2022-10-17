package me.imlukas.slashcommands.commands.server;

import me.imlukas.slashcommands.ISlashCommand;
import me.imlukas.slashcommands.ISlashCommandCtx;
import me.imlukas.slashcommands.annotations.SlashCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.util.List;

public class RolesCommand implements ISlashCommand {

    @SlashCommand
    public void run(ISlashCommandCtx ctx) {
        Guild guild = ctx.getGuild();
        TextChannel textChannel = ctx.getTextChannel();
        List<Role> roleList = guild.getRoles();
        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        for (Role role : roleList) {

            String roleAsMention = role.getAsMention();
            String roleID = role.getId();
            List<Member> roleMembers = guild.getMembersWithRoles(role);

            messageCreateBuilder.addContent(roleAsMention + "\tID: " + roleID + "\tMembers: " + roleMembers.size() + "\n");
        }

        textChannel.sendMessage(MarkdownUtil.codeblock(messageCreateBuilder.getContent())).queue();
    }

    @Override
    public String getName() {
        return "roles";
    }

    @Override
    public String getDescription() {
        return "Get a list of roles";
    }
}
