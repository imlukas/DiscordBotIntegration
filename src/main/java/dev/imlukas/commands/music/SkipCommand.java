package dev.imlukas.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.imlukas.util.command.SlashCommand;
import dev.imlukas.util.command.SlashCommandContext;
import dev.imlukas.util.command.annotations.SlashCommandHandler;
import dev.imlukas.commands.music.manager.GuildMusicManager;
import dev.imlukas.commands.music.manager.PlayerManager;
import dev.imlukas.util.misc.utils.EmbedBuilders;
import net.dv8tion.jda.api.EmbedBuilder;

public class SkipCommand implements SlashCommand {
    @SlashCommandHandler
    public void run(SlashCommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getMusicManager(ctx.getGuild());

        AudioTrack audioTrack =  musicManager.getTrackQueue().nextTrack();

        if (audioTrack == null) {
            ctx.replyEmbed(EmbedBuilders.error("There is no track to skip").build());
            return;
        }

        EmbedBuilder embedBuilder = EmbedBuilders.getMusicEmbed();
        embedBuilder.setTitle("[Success] Skipped the track");
        embedBuilder.addField("" , "Name: " + audioTrack.getInfo().title, false);
        embedBuilder.addField("" , "URL: " + audioTrack.getInfo().uri, false);
        ctx.replyEmbed(embedBuilder.build());
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
