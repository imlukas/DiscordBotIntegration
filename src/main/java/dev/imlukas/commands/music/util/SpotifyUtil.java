package dev.imlukas.commands.music.util;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import java.io.IOException;

import static dev.imlukas.VeruxBot.SPOTIFY_API;

public class SpotifyUtil {

    public static Track getTrack(String url) {
        String songId = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));

        GetTrackRequest getTrackRequest = SPOTIFY_API.getTrack(songId).build();

        Track track = null;
        try {
            track = getTrackRequest.execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return track;
    }

    public static String getTrackName(String url) {
        Track track = getTrack(url);

        if (track == null) {
            return null;
        }

        return track.getName();
    }
}
