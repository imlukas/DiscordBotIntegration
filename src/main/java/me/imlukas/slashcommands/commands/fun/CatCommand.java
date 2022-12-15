package me.imlukas.slashcommands.commands.fun;

import lombok.SneakyThrows;
import me.imlukas.slashcommands.ISlashCommand;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.annotations.Option;
import me.imlukas.slashcommands.annotations.SlashCommandHandler;
import me.imlukas.utils.Colors;
import me.imlukas.utils.JSONParser;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class CatCommand implements ISlashCommand {

    @SneakyThrows
    @SlashCommandHandler()
    public void run(@Option(name="breed", description="The breed of cat you want to see") String breed,
                    SlashCommandContext ctx) {
        String sURL = "https://api.thecatapi.com/v1/images/search";
        if (breed != null) {
            String breedID = breed.substring(0, 4);
            sURL = "https://api.thecatapi.com/v1/images/search?breed_ids=" + breedID;
        }
        JSONArray json = JSONParser.getJsonArray(new URL(sURL));

        if (json == null){
            ctx.getEvent().reply(":x: Something went wrong! \nMake sure you put a correct breed.").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(":cat: Meowww!");
        embed.setColor(Colors.EMBED_PURPLE);

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
