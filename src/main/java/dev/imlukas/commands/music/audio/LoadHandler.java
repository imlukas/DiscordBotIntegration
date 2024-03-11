package dev.imlukas.commands.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.imlukas.commands.music.manager.GuildMusicManager;
import dev.imlukas.util.command.SlashCommandContext;
import dev.imlukas.util.misc.utils.EmbedBuilders;
import dev.imlukas.util.misc.utils.MusicUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.InteractionHook;

public class LoadHandler implements AudioLoadResultHandler {
    private final GuildMusicManager musicManager;
    private final EmbedBuilder embedBuilder = EmbedBuilders.getMusicEmbed();
    private final InteractionHook hook;

    public LoadHandler(SlashCommandContext context, GuildMusicManager musicManager) {
        this.musicManager = musicManager;
        this.hook = context.getEvent().getHook();
    }
    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        String formattedTime = MusicUtil.getFormattedDuration(audioTrack);

        if (MusicUtil.isTooLong(audioTrack)) {
            embedBuilder.setTitle("[Error] Track too long");
            embedBuilder.setDescription("The track you tried to load is longer than 10 minutes");
            hook.sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        }

        musicManager.getTrackQueue().queue(audioTrack);
        embedBuilder.setTitle("[Success] Track loaded");
        embedBuilder.setDescription("Title: " + audioTrack.getInfo().title);
        embedBuilder.addField("", "Duration: " + formattedTime + " Minutes", false);

        hook.sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        if (audioPlaylist.isSearchResult()) {
            trackLoaded(audioPlaylist.getTracks().get(0));
            return;
        }

        for (AudioTrack track : audioPlaylist.getTracks()) {
            musicManager.getTrackQueue().queue(track);
        }

        embedBuilder.setTitle("[Success] Playlist loaded");
        embedBuilder.setDescription(audioPlaylist.getName());
        embedBuilder.addField("", "Tracks: " + audioPlaylist.getTracks().size(), false);

        hook.sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public void noMatches() {
        embedBuilder.setTitle("[Error] No matches found");
        embedBuilder.setDescription("No matches found for the given query");

        hook.sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public void loadFailed(FriendlyException e) {
        System.err.println("Could not play music: " + e.getMessage());
    }
}
