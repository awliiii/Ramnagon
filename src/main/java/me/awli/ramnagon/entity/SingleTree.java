package me.awli.ramnagon.entity;

import me.awli.ramnagon.Camera;
import me.awli.ramnagon.gfx.Screen;
import me.awli.ramnagon.gfx.Textures;

public class SingleTree extends Entity {
    public SingleTree(int x, int y) {
        super(x, y);
    }

    @Override
    public void render(Screen screen, Camera camera) {
        screen.draw(Textures.SINGLE_TREE, x + camera.getX(), y + camera.getY());
    }

    @Override
    public void tick() { }
}
