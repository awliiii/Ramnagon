package me.awli.ramnagon.level;

import me.awli.ramnagon.Camera;
import me.awli.ramnagon.Constants;
import me.awli.ramnagon.Game;
import me.awli.ramnagon.entity.Entity;
import me.awli.ramnagon.gfx.Screen;
import me.awli.ramnagon.gfx.Texture;
import me.awli.ramnagon.gfx.Textures;
import me.awli.ramnagon.gfx.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
 * TODO:
 *  - Independent tile position
 *  - Getter and setters for the entities list
 *  - Blend everything with the skyColor
 */
public class Level {
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;

    public Camera camera;

    private final List<Entity> entities = new ArrayList<>();

    private byte[][] map;

    private long time; // the number of ticks

    private int skyColor = Constants.NIGHT_COLOR;

    public Level() {
        this.camera = new Camera(Game.WIDTH / 2, Game.HEIGHT / 2);

        loadMap("/maps/plains.png");
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void removeEntity(int index) {
        entities.remove(index);
    }

    public Entity getEntity(int index) {
        return entities.get(index);
    }

    public void tick() {
        time++;

        updateSkyColor();

        entities.forEach(Entity::tick);
    }

    public void updateSkyColor() {
        int timeOfTheDay = (int) (time % 2400);

        if (timeOfTheDay < 200) { // dusk
            float factor = (float) timeOfTheDay / 200;
            skyColor = Utils.interpolateColor(Constants.NIGHT_COLOR, Constants.DUSK_COLOR, factor);
        } else if (timeOfTheDay < 200 + 1000) { // day
            float factor = (float) (timeOfTheDay - 200) / 1000;
            skyColor = Utils.interpolateColor(Constants.DUSK_COLOR, Constants.DAY_COLOR, factor);
        } else if (timeOfTheDay < 200 + 1000 + 200) { // dusk
            float factor = (float) (timeOfTheDay - 200 - 1000) / 200;
            skyColor = Utils.interpolateColor(Constants.DAY_COLOR, Constants.DUSK_COLOR, factor);
        } else { // night
            float factor = (float) (timeOfTheDay - 200 - 1000 - 200) / 1000;
            skyColor = Utils.interpolateColor(Constants.DUSK_COLOR, Constants.NIGHT_COLOR, factor);
        }
    }

    public void render(Screen screen) {
        Arrays.fill(screen.pixels, skyColor);

        renderTiles(screen);
        entities.forEach(entity -> entity.render(screen));
    }

    public void renderTiles(Screen screen) {
        for (int row = 0; row < map.length; row++) {
            for (int column = 0; column < map[0].length; column++) {
                // 1:2 width:height
                int x = (row - column) * (TILE_WIDTH / 2);
                int y = (row + column) * (TILE_HEIGHT / 4);

                int tile = map[row][column];
                if (tile == -128)
                    continue;

                screen.draw(Textures.TILES[0][tile], x + camera.getX(), y + camera.getY());
            }
        }
    }

    public void loadMap(String path) {
        BufferedImage image;
        try {
            image = ImageIO.read(Game.class.getResource(path));
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load " + path);
        }

        int width = image.getWidth();
        int height = image.getHeight();

        this.map = new byte[width][height];
        for (int row = 0; row < width; row++) {
            for (int column = 0; column < height; column++) {
                int pixelColor = image.getRGB(row, column) & 0xFFFFFF; // mask the alpha

                switch (pixelColor) {
                    case 0x00FF00:
                        map[row][column] = 0;
                        break;
                    case 0xFFFF00:
                        map[row][column] = 1;
                        break;
                    case 0x888888:
                        map[row][column] = 2;
                        break;
                    default:
                        map[row][column] = -128;
                        break;
                }
            }
        }
    }

    public long getTime() {
        return time;
    }
}
