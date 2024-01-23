package dev.imlukas.util.misc.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class MusicUtil {

    private MusicUtil() {}

    public static String getFormattedDuration(AudioTrack track) {
        double duration = (track.getInfo().length / 1000d);
        int minutes = (int) (duration / 60d);
        int seconds = (int) (duration % 60d);

        String formattedTime = minutes + ":" + seconds;
        if (seconds < 10) {
            formattedTime = minutes + ":0" + seconds;
        }

        return formattedTime;
    }

    public static boolean isTooLong(AudioTrack track) {
        double duration = (track.getInfo().length / 1000d);
        int minutes = (int) (duration / 60d);
        return minutes > 10;
    }
}
