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

/**
 * Created by Lukas on 10.01.2023
 */
public class QueueCommand implements SlashCommand {

    /**
     * Sends the current music queue
     * @param ctx The SlashCommandContext
     */
    @SlashCommandHandler
    public void run(SlashCommandContext ctx) {
        Guild guild = ctx.getGuild();
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getMusicManager(guild);
        LinkedList<AudioTrack> queue = musicManager.getTrackScheduler().getQueue();

        EmbedBuilder builder = EmbedBuilders.getMusicEmbed();
        builder.setTitle("Music Queue");

        int size = queue.size();
        if (queue.size() > 50) {
            size = 50;
        }

        for (int i = 0; i < size; i++) {
            AudioTrack track = queue.get(i);
            String formattedTime = MusicUtil.getTimeString(track);

            builder.addField("", "[" + i + "] " + track.getInfo().title + " - " + formattedTime + " Minutes", false);
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
