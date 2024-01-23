package dev.imlukas.commands.music.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import dev.imlukas.commands.music.audio.AudioForwarder;
import dev.imlukas.commands.music.audio.TrackQueue;
import lombok.Getter;

@Getter
public class GuildMusicManager {

    private final TrackQueue trackQueue;
    private final AudioForwarder audioForwarder;

    public GuildMusicManager(AudioPlayerManager manager) {
        AudioPlayer player = manager.createPlayer();
        trackQueue = new TrackQueue(player);
        audioForwarder = new AudioForwarder(player);
    }

    public void clearQueue() {
        trackQueue.clear();
    }

    public AudioPlayer getPlayer() {
        return trackQueue.getPlayer();
    }
}