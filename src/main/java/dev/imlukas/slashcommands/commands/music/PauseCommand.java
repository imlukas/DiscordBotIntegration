package dev.imlukas.slashcommands.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import dev.imlukas.slashcommands.SlashCommand;
import dev.imlukas.slashcommands.SlashCommandContext;
import dev.imlukas.slashcommands.annotations.SlashCommandHandler;
import dev.imlukas.slashcommands.commands.music.manager.GuildMusicManager;
import dev.imlukas.slashcommands.commands.music.manager.PlayerManager;

public class PauseCommand implements SlashCommand {

    @SlashCommandHandler
    public void run(SlashCommandContext context) {
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getMusicManager(context.getGuild());

        AudioPlayer player = musicManager.getTrackScheduler().getPlayer();
        if (player.isPaused()) {
            player.setPaused(false);
            context.getEvent().reply("Resumed the music").queue();
        } else {
            player.setPaused(true);
            context.getEvent().reply("Paused the music").queue();
        }
    }
    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getDescription() {
        return "Pause the music";
    }
}
