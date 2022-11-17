package me.imlukas.database.mysql.impl;

import me.imlukas.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

import static me.imlukas.database.mysql.impl.SQLQueries.*;

public class SQLHandler {
    private final Bot main;
    private final Connection connection;

    public SQLHandler(Bot main) {
        this.main = main;
        this.connection = main.getSqlSetup().get();
    }

    public String getTable(Guild guild){
        return "server_" + guild.getId();
    }

    public void addXp(Guild guild, int xp, User user){
        long userId = user.getIdLong();

        getXp(guild, user).thenCompose((oldXp) -> {
            int newXp = oldXp + xp;
            try {
                PreparedStatement statement = connection.prepareStatement(UPDATE_XP.replaceFirst("\\?", "server_" + guild.getId()));
                statement.setInt(1,  newXp);
                statement.setLong(2, userId);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public void wipeUser(Guild guild, long id){
        CompletableFuture.runAsync(() -> {
            try {
                PreparedStatement statement = connection.prepareStatement(WIPE_USER.replaceFirst("\\?", getTable(guild)));
                statement.setLong(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void wipeTable(){
        //TODO
    }

    public void getXpOrdered(int entries, OrderType orderType){
        //TODO
    }

    public CompletableFuture<Integer> getXp(Guild guild, User user) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement statement = connection.prepareStatement(GET_XP.replaceFirst("\\?", getTable(guild)));
                statement.setLong(1, user.getIdLong());
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    return rs.getInt("xp");
                }
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });

    }
}
