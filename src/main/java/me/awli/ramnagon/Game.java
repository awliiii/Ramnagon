package me.awli.ramnagon;

import me.awli.ramnagon.gfx.Screen;
import me.awli.ramnagon.level.Level;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

/*
 * TODO:
 *  - Player movement using mouse click
 *  - GUI
 */
public final class Game extends Canvas implements Runnable {
    public static final int SCALE = 2;
    public static final int WIDTH = 640;
    public static final int HEIGHT = (WIDTH * 3) / 4;

    public static final String TITLE = "RAMNAGON alpha";

    public final InputHandler inputHandler;

    public JFrame frame;

    private Thread thread;
    private volatile boolean isRunning = false;

    private final Screen screen;

    private Level level;

    private Game() {
        // GAME HANDLERS
        this.inputHandler = new InputHandler();
        this.addKeyListener(inputHandler);
        this.addMouseListener(inputHandler);
        this.addMouseMotionListener(inputHandler);

        this.screen = new Screen(WIDTH, HEIGHT);

        // CANVAS STUFF
        Dimension windowDimensions = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
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
        // i have seen this in scratch games
        int sideways = (inputHandler.keys[KeyEvent.VK_A] ? 1 : 0) - (inputHandler.keys[KeyEvent.VK_D] ? 1 : 0);
        int forward = (inputHandler.keys[KeyEvent.VK_W] ? 1 : 0) - (inputHandler.keys[KeyEvent.VK_S] ? 1 : 0);
        // i should take the magnitude and then divide the delta through that magnitude
        // but it wouldnt work because everything is in int and not double/float
        level.camera.move(sideways * Constants.CAMERA_SPEED, forward * Constants.CAMERA_SPEED);

        if (inputHandler.keys[KeyEvent.VK_1])
            level.loadMap("/maps/plains.png");
        else if (inputHandler.keys[KeyEvent.VK_2])
            level.loadMap("/maps/long.png");
        else if (inputHandler.keys[KeyEvent.VK_3])
            level.loadMap("/maps/square.png");

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

        g.drawImage(screen.image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
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