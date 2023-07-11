package dev.imlukas.slashcommands.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.imlukas.slashcommands.SlashCommand;
import dev.imlukas.slashcommands.SlashCommandContext;
import dev.imlukas.slashcommands.annotations.SlashCommandHandler;
import dev.imlukas.slashcommands.commands.music.manager.GuildMusicManager;
import dev.imlukas.slashcommands.commands.music.manager.PlayerManager;
import dev.imlukas.utils.EmbedBuilders;
import dev.imlukas.utils.MusicUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.util.LinkedList;

public class NowPlayingCommand implements SlashCommand {

    @SlashCommandHandler
    public void run(SlashCommandContext context) {
        Guild guild = context.getGuild();
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getMusicManager(guild);
        AudioTrack track = musicManager.getTrackScheduler().getPlayer().getPlayingTrack();


        EmbedBuilder builder = EmbedBuilders.getMusicEmbed();
        if (track == null) {
            builder.setTitle("Nothing is playing");
        } else {
            String formattedTime = MusicUtil.getTimeString(track);
            builder.setTitle("Now Playing");
            builder.setDescription("Title: " + track.getInfo().title);
            builder.addField("", "Duration: " + formattedTime + " Minutes", false);
        }

        context.getEvent().replyEmbeds(builder.build()).queue();
    }

    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getDescription() {
        return "Shows the current playing music";
    }
}
