package dev.imlukas.utils;

import net.dv8tion.jda.api.EmbedBuilder;

public class EmbedBuilders {

    public static EmbedBuilder getMusicEmbed() {
        return new EmbedBuilder().setColor(Colors.EMBED_BURGUNDY).setFooter("Verux Music").clearFields();
    }
}
