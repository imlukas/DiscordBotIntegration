package dev.imlukas.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.imlukas.util.command.SlashCommand;
import dev.imlukas.util.command.SlashCommandContext;
import dev.imlukas.util.command.annotations.Option;
import dev.imlukas.util.command.annotations.SlashCommandHandler;
import dev.imlukas.commands.music.manager.GuildMusicManager;
import dev.imlukas.commands.music.manager.PlayerManager;
import dev.imlukas.util.misc.utils.EmbedBuilders;
import dev.imlukas.util.misc.utils.MusicUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

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
    public void run(@Option(name = "page", description = "The page of the queue", type = OptionType.INTEGER) int page, SlashCommandContext ctx) {
        Guild guild = ctx.getGuild();
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getMusicManager(guild);
        LinkedList<AudioTrack> queue = musicManager.getTrackQueue().getQueue();

        if (queue.isEmpty()) {
            ctx.replyEmbed(EmbedBuilders.error("The queue is empty").build());
            return;
        }

        EmbedBuilder builder = EmbedBuilders.getMusicEmbed();
        builder.setTitle("Queue");

        if (page == -1) {
            page = 0;
        }

        int start = page == 1 ? 0 : (page - 1) * 25;
        int size = Math.min(queue.size(), 25);
        int end = start + size;

        if (end > queue.size()) {
            end = queue.size();
        }

        for (int i = start; i < end; i++) {
            AudioTrack track = queue.get(i);
            String formattedTime = MusicUtil.getFormattedDuration(track);

            builder.addField("", "[" + i + 1 + "] " + track.getInfo().title + " - " + formattedTime + " Minutes", false);
        }

        Button nextButton = Button.primary("Next", "Next");
        Button previousButton = Button.primary("Previous", "Previous");
        ctx.getEvent().replyEmbeds(builder.build()).setActionRow(previousButton, nextButton).queue();
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
