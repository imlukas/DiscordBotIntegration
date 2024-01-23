package dev.imlukas.commands.others.git.data;

/**
 * Data class that represents a GitHub user
 * Used for GSON
 */
public class GitUser {

    private String login;
    private String avatar_url;
    private String html_url;
    private String created_at;
    private String name;
    private String location;
    private String email;
    private int public_repos;
    private int public_gists;
    private int followers;
    private int following;

    public String getLoginName() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar_url;
    }

    public String getURL() {
        return html_url;
    }

    public String getCreationDate() {
        return created_at
                .replace("T", " ")
                .replace("Z", "")
                .replaceAll("-", "/");
    }

    public String getLocation() {
        return location;
    }

    public String getEmail() {
        return email;
    }

    public int getRepos() {
        return public_repos;
    }

    public int getGists() {
        return public_gists;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowing() {
        return following;
    }


}
