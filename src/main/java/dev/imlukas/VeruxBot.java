package dev.imlukas;

import dev.imlukas.database.json.JSONFileHandler;
import dev.imlukas.listeners.AutoDisconnectListener;
import dev.imlukas.listeners.EnterLeaveListener;
import dev.imlukas.listeners.GuildJoinReadyListener;
import dev.imlukas.listeners.SlashCommandListener;
import dev.imlukas.util.command.SlashCommandManager;
import dev.imlukas.commands.admin.AdminCommand;
import dev.imlukas.commands.admin.PreferencesCommand;
import dev.imlukas.commands.admin.listener.AutoCompleteListener;
import dev.imlukas.commands.fun.CatCommand;
import dev.imlukas.commands.fun.DogCommand;
import dev.imlukas.commands.fun.RockPaperScissorCommand;
import dev.imlukas.commands.member.AvatarCommand;
import dev.imlukas.commands.member.BanSlashCommand;
import dev.imlukas.commands.member.UnbanSlashCommand;
import dev.imlukas.commands.music.*;
import dev.imlukas.commands.others.git.GitHubCommand;
import dev.imlukas.commands.server.RolesCommand;
import dev.imlukas.commands.server.ServerCommand;
import dev.imlukas.commands.ticket.TicketCommand;
import dev.imlukas.commands.ticket.listener.ButtonHandler;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import se.michaelthelin.spotify.SpotifyApi;

import javax.security.auth.login.LoginException;

import static dev.imlukas.secrets.DISCORD_TOKEN;
import static dev.imlukas.secrets.SPOTIFY_TOKEN;

@Getter
public class VeruxBot {

    private final ShardManager shardManager;
    private final SlashCommandManager slashCommandManager;
    private final JSONFileHandler jsonFileHandler;


    public static final SpotifyApi SPOTIFY_API = new SpotifyApi.Builder()
            .setAccessToken(SPOTIFY_TOKEN)
            .build();

    public VeruxBot() throws LoginException {
        jsonFileHandler = new JSONFileHandler();
        slashCommandManager = new SlashCommandManager();
        registerCommands();
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(DISCORD_TOKEN)
                .setActivity(Activity.listening("your commands"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS)
                .enableCache(CacheFlag.ONLINE_STATUS)
                .addEventListeners(
                        new ButtonHandler(),
                        new EnterLeaveListener(this),
                        new AutoDisconnectListener(),
                        new GuildJoinReadyListener(this),
                        new SlashCommandListener(this),
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
        slashCommandManager.registerCommand(new TicketCommand(this));
        slashCommandManager.registerCommand(new AdminCommand(this));
        slashCommandManager.registerCommand(new GitHubCommand());
        slashCommandManager.registerCommand(new PreferencesCommand(this));

        // Music
        slashCommandManager.registerCommand(new PlayCommand());
        slashCommandManager.registerCommand(new SkipCommand());
        slashCommandManager.registerCommand(new SkipIndexCommand());
        slashCommandManager.registerCommand(new QueueCommand());
        slashCommandManager.registerCommand(new PauseCommand());
        slashCommandManager.registerCommand(new ClearCommand());
        slashCommandManager.registerCommand(new NowPlayingCommand());
    }

    public ShardManager getShardManager() {
        return this.shardManager;
    }

    public static void main(String[] args) {
        try {
            new VeruxBot();
        } catch (LoginException e) {
            System.out.println("Invalid token");
        }
    }

}
