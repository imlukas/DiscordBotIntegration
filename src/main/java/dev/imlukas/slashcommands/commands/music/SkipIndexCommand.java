package dev.imlukas.slashcommands.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.imlukas.slashcommands.SlashCommand;
import dev.imlukas.slashcommands.SlashCommandContext;
import dev.imlukas.slashcommands.annotations.Option;
import dev.imlukas.slashcommands.annotations.SlashCommandHandler;
import dev.imlukas.slashcommands.commands.music.manager.GuildMusicManager;
import dev.imlukas.slashcommands.commands.music.manager.PlayerManager;
import dev.imlukas.utils.EmbedBuilders;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class SkipIndexCommand implements SlashCommand {
    @SlashCommandHandler
    public void run(@Option(name = "skipindex", description = "Index of the music to skip", type = OptionType.INTEGER, required = true) int index, SlashCommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getMusicManager(ctx.getGuild());
        index = index - 1;
        AudioTrack audioTrack = musicManager.getTrackScheduler().removeTrack(index);
        EmbedBuilder embedBuilder = EmbedBuilders.getMusicEmbed();

        if (audioTrack == null) {
            embedBuilder.setTitle("[Error] There is no music at that index");
            ctx.getEvent().replyEmbeds(embedBuilder.build()).queue();
            return;
        }

        embedBuilder.setTitle("[Success] Skipped the track");
        embedBuilder.addField("" , "Name: " + audioTrack.getInfo().title, false);
        embedBuilder.addField("" , "URL: " + audioTrack.getInfo().uri, false);
        ctx.getEvent().replyEmbeds(embedBuilder.build()).queue();
    }
    @Override
    public String getName() {
        return "skipindex";
    }

    @Override
    public String getDescription() {
        return "Skips the current music, if there is any";
    }
}
