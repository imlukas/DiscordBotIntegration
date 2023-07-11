package dev.imlukas.slashcommands.commands.music;

import dev.imlukas.slashcommands.SlashCommand;
import dev.imlukas.slashcommands.SlashCommandContext;
import dev.imlukas.slashcommands.annotations.Option;
import dev.imlukas.slashcommands.annotations.SlashCommandHandler;
import dev.imlukas.slashcommands.commands.music.manager.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.net.URI;
import java.net.URISyntaxException;

import static dev.imlukas.slashcommands.commands.music.util.SpotifyUtil.getTrackName;

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
        ctx.getEvent().deferReply(false).queue();
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
