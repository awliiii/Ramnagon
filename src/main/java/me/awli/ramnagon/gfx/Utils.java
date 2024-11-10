package me.awli.ramnagon.gfx;

public class Utils {
    public static int interpolateColor(int color1, int color2, float factor) {
        int a = (int) ((color1 >> 24 & 0xFF) * (1 - factor) + (color2 >> 24 & 0xFF) * factor);
        int r = (int) ((color1 >> 16 & 0xFF) * (1 - factor) + (color2 >> 16 & 0xFF) * factor);
        int g = (int) ((color1 >> 8 & 0xFF) * (1 - factor) + (color2 >> 8 & 0xFF) * factor);
        int b = (int) ((color1 & 0xFF) * (1 - factor) + (color2 & 0xFF) * factor);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}