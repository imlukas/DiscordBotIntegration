package dev.imlukas.commands.fun;

import dev.imlukas.util.misc.utils.EmbedBuilders;
import lombok.SneakyThrows;
import dev.imlukas.util.command.SlashCommand;
import dev.imlukas.util.command.SlashCommandContext;
import dev.imlukas.util.command.annotations.Option;
import dev.imlukas.util.command.annotations.SlashCommandHandler;
import dev.imlukas.util.misc.utils.Colors;
import dev.imlukas.util.misc.utils.JSONParser;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONObject;

import java.net.URL;

import static dev.imlukas.util.misc.utils.Colors.PURPLE;

public class DogCommand implements SlashCommand {

    @SneakyThrows
    @SlashCommandHandler
    public void run(@Option(name = "breed", description = "The breed of dog you want to see") String breed,
                    @Option(name = "sub-breed", description = "The sub-breed of dog you want to see") String subBreed,
                    SlashCommandContext ctx) {
        String sURL = "https://dog.ceo/api/breeds/image/random";
        if (breed != null) {
            sURL = "https://dog.ceo/api/breed/" + breed + "/images/random";
        } else if (subBreed != null) {
            sURL = "https://dog.ceo/api/breed/" + breed + "/" + subBreed + "/images/random";
        }
        JSONObject json = JSONParser.getJsonObject(new URL(sURL));
        if (json == null) {
            ctx.getEvent().reply(":x: Something went wrong! \nMake sure you put a correct breed.").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embed = EmbedBuilders.createBuilder(PURPLE);
        embed.setTitle(":dog: woof!");

        String dogImage = json.getString("message");

        embed.setImage(dogImage);
        ctx.replyEmbed(embed.build());
    }

    @Override
    public String getName() {
        return "dog";
    }

    @Override
    public String getDescription() {
        return "Get a random dog picture.";
    }
}
