package me.imlukas;

import lombok.Getter;
import me.imlukas.database.json.json.JSONFileHandler;
import me.imlukas.database.mysql.impl.SQLHandler;
import me.imlukas.database.mysql.impl.SQLSetup;
import me.imlukas.listeners.EnterLeaveListener;
import me.imlukas.listeners.GuildJoinReadyListener;
import me.imlukas.listeners.SlashCommandListener;
import me.imlukas.slashcommands.SlashCommandManager;
import me.imlukas.slashcommands.commands.admin.AdminCommand;
import me.imlukas.slashcommands.commands.admin.PreferencesCommand;
import me.imlukas.slashcommands.commands.admin.listener.AutoCompleteListener;
import me.imlukas.slashcommands.commands.fun.CatCommand;
import me.imlukas.slashcommands.commands.fun.DogCommand;
import me.imlukas.slashcommands.commands.fun.RockPaperScissorCommand;
import me.imlukas.slashcommands.commands.member.AvatarCommand;
import me.imlukas.slashcommands.commands.member.BanSlashCommand;
import me.imlukas.slashcommands.commands.member.UnbanSlashCommand;
import me.imlukas.slashcommands.commands.music.ClearCommand;
import me.imlukas.slashcommands.commands.music.PlayCommand;
import me.imlukas.slashcommands.commands.music.QueueCommand;
import me.imlukas.slashcommands.commands.music.SkipCommand;
import me.imlukas.slashcommands.commands.others.git.GitHubCommand;
import me.imlukas.slashcommands.commands.server.RolesCommand;
import me.imlukas.slashcommands.commands.server.ServerCommand;
import me.imlukas.slashcommands.commands.ticket.TicketCommand;
import me.imlukas.slashcommands.commands.xp.XpCommand;
import me.imlukas.slashcommands.commands.xp.listener.UserMessageListener;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import se.michaelthelin.spotify.SpotifyApi;

import javax.security.auth.login.LoginException;

import static me.imlukas.config.secrets.DISCORD_TOKEN;
import static me.imlukas.config.secrets.SPOTIFY_TOKEN;

@Getter
public class Bot {

    private final ShardManager shardManager;
    private final SlashCommandManager slashCommandManager;
    private final JSONFileHandler jsonFileHandler;
    private final SQLSetup sqlSetup;
    private final SQLHandler sqlHandler;

    public static final SpotifyApi SPOTIFY_API = new SpotifyApi.Builder()
            .setAccessToken(SPOTIFY_TOKEN)
            .build();

    public Bot() throws LoginException {
        jsonFileHandler = new JSONFileHandler();
        sqlSetup = new SQLSetup(this);
        sqlHandler = new SQLHandler(this);
        slashCommandManager = new SlashCommandManager();
        registerCommands();
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(DISCORD_TOKEN)
                .setActivity(Activity.listening("your commands"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS)
                .enableCache(CacheFlag.ONLINE_STATUS)
                .addEventListeners(
                        new EnterLeaveListener(),
                        new GuildJoinReadyListener(this),
                        new SlashCommandListener(this),
                        new UserMessageListener(this),
                        new AutoCompleteListener(this))
                .setMemberCachePolicy(MemberCachePolicy.ALL);
        shardManager = builder.build();

    }

    private void registerCommands() {
        slashCommandManager.registerCommand(new BanSlashCommand());
        slashCommandManager.registerCommand(new UnbanSlashCommand());
        slashCommandManager.registerCommand(new AvatarCommand());
        slashCommandManager.registerCommand(new RolesCommand());
        slashCommandManager.registerCommand(new ServerCommand());
        slashCommandManager.registerCommand(new RockPaperScissorCommand());
        slashCommandManager.registerCommand(new CatCommand());
        slashCommandManager.registerCommand(new DogCommand());
        slashCommandManager.registerCommand(new XpCommand(this));
        slashCommandManager.registerCommand(new TicketCommand(this));
        slashCommandManager.registerCommand(new AdminCommand(this));
        slashCommandManager.registerCommand(new GitHubCommand());
        slashCommandManager.registerCommand(new PreferencesCommand(this));
        slashCommandManager.registerCommand(new PlayCommand());
        slashCommandManager.registerCommand(new SkipCommand());
        slashCommandManager.registerCommand(new QueueCommand());
        slashCommandManager.registerCommand(new ClearCommand());
    }

    public ShardManager getShardManager() {
        return this.shardManager;
    }

    public static void main(String[] args) {
        try {
            new Bot();
        } catch (LoginException e) {
            System.out.println("Invalid token");
        }
    }

}
