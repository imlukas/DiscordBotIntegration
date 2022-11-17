package me.imlukas.slashcommands.commands.fun;

import me.imlukas.slashcommands.ISlashCommand;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.annotations.Option;
import me.imlukas.slashcommands.annotations.SlashCommandHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static me.imlukas.utils.Colors.EMBED_PURPLE;

public class RockPaperScissorCommand implements ISlashCommand {

    private final Random random = ThreadLocalRandom.current();

    @SlashCommandHandler
    public void run(@Option(name = "choice", description = "Your choice", required = true) String choice,
                     SlashCommandContext ctx) {
        List<String> choices = List.of("rock", "paper", "scissor");

        int userChoice = choices.indexOf(choice);

        if (userChoice == -1) {
            ctx.getEvent().reply("Invalid choice").queue();
            return;
        }

        int botChoice = random.nextInt(3);


        int diff = userChoice - botChoice;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(EMBED_PURPLE);

        if(userChoice == botChoice) {
            embedBuilder.setTitle("It's a tie!");
            ctx.getEvent().replyEmbeds(embedBuilder.build()).queue();
            return;
        }
        if(diff == 1 || diff == -2) {
            embedBuilder.setTitle("You won!");
        } else {
            embedBuilder.setTitle("You Lost!");
        }
        embedBuilder.setDescription("You chose: " +  MarkdownUtil.bold(choices.get(userChoice)) +
                "\nI choose: " + MarkdownUtil.bold(choices.get(botChoice)));
        ctx.getEvent().replyEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "rps";
    }

    @Override
    public String getDescription() {
        return "Play a rock, paper, scissor game with the bot";
    }
}
