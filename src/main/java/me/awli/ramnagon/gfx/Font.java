package me.awli.ramnagon.gfx;

public class Font {
    private static final String CHARS = " !\"#$%&'()*+,-./"
                                        + "0123456789:;<=>?"
                                        + "@ABCDEFGHIJKLMNO"
                                        + "PQRSTUVWXYZ[\\]^_"
                                        + "`abcdefghijklmno"
                                        + "pqrstuvwxyz{|}~";
    
    public static void draw(String text, Screen screen, int x, int y) {
        for (int i = 0; i < text.length(); i++) {
            int ix = CHARS.indexOf(text.charAt(i));
            if (ix >= 0)
                screen.draw(Textures.FONT[ix % 16][ix / 16], x + i * 6, y);
            else // ? character
                screen.draw(Textures.FONT[15][1], x + i * 6, y);
        }
    }

    public static int getTextLength(String text) {
        return text.length() * 6;
    }
}
