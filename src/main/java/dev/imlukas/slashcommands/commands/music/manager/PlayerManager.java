package dev.imlukas.slashcommands.commands.music.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.imlukas.slashcommands.SlashCommandContext;
import dev.imlukas.utils.EmbedBuilders;
import dev.imlukas.utils.MusicUtil;
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
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getAudioForwarder());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel textChannel, SlashCommandContext data, String trackUrl) {
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                String formattedTime = MusicUtil.getTimeString(audioTrack);
                EmbedBuilder embedBuilder = EmbedBuilders.getMusicEmbed();
                if (MusicUtil.isTooLong(audioTrack)) {
                    embedBuilder.setTitle("[Error] Track too long");
                    embedBuilder.setDescription("The track you tried to load is longer than 10 minutes");
                } else {
                    musicManager.getTrackScheduler().queue(audioTrack);
                    embedBuilder.setTitle("[Success] Track loaded");
                    embedBuilder.setDescription("Title: " + audioTrack.getInfo().title);
                    embedBuilder.addField("", "Duration: " + formattedTime + " Minutes", false);
                }

                data.getEvent().getHook().sendMessageEmbeds(embedBuilder.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                if (audioPlaylist.isSearchResult()) {
                    trackLoaded(audioPlaylist.getTracks().get(0));
                    return;
                }

                for (AudioTrack track : audioPlaylist.getTracks()) {
                    musicManager.getTrackScheduler().queue(track);
                }
                EmbedBuilder embedBuilder = EmbedBuilders.getMusicEmbed();

                embedBuilder.setTitle("[Success] Playlist loaded");
                embedBuilder.setDescription(audioPlaylist.getName());
                embedBuilder.addField("", "Tracks: " + audioPlaylist.getTracks().size(), false);

                data.getEvent().getHook().sendMessageEmbeds(embedBuilder.build()).queue();
            }

            @Override
            public void noMatches() {
                System.out.println("No matches found");
            }

            @Override
            public void loadFailed(FriendlyException e) {
                System.out.println(e.getMessage());
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