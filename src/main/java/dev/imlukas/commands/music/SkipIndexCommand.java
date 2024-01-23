package dev.imlukas.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.imlukas.util.command.SlashCommand;
import dev.imlukas.util.command.SlashCommandContext;
import dev.imlukas.util.command.annotations.Option;
import dev.imlukas.util.command.annotations.SlashCommandHandler;
import dev.imlukas.commands.music.manager.GuildMusicManager;
import dev.imlukas.commands.music.manager.PlayerManager;
import dev.imlukas.util.misc.utils.EmbedBuilders;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class SkipIndexCommand implements SlashCommand {
    @SlashCommandHandler
    public void run(@Option(name = "skipindex", description = "Index of the music to skip", type = OptionType.INTEGER, required = true) int index, SlashCommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getMusicManager(ctx.getGuild());
        index = index - 1;
        AudioTrack audioTrack = musicManager.getTrackQueue().removeTrack(index);
        EmbedBuilder embedBuilder = EmbedBuilders.getMusicEmbed();

        if (audioTrack == null) {
            ctx.replyEmbed(EmbedBuilders.error("There is no track to skip at that index."));
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
        return "Skips the music at specified index, if there is any";
    }
}
