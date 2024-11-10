package me.awli.ramnagon.entity;

import me.awli.ramnagon.Camera;
import me.awli.ramnagon.gfx.Screen;

public abstract class Entity {
    protected int x;

    protected int y;

    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void tick();
    public abstract void render(Screen screen, Camera camera);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
