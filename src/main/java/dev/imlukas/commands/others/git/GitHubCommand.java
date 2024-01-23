package dev.imlukas.commands.others.git;


import com.google.gson.Gson;
import lombok.SneakyThrows;
import dev.imlukas.util.command.SlashCommand;
import dev.imlukas.util.command.SlashCommandContext;
import dev.imlukas.util.command.annotations.Option;
import dev.imlukas.util.command.annotations.SubCommand;
import dev.imlukas.commands.others.git.data.GitUser;
import dev.imlukas.commands.others.git.data.Repository;
import dev.imlukas.util.misc.utils.JSONParser;
import dev.imlukas.util.misc.utils.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.json.JSONObject;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class GitHubCommand implements SlashCommand {

    @SneakyThrows
    @SubCommand(name = "repository", description = "Get info on a repository")
    public void repository(@Option(name = "repository", description = "The repository to get info on", type = OptionType.STRING, required = true) String repository, SlashCommandContext ctx) {


        URL gitURL = null;
        try {
            gitURL = new URL("https://api.github.com/repos/" + repository);
        } catch (MalformedURLException e) {
            ctx.getEvent().reply("Invalid repository name. (Use username/repo-name)").queue();
        }

        JSONObject jsonObject = JSONParser.getJsonObject(gitURL);

        Gson gson = new Gson();

        if (jsonObject == null) {
            ctx.getEvent().reply("Invalid repository name. (Use username/repo-name)").queue();
            return;
        }

        Repository repo = gson.fromJson(jsonObject.toString(), Repository.class);

        EmbedBuilder embed = new EmbedBuilder();
        // Repository
        URL repoURL = repo.getRepoURL();
        int forks = repo.getForks();
        int openIssues = repo.getIssues();
        int starred = repo.getStarred();

        // User
        Repository.Owner owner = repo.getOwner();
        String ownerName = owner.getName();
        String avatarURL = owner.getAvatar();
        String userUrl = owner.getUserURL();

        embed.setAuthor(ownerName, userUrl, avatarURL);
        embed.setColor(Color.decode("#6cc644"));
        embed.setThumbnail(avatarURL);

        embed.addField("Repository", StringUtils.attachURL(repo.getName(), repo.getRepoURLAsString()), true);
        embed.addField("Most Used Language", repo.getLanguage(), true);
        embed.addBlankField(true);

        if (forks > 0) {
            embed.addField("Forks", forks + "", true);
        }
        if (openIssues > 0) {

            String issues = "https://github.com/" + repository + "/issues";

            embed.addField("Open Issues", StringUtils.attachURL(openIssues, issues) + "", true);
        }
        if (starred > 0) {
            embed.addField("Stars", starred + "", true);
        }

        embed.setFooter("Repo created at " + repo.getCreationDate());

        ctx.getEvent().replyEmbeds(embed.build()).queue();

    }

    @SubCommand(name = "user", description = "Get info on a user")
    public void user(@Option(name = "user", description = "The user to get info on", type = OptionType.STRING, required = true) String user, SlashCommandContext ctx) {
        URL gitURL = null;
        try {
            gitURL = new URL("https://api.github.com/users/" + user);
        } catch (MalformedURLException e) {
            ctx.getEvent().reply("Invalid user name.").queue();
        }

        JSONObject jsonObject = JSONParser.getJsonObject(gitURL);
        Gson gson = new Gson();

        GitUser gitUser = gson.fromJson(jsonObject.toString(), GitUser.class);

        EmbedBuilder embed = new EmbedBuilder();

        embed.setAuthor(gitUser.getName(), gitUser.getURL(), gitUser.getAvatar());

        // Data
        embed.addField("Name", gitUser.getName(), true);
        embed.addField("Location", gitUser.getLocation(), true);
        embed.addBlankField(true);

        // Repos and gists
        embed.addField("Public Repos", gitUser.getRepos() + "", true);
        embed.addField("Public Gists", gitUser.getGists() + "", true);
        embed.addBlankField(true);


        embed.setThumbnail(gitUser.getAvatar());
        embed.setFooter("User created at " + gitUser.getCreationDate());
        embed.setColor(Color.decode("#6cc644"));

        ctx.getEvent().replyEmbeds(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "github";
    }

    @Override
    public String getDescription() {
        return "A way to quickly access github information";
    }
}
