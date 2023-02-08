package me.imlukas.slashcommands.commands.xp;

import me.imlukas.Bot;
import me.imlukas.database.mysql.data.ColumnType;
import me.imlukas.database.mysql.data.DataType;
import me.imlukas.database.mysql.impl.SQLHandler;
import me.imlukas.slashcommands.ISlashCommand;
import me.imlukas.slashcommands.SlashCommandContext;
import me.imlukas.slashcommands.annotations.Option;
import me.imlukas.slashcommands.annotations.SlashCommandHandler;
import me.imlukas.slashcommands.annotations.SubCommand;
import me.imlukas.utils.Colors;
import me.imlukas.utils.XpUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class XpCommand implements ISlashCommand {
    private final SQLHandler sqlHandler;

    public XpCommand(Bot main) {
        this.sqlHandler = main.getSqlHandler();
    }

    @SlashCommandHandler
    public void run(SlashCommandContext ctx) {
        // TODO
    }

    @SubCommand(name = "view", description = "View your xp")
    public void view(SlashCommandContext ctx) {
        DataType<Integer> xpData = new DataType<>(ColumnType.INT, "xp");

        sqlHandler.fetch(xpData, ctx.getGuild(), ctx.getUser()).thenAccept((xp) -> {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("XP");
            builder.setDescription("Your XP: " + xp);
            builder.addField("Level", "" +  XpUtil.getLevelFromXp(xp), true);
            builder.addField("XP to next level", "" + XpUtil.getXpNeededForLevel(xp), true);
            builder.setColor(Colors.EMBED_PURPLE);

            ctx.getEvent().replyEmbeds(builder.build()).queue();
        });

    }
    @SubCommand(name = "give", description = "Give xp to a user")
    public void give(@Option(name = "user", description = "The user to give xp to", type = OptionType.USER) User user,
                     @Option(name = "amount", description = "The amount of xp to give", type = OptionType.INTEGER) int amount,
                     SlashCommandContext ctx) {

        DataType<Integer> xpData = new DataType<>(ColumnType.INT, "xp");

        sqlHandler.addXp(xpData, amount, ctx.getGuild(), user);
        ctx.getEvent().reply("Gave " + amount + " xp to " + user.getAsTag()).queue();
        // TODO
    }
    @SubCommand(name = "xpneeded", description = "View xp needed for a level")
    public void xpNeeded(@Option(name = "level", description = "The level you want to know the xp for", required = true, type = OptionType.INTEGER) int level,
                         SlashCommandContext ctx) {

        DataType<Integer> xpData = new DataType<>(ColumnType.INT, "xp");
        int xp = sqlHandler.fetch(xpData, ctx.getGuild(), ctx.getUser()).join();
        int userLevel = XpUtil.getLevelFromXp(xp);


        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Level " + userLevel);
        builder.addField("XP needed:", "" + XpUtil.getXpToLevel(userLevel), false);
        if (userLevel >= level){
            builder.addField("", "You already reached this level! Your current level is " + userLevel, false);
        } else {
            builder.addField("You still need " + XpUtil.getXpNeededForLevel(xp)
                    + " XP to reach level " + userLevel, "", false);
        }
        builder.setColor(Colors.EMBED_PURPLE);
        ctx.getEvent().replyEmbeds(builder.build()).queue();
    }

    @Override
    public String getName() {
        return "xp";
    }

    @Override
    public String getDescription() {
        return "Xp commands";
    }
}
