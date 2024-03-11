package dev.imlukas.commands.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.LinkedList;

public class TrackQueue extends AudioEventAdapter {

    private final AudioPlayer player;
    private final LinkedList<AudioTrack> queue = new LinkedList<>();

    public TrackQueue(AudioPlayer audioPlayer) {
        this.player = audioPlayer;
    }

    /**
     * Called when a track ends or its audio is no longer valid.
     * @param player Audio player that the track being played is attached to
     * @param track Audio track that ended or whose audio was no longer valid
     * @param endReason The reason why the track stopped playing
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        super.onTrackEnd(player, track, endReason);
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }

    /**
     * Clears the queue and stops the current track.
     */
    public void clear() {
        queue.clear();
        player.stopTrack();
    }

    /**
     * Adds a track to the queue.
     * @param track the track to add
     */
    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.add(track);
        }
    }

    /**
     * Removes a track from the queue.
     * @param index the index of the track to remove
     * @return returns the track that was removed
     */
    public AudioTrack removeTrack(int index) {
        if (queue.size() < index) {
            return null;
        }

        if (queue.isEmpty() || index < 0) {
            return null;
        }

        return queue.remove(index);
    }

    /**
     * Plays the next track on the queue.
     * @return returns the last track that was playing
     */
    public AudioTrack nextTrack() {
        AudioTrack lastPlayed = player.getPlayingTrack();
        if (queue.isEmpty()) {
            player.stopTrack();
        } else {
            player.startTrack(queue.poll(), false);
        }
        return lastPlayed;
    }


    public AudioPlayer getPlayer() {
        return player;
    }

    public LinkedList<AudioTrack> getQueue() {
        return new LinkedList<>(queue);
    }

}
