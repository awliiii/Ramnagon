package me.awli.ramnagon;

import me.awli.ramnagon.gfx.Screen;
import me.awli.ramnagon.gfx.Textures;
import me.awli.ramnagon.level.Level;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

/*
 * TODO:
 *  - Player movement using mouse click
 *  - GUI, part 2
 */
public final class Game extends Canvas implements Runnable {
    public static final int WIDTH = 320;
    public static final int HEIGHT = (WIDTH * 3) / 4;

    public static final String TITLE = "RAMNAGON alpha";

    public final InputHandler inputHandler;

    public JFrame frame;

    private Thread thread;
    private volatile boolean isRunning = false;

    private final Screen screen;

    private final Level level;

    private Game() {
        // GAME HANDLERS
        this.inputHandler = new InputHandler();
        this.addKeyListener(inputHandler);
        this.addMouseListener(inputHandler);
        this.addMouseMotionListener(inputHandler);

        this.screen = new Screen(WIDTH, HEIGHT);

        // CANVAS STUFF
        Dimension windowDimensions = new Dimension(WIDTH * Constants.SCALE, HEIGHT * Constants.SCALE);
        this.setMinimumSize(windowDimensions);
        this.setPreferredSize(windowDimensions);
        this.setMaximumSize(windowDimensions);

        // FRAME STUFF
        this.frame = new JFrame(TITLE);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        // CURSOR
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        BufferedImage cursorImage;
        try {
            cursorImage = ImageIO.read(Game.class.getResource("/textures/ui/cursor.png"));
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load the cursor");
        }
        Cursor cursor = toolkit.createCustomCursor(cursorImage, new Point(this.getX(), this.getY()), "cursor");
        frame.setCursor(cursor);

        this.level = new Level();
    }

    public synchronized void start() {
        if (isRunning)
            return;
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!isRunning)
            return;
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void tick() {
        // i have seen this in scratch games lol
        int sideways = (inputHandler.keys[KeyEvent.VK_A] ? 1 : 0) - (inputHandler.keys[KeyEvent.VK_D] ? 1 : 0);
        int forward = (inputHandler.keys[KeyEvent.VK_W] ? 1 : 0) - (inputHandler.keys[KeyEvent.VK_S] ? 1 : 0);
        // i should take the magnitude and then divide the delta through that magnitude
        // but it wouldnt work because everything is in int and not double/float
        level.camera.move(sideways * Constants.CAMERA_SPEED, forward * Constants.CAMERA_SPEED);

        if (inputHandler.mouseKeys[MouseEvent.BUTTON1])
            System.out.println("(" + inputHandler.mouseX + ", " + inputHandler.mouseY + ")");

        level.tick();
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        level.render(screen);

        screen.draw(Textures.INVENTORY, 0, HEIGHT - Textures.INVENTORY.HEIGHT);

        int currentHealth = (int) (level.getTime() % 100); // TEMP
        int maxHealth = 100; // TEMP
        int healthPercentage = (int) ((double) currentHealth / maxHealth * 100); // TEMP
        int healthBarWidth = (int) ((healthPercentage / 100.0) * (78 - 8)); // TEMP
        screen.fill(8, HEIGHT - 14, 8 + healthBarWidth, HEIGHT - 8, 0x8E2A33);

        g.drawImage(screen.image, 0, 0, WIDTH * Constants.SCALE, HEIGHT * Constants.SCALE, null);
        g.dispose();
        bs.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double unprocessed = 0;
        double nsPerTick = 1000000000.0 / 60;
        int frames = 0; // per sec
        int ticks = 0; // per sec
        long lastTimer = System.currentTimeMillis();

        while (isRunning) {
            long now = System.nanoTime();
            unprocessed += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = false;
            while (unprocessed >= 1) {
                ticks++;
                tick();
                unprocessed -= 1;
                shouldRender = true;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (shouldRender) {
                frames++;
                render();
            }

            if (System.currentTimeMillis() - lastTimer > 1000) {
                lastTimer += 1000;
                System.out.println(ticks + " tps, " + frames + " fps");
                frames = 0;
                ticks = 0;
            }
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}