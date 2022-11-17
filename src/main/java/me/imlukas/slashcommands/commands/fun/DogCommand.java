package me.imlukas.slashcommands.commands.fun;

import lombok.SneakyThrows;
import me.imlukas.slashcommands.ISlashCommand;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.annotations.Option;
import me.imlukas.slashcommands.annotations.SlashCommand;
import me.imlukas.slashcommands.annotations.SlashCommandHandler;
import me.imlukas.utils.Colors;
import me.imlukas.utils.JSONParser;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONObject;

import java.net.URL;

public class DogCommand implements ISlashCommand {

    @SneakyThrows
    @SlashCommandHandler()
    public void run(@Option(name="breed", description="The breed of dog you want to see") String breed,
                    @Option(name="sub-breed", description="The sub-breed of dog you want to see") String subBreed,
                    SlashCommandContext ctx) {
        String sURL = "https://dog.ceo/api/breeds/image/random";
        if (breed != null) {
            sURL = "https://dog.ceo/api/breed/" + breed + "/images/random";
        } else if (subBreed != null) {
            sURL = "https://dog.ceo/api/breed/" + breed + "/" + subBreed + "/images/random";
        }
        JSONObject json = JSONParser.getJsonObject(new URL(sURL));
        if (json == null){
            ctx.getEvent().reply(":x: Something went wrong! \nMake sure you put a correct breed.").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":dog: woof!");
        embed.setColor(Colors.EMBED_PURPLE);

        String dogImage = json.getString("message");

        embed.setImage(dogImage);
        ctx.getEvent().deferReply().addEmbeds(embed.build()).queue();
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
