package dev.imlukas.commands.music;

import dev.imlukas.util.command.SlashCommand;
import dev.imlukas.util.command.SlashCommandContext;
import dev.imlukas.util.command.annotations.SlashCommandHandler;
import dev.imlukas.commands.music.manager.GuildMusicManager;
import dev.imlukas.commands.music.manager.PlayerManager;
import dev.imlukas.util.misc.utils.Colors;
import dev.imlukas.util.misc.utils.EmbedBuilders;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

public class ClearCommand implements SlashCommand {
    @SlashCommandHandler
    public void run(SlashCommandContext ctx) {
        PlayerManager playerManager = PlayerManager.getInstance();
        Guild guild = ctx.getGuild();

        if (guild == null) {
            return;
        }

        GuildMusicManager musicManager = playerManager.getMusicManager(guild);

        if (musicManager == null) {
            ctx.replyEmbed(EmbedBuilders.error("There is no music playing", Colors.BURGUNDY).build());
            return;
        }

        musicManager.clearQueue();
        ctx.replyEmbed(EmbedBuilders.success("Cleared the queue", Colors.GREEN).build());
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "Stops the music and clears the queue";
    }
}
