package dev.imlukas.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.imlukas.commands.music.manager.GuildMusicManager;
import dev.imlukas.commands.music.manager.PlayerManager;
import dev.imlukas.util.command.SlashCommand;
import dev.imlukas.util.command.SlashCommandContext;
import dev.imlukas.util.command.annotations.SlashCommandHandler;
import dev.imlukas.util.misc.utils.Colors;
import dev.imlukas.util.misc.utils.EmbedBuilders;
import dev.imlukas.util.misc.utils.MusicUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

public class NowPlayingCommand implements SlashCommand {

    @SlashCommandHandler
    public void run(SlashCommandContext ctx) {
        Guild guild = ctx.getGuild();
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getMusicManager(guild);

        if (musicManager == null) {
            return;
        }

        AudioTrack track = musicManager.getPlayer().getPlayingTrack();


        if (track == null) {
            ctx.replyEmbed(EmbedBuilders.error("There is no music playing", Colors.BURGUNDY).build());
            return;
        }

        EmbedBuilder embed = EmbedBuilders.getMusicEmbed();

        String formattedTime = MusicUtil.getFormattedDuration(track);
        embed.setTitle("Now Playing");
        embed.setDescription("Title: " + track.getInfo().title);
        embed.addField("", "Duration: " + formattedTime + " Minutes", false);

        ctx.replyEmbed(embed.build());
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
