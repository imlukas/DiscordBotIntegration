package me.imlukas.slashcommands.commands.others.git.data;

/*
{
  "login": "imlukas",
  "id": 77332031,
  "node_id": "MDQ6VXNlcjc3MzMyMDMx",
  "avatar_url": "https://avatars.githubusercontent.com/u/77332031?v=4",
  "gravatar_id": "",
  "url": "https://api.github.com/users/imlukas",
  "html_url": "https://github.com/imlukas",
  "followers_url": "https://api.github.com/users/imlukas/followers",
  "following_url": "https://api.github.com/users/imlukas/following{/other_user}",
  "gists_url": "https://api.github.com/users/imlukas/gists{/gist_id}",
  "starred_url": "https://api.github.com/users/imlukas/starred{/owner}{/repo}",
  "subscriptions_url": "https://api.github.com/users/imlukas/subscriptions",
  "organizations_url": "https://api.github.com/users/imlukas/orgs",
  "repos_url": "https://api.github.com/users/imlukas/repos",
  "events_url": "https://api.github.com/users/imlukas/events{/privacy}",
  "received_events_url": "https://api.github.com/users/imlukas/received_events",
  "type": "User",
  "site_admin": false,
  "name": "Lukas Pinheiro",
  "company": null,
  "blog": "",
  "location": "Portugal",
  "email": null,
  "hireable": null,
  "bio": "developer, i guess",
  "twitter_username": null,
  "public_repos": 12,
  "public_gists": 0,
  "followers": 1,
  "following": 4,
  "created_at": "2021-01-12T11:57:43Z",
  "updated_at": "2023-01-06T01:33:11Z"
}

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
