package me.awli.ramnagon.gfx;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Screen {
    public BufferedImage image;
    public int[] pixels;
    public int width;
    public int height;

    public Screen(int width, int height) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        this.width = width;
        this.height = height;
    }

    // TODO: Flip X and Y
    public void draw(Texture texture, int xPosition, int yPosition) {
        int xStart = xPosition;
        int xEnd = xPosition + texture.WIDTH;
        int yStart = yPosition;
        int yEnd = yPosition + texture.HEIGHT;

        if (xStart < 0)
            xStart = 0;
        if (yStart < 0)
            yStart = 0;
        if (xEnd > width)
            xEnd = width;
        if (yEnd > height)
            yEnd = height;

        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                int pixelColor = texture.pixels[(y - yPosition) * texture.WIDTH - xPosition + x];
                if (pixelColor < 0)
                    pixels[x + y * width] = pixelColor;
            }
        }
    }
}
