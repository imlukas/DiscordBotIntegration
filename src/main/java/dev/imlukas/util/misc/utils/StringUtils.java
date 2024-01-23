package dev.imlukas.util.misc.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public final class StringUtils {

    private static final Pattern NUMERIC_PATTERN = Pattern.compile("[0-9]+");
    private static final Pattern DECIMAL_PATTERN = Pattern.compile("[0-9]+\\.[0-9]+");
    private StringUtils() {

    }

    public static String createRandomString(int length) {
        Random random = ThreadLocalRandom.current();

        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        byte[] bytes = new byte[length];

        random.nextBytes(bytes);

        // transform into char[], no need for StringBuilder
        char[] chars = new char[length];

        for (int index = 0; index < length; index++) {
            // clamp the byte to 0-255, then mod it to the charset length so that it doesn't go out of bounds,
            // and just wraps around
            chars[index] = characters.charAt((bytes[index] & 0xFF) % characters.length());
        }

        return new String(chars);
    }

    public static boolean isNumeric(String str) {
        return DECIMAL_PATTERN.matcher(str).matches();
    }

    public static boolean isInteger(String str) {
        return NUMERIC_PATTERN.matcher(str).matches();
    }

    public static String attachURL(Object text, String url) {
        return "[" + text + "](" + url + ")";
    }

}