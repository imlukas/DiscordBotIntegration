package me.imlukas.slashcommands.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.imlukas.slashcommands.SlashCommand;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.annotations.SlashCommandHandler;
import me.imlukas.slashcommands.commands.music.manager.GuildMusicManager;
import me.imlukas.slashcommands.commands.music.manager.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.awt.*;
import java.util.Queue;

import static me.imlukas.utils.Colors.EMBED_PURPLE;

public class QueueCommand implements SlashCommand {

    @SlashCommandHandler
    public void run(SlashCommandContext ctx) {
        Guild guild = ctx.getGuild();
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getMusicManager(guild);
        Queue<AudioTrack> queue = musicManager.getTrackScheduler().getQueue();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Queue");
        builder.setColor(EMBED_PURPLE);

        int i = 0;
        for (AudioTrack track : queue) {
            i++;
            builder.addField("", "(" + i + ") " + track.getInfo().title, false);
        }

        ctx.getEvent().replyEmbeds(builder.build()).queue();
    }
    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getDescription() {
        return "See the current music queue";
    }
}
