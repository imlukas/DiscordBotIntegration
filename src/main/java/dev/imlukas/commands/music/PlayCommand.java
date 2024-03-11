package dev.imlukas.commands.music;

import dev.imlukas.util.command.SlashCommand;
import dev.imlukas.util.command.SlashCommandContext;
import dev.imlukas.util.command.annotations.Option;
import dev.imlukas.util.command.annotations.SlashCommandHandler;
import dev.imlukas.commands.music.manager.PlayerManager;
import dev.imlukas.util.misc.utils.EmbedBuilders;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.net.URI;
import java.net.URISyntaxException;

import static dev.imlukas.commands.music.util.SpotifyUtil.getTrackName;

public class PlayCommand implements SlashCommand {

    @SlashCommandHandler
    public void run(@Option(name = "url", description = "The url or name of the song/playlist you want to play") String url, SlashCommandContext ctx) {
        SlashCommandInteractionEvent event = ctx.getEvent();
        Member member = event.getMember();
        GuildVoiceState memberState = member.getVoiceState();

        if (memberState == null) {
            return;
        }

        if(!memberState.inAudioChannel()) {
            event.replyEmbeds(EmbedBuilders.error("You need to be in a voice channel to play music").build()).queue();
            return;
        }

        Member veruxInstance = event.getGuild().getSelfMember();
        GuildVoiceState veruxState = veruxInstance.getVoiceState();

        if (veruxState == null) {
            System.out.println("Verux state is null");
            return;
        }

        if(!veruxState.inAudioChannel()) {
            event.getGuild().getAudioManager().openAudioConnection(memberState.getChannel());
        }

        url = parseUrl(url);

        PlayerManager playerManager = PlayerManager.getInstance();
        System.out.println("Loading track command: " + url);
        playerManager.loadAndPlay(event.getChannel().asTextChannel(), ctx, url);
        event.deferReply(false).queue();
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
