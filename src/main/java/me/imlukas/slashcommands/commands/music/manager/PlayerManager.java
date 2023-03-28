package me.imlukas.slashcommands.commands.music.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.utils.EmbedBuilders;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager(){
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild){
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getAudioForwarder());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel textChannel, SlashCommandContext data, String trackUrl){
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.getTrackScheduler().queue(audioTrack);

                EmbedBuilder embedBuilder = EmbedBuilders.MUSIC_EMBED;

                int seconds = (int) (audioTrack.getInfo().length / 1000) % 60;

                embedBuilder.setTitle("[Success] Track loaded");
                embedBuilder.setDescription(audioTrack.getInfo().title);
                embedBuilder.addField("", "Duration: " + seconds, false);
                data.getEvent().getHook().sendMessageEmbeds(embedBuilder.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                for (AudioTrack track : audioPlaylist.getTracks()) {
                    musicManager.getTrackScheduler().queue(track);
                }
                EmbedBuilder embedBuilder = EmbedBuilders.MUSIC_EMBED;

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

    public static PlayerManager getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}