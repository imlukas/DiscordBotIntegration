package dev.imlukas.commands.music.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.imlukas.util.command.SlashCommandContext;
import dev.imlukas.util.misc.utils.EmbedBuilders;
import dev.imlukas.util.misc.utils.MusicUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), guildId -> {
            GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getAudioForwarder());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel textChannel, SlashCommandContext context, String trackUrl) {
        GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            private final EmbedBuilder embedBuilder = EmbedBuilders.getMusicEmbed();

            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                String formattedTime = MusicUtil.getFormattedDuration(audioTrack);

                if (MusicUtil.isTooLong(audioTrack)) {
                    embedBuilder.setTitle("[Error] Track too long");
                    embedBuilder.setDescription("The track you tried to load is longer than 10 minutes");
                    context.replyEmbed(embedBuilder.build());
                    return;
                }

                musicManager.getTrackQueue().queue(audioTrack);
                embedBuilder.setTitle("[Success] Track loaded");
                embedBuilder.setDescription("Title: " + audioTrack.getInfo().title);
                embedBuilder.addField("", "Duration: " + formattedTime + " Minutes", false);

                context.replyEmbed(embedBuilder.build());
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

                context.replyEmbed(embedBuilder.build());
            }

            @Override
            public void noMatches() {
                embedBuilder.setTitle("[Error] No matches found");
                embedBuilder.setDescription("No matches found for the given query");

                context.replyEmbed(embedBuilder.build());
            }

            @Override
            public void loadFailed(FriendlyException e) {
                System.err.println("Could not play music: " + e.getMessage());
            }

        });
    }

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}