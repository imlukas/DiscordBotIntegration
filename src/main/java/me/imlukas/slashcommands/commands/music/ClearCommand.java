package me.imlukas.slashcommands.commands.music;

import me.imlukas.slashcommands.SlashCommand;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.annotations.SlashCommandHandler;
import me.imlukas.slashcommands.commands.music.manager.GuildMusicManager;
import me.imlukas.slashcommands.commands.music.manager.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;

public class ClearCommand implements SlashCommand {
    @SlashCommandHandler
    public void run(SlashCommandContext ctx) {
        PlayerManager playerManager = PlayerManager.getInstance();
        Guild guild = ctx.getEvent().getGuild();

        if (guild == null) {
            return;
        }

        GuildMusicManager musicManager = playerManager.getMusicManager(guild);

        if (musicManager == null) {
            return;
        }

        musicManager.getTrackScheduler().clear();

        ctx.getEvent().reply("Stopped the music and cleared the queue").queue();
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "Stops the music and clears the queue";
    }
}
