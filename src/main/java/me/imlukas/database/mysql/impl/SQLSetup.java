package me.imlukas.database.mysql.impl;

import lombok.AccessLevel;
import lombok.Getter;
import me.imlukas.Bot;
import me.imlukas.utils.SQLConnectionProvider;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import static me.imlukas.database.mysql.impl.SQLQueries.ADD_USER;
import static me.imlukas.database.mysql.impl.SQLQueries.CREATE_SERVER_TABLE;

public class SQLSetup extends SQLConnectionProvider {

    private static final String[] TABLES = {
            CREATE_SERVER_TABLE
    };
    private final Bot main;
    private final String host, database, username, password;
    private final int port;

    public SQLSetup(Bot main, String host, String database, String username, String password, int port) {
        this.main = main;
        this.host = "localhost";
        this.username = "root";
        this.password = "";
        this.port = 3306;
        this.database = "veruxbot";
    }

    @Override
    public void load() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);


            connection.setAutoCommit(true);
            System.out.println("[VeruxBot] Connected to MySQL server.");
        } catch (Exception e) {
            System.out.println("[VeruxBot] Failed to connect to MySQL server.");
        }
    }

    public void setupServer(Guild guild){
        createTable(guild);
        // Handle members
        List<Member> guildMembers = guild.getMembers();
        for (Member member : guildMembers) {
            if (member.getUser().isBot()) {
                continue;
            }
            addUser(guild, member.getUser());
        }
    }

    public void addUser(Guild guild, User user) {

        System.out.println("Adding user " + user.getName() + " with ID: "+ user.getId() + " to " + guild.getName() + " with ID: " + guild.getId());
        long userID = user.getIdLong();
        CompletableFuture.runAsync(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER.replaceFirst("\\?", "server_" + guild.getId()));

                preparedStatement.setLong(1, userID);
                preparedStatement.setString(2, user.getName());

                preparedStatement.executeUpdate();
                System.out.println("Added user " + user.getName() + " with ID: "+ user.getId() + " to " + guild.getName() + " with ID: " + guild.getId());
            } catch (SQLException e) {
                System.out.println("Failed to add user " + user.getName() + " with ID: "+ user.getId() + " to " + guild.getName() + " with ID: " + guild.getId());
            }
        });
    }
    public void createTable(Guild guild) {
        CompletableFuture.runAsync(() -> {
            try {
                PreparedStatement statement = connection.prepareStatement(CREATE_SERVER_TABLE
                        .replaceFirst("\\?", "server_" + guild.getId()));
                statement.executeUpdate();
            } catch (Exception e) {
                System.out.println("[VeruxBot] Failed to create table for server " + guild.getName());
            }
        });
        System.out.println("[VeruxBot] Created table for server " + guild.getName());
    }

    public void updateTables() {
        List<Guild> guilds = main.getShardManager().getGuilds();
        CompletableFuture.runAsync(() -> {
            for (Guild guild : guilds) {
                try {
                    for (String query : TABLES) {
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "" + guild.getId());
                        preparedStatement.execute();
                    }
                } catch (Exception e) {
                    System.out.println("[VeruxBot] Failed to update tables");
                }
            }
        });
    }
}
