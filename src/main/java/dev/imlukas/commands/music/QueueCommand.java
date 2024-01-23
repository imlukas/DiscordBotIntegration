package dev.imlukas.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.imlukas.util.command.SlashCommand;
import dev.imlukas.util.command.SlashCommandContext;
import dev.imlukas.util.command.annotations.SlashCommandHandler;
import dev.imlukas.commands.music.manager.GuildMusicManager;
import dev.imlukas.commands.music.manager.PlayerManager;
import dev.imlukas.util.misc.utils.EmbedBuilders;
import dev.imlukas.util.misc.utils.MusicUtil;
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

        // TODO: ADD PAGING
        Guild guild = ctx.getGuild();
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getMusicManager(guild);
        LinkedList<AudioTrack> queue = musicManager.getTrackQueue().getQueue();

        if (queue.isEmpty()) {
            ctx.replyEmbed(EmbedBuilders.error("The queue is empty"));
            return;
        }

        EmbedBuilder builder = EmbedBuilders.getMusicEmbed();
        builder.setTitle("Queue");

        int size = queue.size();
        if (queue.size() > 50) {
            size = 50;
        }

        for (int i = 0; i < size; i++) {
            AudioTrack track = queue.get(i);
            String formattedTime = MusicUtil.getFormattedDuration(track);

            builder.addField("", "[" + i + "] " + track.getInfo().title + " - " + formattedTime + " Minutes", false);
        }

        ctx.replyEmbed(builder.build());
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
