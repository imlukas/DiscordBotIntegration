package dev.imlukas.util.misc.utils;

import java.awt.*;

public enum Colors {

    PURPLE(new Color(138,43,226)),
    GREEN(new Color(64, 204, 64)),
    BURGUNDY(new Color(128,0,0));

    private final Color color;

    Colors(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
