package me.imlukas.slashcommands.commands.music;

import me.imlukas.Bot;
import me.imlukas.slashcommands.SlashCommand;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.annotations.Option;
import me.imlukas.slashcommands.annotations.SlashCommandHandler;
import me.imlukas.slashcommands.commands.music.manager.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static me.imlukas.slashcommands.commands.music.util.SpotifyUtil.getTrackName;

public class PlayCommand implements SlashCommand {

    @SlashCommandHandler
    public void run(@Option(name = "url", description = "The url of the song you want to play") String url, SlashCommandContext ctx) {
        SlashCommandInteractionEvent event = ctx.getEvent();
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inAudioChannel()) {
            event.reply("You need to be in a voice channel").queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inAudioChannel()) {
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
        } else {
            if(selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
                event.reply("You need to be in the same channel as me").queue();
                return;
            }
        }

        url = parseUrl(url);

        PlayerManager playerManager = PlayerManager.getInstance();
        playerManager.loadAndPlay(event.getChannel().asTextChannel(), ctx, url);
        ctx.getEvent().deferReply(true).queue();
    }

    public String parseUrl(String url) {
        if (url.contains("spotify")) {
            url = "ytsearch:" + getTrackName(url);
        } else {
            url = parseYoutubeUrl(url);
        }

        return url;
    }

    public String parseYoutubeUrl(String url) {
        try {
            new URI(url);
        } catch (URISyntaxException e) {
            url = "ytsearch:" + url;
        }

        return url;

    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "plays a song in the voice channel you're in";
    }
}
