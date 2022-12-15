package me.imlukas.database.mysql.impl;

public class SQLQueries {

    public final static String CREATE_DATABASE = "CREATE DATABASE ?";
    public final static String UPDATE_XP = "UPDATE ? SET XP = ? WHERE ID = ?";
    public final static String WIPE_USER = "UPDATE ? SET XP = 0 WHERE ID = ?";
    public final static String GET_XP = "SELECT XP FROM ? WHERE ID = ?";
    public final static String CREATE_SERVER_TABLE = "CREATE TABLE if not exists ? (ID BIGINT UNIQUE NOT NULL, Name VARCHAR(255) NOT NULL, XP INT DEFAULT 0);";
    public final static String ADD_USER = "INSERT INTO ? (ID, Name) VALUES (?, ?);";
}
