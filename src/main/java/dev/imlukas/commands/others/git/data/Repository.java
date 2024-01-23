package dev.imlukas.commands.others.git.data;

import lombok.Data;

import java.net.MalformedURLException;
import java.net.URL;

@Data
public class Repository {

    private String name;
    private String html_url;
    private String language;
    private String created_at; // 2021-01-01T00:00:00Z
    private Owner owner;
    private int forks;
    private int stargazers_count; // stars
    private int open_issues;


    public String getRepoURLAsString() {
        return html_url;
    }

    public URL getRepoURL() {
        try {
            return new URL(html_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCreationDate() {
        return created_at
                .replace("T", " ")
                .replace("Z", "")
                .replaceAll("-", "/");
    }

    public int getIssues() {
        return open_issues;
    }

    public int getStarred() {
        return stargazers_count;
    }

    @Data
    public static class Owner {
        private String login;
        private String avatar_url;
        private String html_url;


        public String getName() {
            return login;
        }

        public String getAvatar() {
            return avatar_url;
        }

        public String getUserURL() {
            return html_url;
        }
    }
}
