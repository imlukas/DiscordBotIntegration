package me.imlukas.database.json.data;

import lombok.Data;

public class GuildStorage {

    @Data
    private static class Preferences {
        private String ticketChannel;
        private String ticketTitle;
        private String ticketDescription;
    }

    @Data
    public static class Information {
        private int members;
        private int guildId;
    }
}
