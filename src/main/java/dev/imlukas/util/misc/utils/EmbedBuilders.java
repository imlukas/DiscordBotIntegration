package dev.imlukas.util.misc.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class EmbedBuilders {

    public static EmbedBuilder getMusicEmbed() {
        return new EmbedBuilder().setColor(Colors.GREEN.getColor()).setFooter("Verux").clearFields();
    }

    public static EmbedBuilder createBuilder(Colors color, String... fields) {
        EmbedBuilder builder = new EmbedBuilder().setColor(color.getColor()).setFooter("Verux").clearFields();
        for (String field : fields) {
            builder.addField(field, "", false);
        }
        return builder;
    }
    public static EmbedBuilder success(String title) {
        return success(title, Colors.GREEN);
    }
    public static EmbedBuilder success(String title, Colors color) {
        return new EmbedBuilder().setTitle("[Success] " + title).setColor(color.getColor()).setFooter("Verux");
    }

    public static EmbedBuilder error(String title) {
        return error(title, Colors.BURGUNDY);
    }

    public static EmbedBuilder error(String title, Colors color) {
        return new EmbedBuilder().setTitle("[Error] " + title).setColor(color.getColor()).setFooter("Verux");
    }
}
