package me.imlukas;

import me.imlukas.listeners.EnterLeaveListener;
import me.imlukas.listeners.MessageListener;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

import static me.imlukas.config.secrets.TOKEN;


public class Bot {

    private final ShardManager shardManager;

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
