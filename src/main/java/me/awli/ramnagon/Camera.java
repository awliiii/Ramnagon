package me.awli.ramnagon;

import me.awli.ramnagon.entity.Entity;

public class Camera {
    private int x;
    private int y;

    // twój konstruktor
    public Camera(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    // a tutaj są twoje gettery i settery
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

