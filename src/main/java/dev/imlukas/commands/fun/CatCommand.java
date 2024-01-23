package dev.imlukas.commands.fun;

import lombok.SneakyThrows;
import dev.imlukas.util.command.SlashCommand;
import dev.imlukas.util.command.SlashCommandContext;
import dev.imlukas.util.command.annotations.Option;
import dev.imlukas.util.command.annotations.SlashCommandHandler;
import dev.imlukas.util.misc.utils.Colors;
import dev.imlukas.util.misc.utils.JSONParser;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class CatCommand implements SlashCommand {

    @SneakyThrows
    @SlashCommandHandler()
    public void run(@Option(name = "breed", description = "The breed of cat you want to see") String breed,
                    SlashCommandContext ctx) {
        String sURL = "https://api.thecatapi.com/v1/images/search";
        if (breed != null) {
            String breedID = breed.substring(0, 4);
            sURL = "https://api.thecatapi.com/v1/images/search?breed_ids=" + breedID;
        }
        JSONArray json = JSONParser.getJsonArray(new URL(sURL));

        if (json == null) {
            ctx.getEvent().reply(":x: Something went wrong! \nMake sure you put a correct breed.").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":cat: Meowww!");
        embed.setColor(Colors.PURPLE);

        JSONObject JSONObject = json.getJSONObject(0);
        String catImage = JSONObject.getString("url");
        embed.setImage(catImage);
        ctx.getEvent().deferReply().addEmbeds(embed.build()).queue();
    }


    @Override
    public String getName() {
        return "cat";
    }

    @Override
    public String getDescription() {
        return "Get a random cat picture.";
    }
}
