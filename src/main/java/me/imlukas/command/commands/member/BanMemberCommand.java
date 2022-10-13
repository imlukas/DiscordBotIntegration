package me.imlukas.command.commands.member;

import me.imlukas.command.CommandContext;
import me.imlukas.command.ICommand;

public class BanMemberCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {



    }

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String getHelp() {
        return "Bans a player from the server";
    }
}
