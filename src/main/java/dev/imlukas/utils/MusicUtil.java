package dev.imlukas.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class MusicUtil {

    public static String getTimeString(AudioTrack track) {
        double duration = ((double) track.getInfo().length / 1000d);
        int minutes = (int) (duration / 60d);
        int seconds = (int) (duration % 60d);

        String formattedTime = minutes + ":" + seconds;
        if (seconds < 10) {
            formattedTime = minutes + ":0" + seconds;
        }

        return formattedTime;
    }

    public static boolean isTooLong(AudioTrack track) {
        double duration = ((double) track.getInfo().length / 1000d);
        int minutes = (int) (duration / 60d);
        return minutes > 10;
    }
}
