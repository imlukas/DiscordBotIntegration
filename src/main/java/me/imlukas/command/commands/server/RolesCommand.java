package me.imlukas.command.commands.server;

import me.imlukas.command.CommandContext;
import me.imlukas.command.ICommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.util.List;

public class RolesCommand implements ICommand {

    @Override
    public List<String> getAliases() {
        return List.of("roles", "seeroles");
    }

    @Override
    public void handle(CommandContext ctx) {
        Guild guild = ctx.getGuild();
        TextChannel textChannel = ctx.getTextChannel();
        List<Role> roleList = guild.getRoles();
        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        for (Role role : roleList){

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
    public String getHelp() {
        return "see all the server roles";
    }
}
