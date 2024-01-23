package dev.imlukas.commands.fun;

import dev.imlukas.util.command.SlashCommand;
import dev.imlukas.util.command.SlashCommandContext;
import dev.imlukas.util.command.annotations.Option;
import dev.imlukas.util.command.annotations.SlashCommandHandler;
import dev.imlukas.util.misc.utils.EmbedBuilders;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static dev.imlukas.util.misc.utils.Colors.PURPLE;

public class RockPaperScissorCommand implements SlashCommand {

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
        EmbedBuilder embedBuilder = EmbedBuilders.createBuilder(PURPLE);

        if (userChoice == botChoice) {
            embedBuilder.setTitle("It's a tie!");
        }
        if (diff == 1 || diff == -2) {
            embedBuilder.setTitle("You won!");
        } else {
            embedBuilder.setTitle("You Lost!");
        }
        embedBuilder.setDescription("You choose: " + MarkdownUtil.bold(choices.get(userChoice)) +
                "\nI choose: " + MarkdownUtil.bold(choices.get(botChoice)));
        ctx.replyEmbed(embedBuilder.build());
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
