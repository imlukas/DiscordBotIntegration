package me.imlukas.database.mysql.impl;

import me.imlukas.Bot;
import me.imlukas.database.mysql.data.DataType;
import me.imlukas.database.mysql.data.OrderType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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

    public void addXp(DataType value, int newValue, Guild guild, User user){
        long userId = user.getIdLong();

        fetch(value, guild, user).thenCompose((oldXp) -> {

            System.out.println("add xp' old xp: " + oldXp);
            int newXp = (int) oldXp + newValue;
            try {

                PreparedStatement statement = connection.prepareStatement(UPDATE_VALUE.replaceFirst("\\?", getTable(guild))
                        .replaceFirst("\\?", value.getName()));
                statement.setInt(1,  newXp);
                statement.setLong(2, userId);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public void wipeUser(DataType type, Guild guild, long id){
        CompletableFuture.runAsync(() -> {
            try {

                PreparedStatement statement = connection.prepareStatement(WIPE_VALUE.replaceFirst("\\?", getTable(guild))
                        .replaceFirst("\\?", type.getName()));
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

    public CompletableFuture<List<Integer>> getOrdered(Guild guild, int entries, String value, OrderType orderType){
        return null;
    }

    public CompletableFuture<?> fetch(DataType value, Guild guild, User user) {

        String table = getTable(guild);

        return CompletableFuture.supplyAsync(() -> {
            String query = GET_VALUE.replaceFirst("\\?", value.getName());
            query = query.replaceFirst("\\?", table);
            try {
                PreparedStatement statement = connection.prepareStatement(query);
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
