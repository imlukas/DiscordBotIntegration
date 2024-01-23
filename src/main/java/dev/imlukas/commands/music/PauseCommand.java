package dev.imlukas.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import dev.imlukas.commands.music.manager.GuildMusicManager;
import dev.imlukas.commands.music.manager.PlayerManager;
import dev.imlukas.util.command.SlashCommand;
import dev.imlukas.util.command.SlashCommandContext;
import dev.imlukas.util.command.annotations.SlashCommandHandler;
import dev.imlukas.util.misc.utils.EmbedBuilders;

public class PauseCommand implements SlashCommand {

    @SlashCommandHandler
    public void run(SlashCommandContext context) {
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getMusicManager(context.getGuild());
        AudioPlayer player = musicManager.getPlayer();

        boolean isPaused = player.isPaused();
        String action = isPaused ? "Resumed" : "Paused";

        player.setPaused(!isPaused);
        context.replyEmbed(EmbedBuilders.success(action + " the music").build());
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
