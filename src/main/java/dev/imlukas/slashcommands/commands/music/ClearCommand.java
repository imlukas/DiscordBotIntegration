package dev.imlukas.slashcommands.commands.music;

import dev.imlukas.slashcommands.SlashCommand;
import dev.imlukas.slashcommands.SlashCommandContext;
import dev.imlukas.slashcommands.annotations.SlashCommandHandler;
import dev.imlukas.slashcommands.commands.music.manager.GuildMusicManager;
import dev.imlukas.slashcommands.commands.music.manager.PlayerManager;
import dev.imlukas.utils.EmbedBuilders;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

public class ClearCommand implements SlashCommand {
    @SlashCommandHandler
    public void run(SlashCommandContext ctx) {
        PlayerManager playerManager = PlayerManager.getInstance();
        Guild guild = ctx.getEvent().getGuild();

        if (guild == null) {
            return;
        }

        GuildMusicManager musicManager = playerManager.getMusicManager(guild);
        EmbedBuilder embedBuilder = EmbedBuilders.getMusicEmbed();

        if (musicManager == null) {
            embedBuilder.setTitle("[Error] There is no music playing");
            ctx.getEvent().replyEmbeds(embedBuilder.build()).queue();
            return;
        }

        musicManager.getTrackScheduler().clear();

        embedBuilder.setTitle("[Success] Cleared the queue");
        ctx.getEvent().replyEmbeds(embedBuilder.build()).queue();
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
