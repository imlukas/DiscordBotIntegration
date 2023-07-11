package dev.imlukas.slashcommands.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.imlukas.slashcommands.SlashCommand;
import dev.imlukas.slashcommands.SlashCommandContext;
import dev.imlukas.slashcommands.annotations.SlashCommandHandler;
import dev.imlukas.slashcommands.commands.music.manager.GuildMusicManager;
import dev.imlukas.slashcommands.commands.music.manager.PlayerManager;
import dev.imlukas.utils.EmbedBuilders;
import net.dv8tion.jda.api.EmbedBuilder;

public class SkipCommand implements SlashCommand {
    @SlashCommandHandler
    public void run(SlashCommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getMusicManager(ctx.getGuild());

        AudioTrack audioTrack =  musicManager.getTrackScheduler().nextTrack();
        EmbedBuilder embedBuilder = EmbedBuilders.getMusicEmbed();
        embedBuilder.setTitle("[Success] Skipped the track");
        embedBuilder.addField("" , "Name: " + audioTrack.getInfo().title, false);
        embedBuilder.addField("" , "URL: " + audioTrack.getInfo().uri, false);
        ctx.getEvent().replyEmbeds(embedBuilder.build()).queue();
    }
    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Skips the current music, if there is any";
    }
}
