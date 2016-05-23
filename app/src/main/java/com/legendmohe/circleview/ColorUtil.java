package com.legendmohe.circleview;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by legendmohe on 16/5/23.
 */
public class ColorUtil {
    private static final Random sRandom = new Random();

    public static int getContrastColor(int color) {
        double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }

    public static int getComplementaryColor(int colorToInvert) {
        float[] hsv = new float[3];
        Color.RGBToHSV(Color.red(colorToInvert), Color.green(colorToInvert),
                Color.blue(colorToInvert), hsv);
        hsv[0] = (hsv[0] + 180) % 360;
        return Color.HSVToColor(hsv);
    }

    public static int getContrastVersionForColor(int color) {
        float[] hsv = new float[3];
        Color.RGBToHSV(Color.red(color), Color.green(color), Color.blue(color),
                hsv);
        if (hsv[2] < 0.5) {
            hsv[2] = 0.7f;
        } else {
            hsv[2] = 0.3f;
        }
        hsv[1] = hsv[1] * 0.2f;
        return Color.HSVToColor(hsv);
    }

    public static int randomColor() {
        int color = Color.argb(255, sRandom.nextInt(256), sRandom.nextInt(256), sRandom.nextInt(256));
        return color;
    }
}
