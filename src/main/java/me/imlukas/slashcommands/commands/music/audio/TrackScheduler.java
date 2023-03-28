package me.imlukas.slashcommands.commands.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final LinkedList<AudioTrack> queue = new LinkedList<>();

    public TrackScheduler(AudioPlayer audioPlayer) {
        this.player = audioPlayer;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        super.onTrackEnd(player, track, endReason);
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }

    public void clear() {
        queue.clear();
        player.stopTrack();
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.add(track);
        }

        System.out.println("Added " + track.getInfo().title + " to the queue");
    }

    public void nextTrack() {
        player.startTrack(queue.getFirst(), false);
    }

    public AudioTrack removeTrack(int index) {
        if (queue.size() < index) {
            return null;
        }

        return queue.remove(index);
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public Queue<AudioTrack> getQueue() {
        return queue;
    }

}
