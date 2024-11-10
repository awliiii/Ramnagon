package me.awli.ramnagon.gfx;

import me.awli.ramnagon.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Textures {
    public static final Texture[][] FONT = loadAndCut("/textures/ui/monogram-bitmap.png", 6, 10);
    public static final Texture[][] TILES = loadAndCut("/textures/tiles.png", 32, 32);
    public static final Texture SINGLE_TREE = load("/textures/entities/deco/tree_single.png");
    public static final Texture DOUBLE_TREE = load("/textures/entities/deco/tree_double.png");
    public static final Texture INVENTORY = load("/textures/ui/inventory.png");

    public static Texture[][] loadAndCut(String name, int sw, int sh) {
        BufferedImage img;
        try {
            img = ImageIO.read(Game.class.getResource(name));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + name);
        }

        int xSlices = img.getWidth() / sw;
        int ySlices = img.getHeight() / sh;

        Texture[][] result = new Texture[xSlices][ySlices];
        for (int x = 0; x < xSlices; x++) {
            for (int y = 0; y < ySlices; y++) {
                result[x][y] = new Texture(sw, sh);
                img.getRGB(x * sw, y * sh, sw, sh, result[x][y].pixels, 0, sw);
            }
        }
        return result;
    }

    public static Texture load(String path) {
        BufferedImage image;
        try {
            image = ImageIO.read(Game.class.getResource(path));
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load " + path);
        }

        int width = image.getWidth();
        int height = image.getHeight();
        Texture texture = new Texture(width, height);
        image.getRGB(0, 0, width, height, texture.pixels, 0, width);

        return texture;
    }
}
