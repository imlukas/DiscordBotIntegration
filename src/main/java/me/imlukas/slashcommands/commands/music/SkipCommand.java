package me.imlukas.slashcommands.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.imlukas.slashcommands.SlashCommand;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.annotations.Option;
import me.imlukas.slashcommands.annotations.SlashCommandHandler;
import me.imlukas.slashcommands.commands.music.manager.GuildMusicManager;
import me.imlukas.slashcommands.commands.music.manager.PlayerManager;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class SkipCommand implements SlashCommand {


    @SlashCommandHandler
    public void run(@Option(name = "index", description = "Index of the music to skip", type = OptionType.INTEGER) int index, SlashCommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getMusicManager(ctx.getGuild());

        if (index > 0) {
            AudioTrack audioTrack = musicManager.getTrackScheduler().removeTrack(index);
            if (audioTrack == null) {
                ctx.getEvent().reply("There is no track in the index " + index).queue();
                return;
            }

            ctx.getEvent().reply("Skipped the track " + audioTrack.getInfo().title).queue();
            return;
        }

        musicManager.getTrackScheduler().nextTrack();
        ctx.getEvent().reply("Skipped the current track").queue();
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
