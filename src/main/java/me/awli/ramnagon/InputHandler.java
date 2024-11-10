package me.awli.ramnagon;

import javax.swing.event.MouseInputListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

public class InputHandler implements KeyListener, MouseInputListener {
    public boolean[] keys = new boolean[1024];
    public boolean[] mouseKeys = new boolean[8];
    public double mouseX, mouseY;

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseKeys[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseKeys[e.getButton()] = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX() / Game.SCALE;
        mouseY = e.getY() / Game.SCALE;
    }

    // UNUSED
    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) { }

    @Override
    public void mouseClicked(MouseEvent e) { }
}