package me.imlukas.database.mysql.impl;

public class SQLQueries {

    public final static String UPDATE_VALUE = "UPDATE ? SET ? = ? WHERE ID = ?";
    public final static String WIPE_VALUE = "UPDATE ? SET ? = 0 WHERE ID = ?";
    public final static String GET_VALUE = "SELECT ? FROM ? WHERE ID = ?";
    public final static String GET_ORDERED = "SELECT ? FROM ? WHERE ID = ? ORDER BY ? LIMIT ?";
    public final static String CREATE_SERVER_TABLE = "CREATE TABLE if not exists ? (ID BIGINT UNIQUE NOT NULL, Name VARCHAR(255) NOT NULL, XP INT DEFAULT 0);";
    public final static String CREATE_SERVER_PREFERENCES_TABLE = "CREATE TABLE if not exists ? (ID BIGINT UNIQUE NOT NULL, Name VARCHAR(255) NOT NULL, XP INT DEFAULT 0);";
    public final static String ADD_USER = "INSERT INTO ? (ID, Name) VALUES (?, ?);";
}
