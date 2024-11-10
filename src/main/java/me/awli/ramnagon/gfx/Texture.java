package me.awli.ramnagon.gfx;

public class Texture {
    public final int WIDTH;
    public final int HEIGHT;
    public int[] pixels;

    public Texture(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.pixels = new int[WIDTH * HEIGHT];
    }
}
