package me.imlukas;

import lombok.Getter;
import me.imlukas.database.mysql.impl.SQLHandler;
import me.imlukas.database.mysql.impl.SQLSetup;
import me.imlukas.listeners.*;
import me.imlukas.slashcommands.SlashCommandManager;
import me.imlukas.slashcommands.commands.admin.AdminCommand;
import me.imlukas.slashcommands.commands.fun.CatCommand;
import me.imlukas.slashcommands.commands.fun.DogCommand;
import me.imlukas.slashcommands.commands.fun.RockPaperScissorCommand;
import me.imlukas.slashcommands.commands.member.AvatarCommand;
import me.imlukas.slashcommands.commands.member.BanSlashCommand;
import me.imlukas.slashcommands.commands.member.UnbanSlashCommand;
import me.imlukas.slashcommands.commands.server.RolesCommand;
import me.imlukas.slashcommands.commands.server.ServerCommand;
import me.imlukas.slashcommands.commands.xp.XpCommand;
import me.imlukas.userxp.listener.UserMessageListener;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

import static me.imlukas.config.secrets.TOKEN;

@Getter
public class Bot {

    private final ShardManager shardManager;
    private final SlashCommandManager slashCommandManager;
    private final SQLSetup sqlSetup;
    private final SQLHandler sqlHandler;

    public Bot() throws  LoginException{
        sqlSetup = new SQLSetup(this, "db4free.net", "mobkilltrade", "putolukas", "mobkilltrade", 3306);
        sqlHandler = new SQLHandler(this);
        slashCommandManager = new SlashCommandManager();
        registerCommands();
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(TOKEN)
                .setActivity(Activity.listening("your commands"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS)
                .enableCache(CacheFlag.ONLINE_STATUS)
                .addEventListeners(new EnterLeaveListener(), new GuildJoinReadyListener(this), new SlashCommandListener(this), new UserMessageListener(this))
                .setMemberCachePolicy(MemberCachePolicy.ALL);
        shardManager = builder.build();



    }

    private void registerCommands(){
        slashCommandManager.registerCommand(new BanSlashCommand());
        slashCommandManager.registerCommand(new UnbanSlashCommand());
        slashCommandManager.registerCommand(new AvatarCommand());
        slashCommandManager.registerCommand(new RolesCommand());
        slashCommandManager.registerCommand(new ServerCommand());
        slashCommandManager.registerCommand(new RockPaperScissorCommand());
        slashCommandManager.registerCommand(new CatCommand());
        slashCommandManager.registerCommand(new DogCommand());
        slashCommandManager.registerCommand(new XpCommand(this));
        slashCommandManager.registerCommand(new AdminCommand(this));
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
