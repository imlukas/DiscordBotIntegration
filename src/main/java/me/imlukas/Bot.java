package me.imlukas;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import me.imlukas.listeners.EnterLeaveListener;
import me.imlukas.listeners.MessageListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Bot {
    private final ShardManager shardManager;
    private final static String TOKEN = "OTE2MTQ2NDAwMzEwNDg1MDA0.G6P5lz.lWun1-ma2K9O_nQhDf1TM4pRhM25M7_Oi8xnHA";

    public Bot() throws  LoginException{
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(TOKEN)
                .setActivity(Activity.listening("your commands"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS)
                .enableCache(CacheFlag.ONLINE_STATUS)
                .addEventListeners(new MessageListener())
                .addEventListeners(new EnterLeaveListener())
                .setMemberCachePolicy(MemberCachePolicy.ALL);

        shardManager = builder.build();
    }

    public ShardManager getShardManager(){
        return this.shardManager;
    }
    public static void main(String[] args) {
        try {
            Bot bot = new Bot();
        } catch (LoginException e) {
            System.out.println("Invalid token");
        }
    }

}
