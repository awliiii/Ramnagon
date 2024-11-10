package me.awli.ramnagon.entity;

import me.awli.ramnagon.gfx.Screen;

/*
 * TODO:
 *  - Player spritesheet
 *  - Get direction from the movement
 *  - Animations
 */
public class Player extends Entity {
    private int dir = 0;
    private int animationFrame = 0;

    public Player(int x, int y) {
        super(x, y);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Screen screen) {

    }
}
