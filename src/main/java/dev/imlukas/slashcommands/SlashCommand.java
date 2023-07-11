package dev.imlukas.slashcommands;

import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;

public interface SlashCommand {

    String getName();
    default DefaultMemberPermissions getPermission() {
        return DefaultMemberPermissions.ENABLED;
    }
    String getDescription();
}
