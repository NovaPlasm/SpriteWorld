package com.socialdonut.spriteworld;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.socialdonut.spriteworld.entities.Player;
import com.socialdonut.spriteworld.entities.PlayerMP;
import com.socialdonut.spriteworld.gfx.Screen;
import com.socialdonut.spriteworld.gfx.SpriteSheet;
import com.socialdonut.spriteworld.level.Level;
import com.socialdonut.spriteworld.net.GameClient;
import com.socialdonut.spriteworld.net.GameServer;
import com.socialdonut.spriteworld.net.packets.Packet00Login;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 400;
    public static final int HEIGHT = WIDTH/12*9;
    public static final int SCALE = 3;
    public static final String NAME = "SpriteWorld";
    public static final Dimension DIMENSIONS = new Dimension(758, 566);
    public static Game game;
    private static String usern;

    public JFrame frame;

    private Thread thread;

    public boolean running = false;
    public int tickCount = 0;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    private Screen screen;
    public InputHandler input;
    public WindowHandler windowHandler;
    public Level level;
    public Player player;

    public GameClient socketClient;
    public GameServer socketServer;

    public boolean debug = true;
    public boolean isApplet = false;
    
    public void init() {
    	System.out.println("Is something happening");
        game = this;
        screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
        input = new InputHandler(this);
        level = new Level("/levels/map1_back.png", "/levels/map1_fore.png");
        player = new PlayerMP(level, 100, 100, input, usern,null, -1);
        level.addEntity(player);
        if (!isApplet) {
            Packet00Login loginPacket = new Packet00Login(player.getUsername(), player.x, player.y);
            if (socketServer != null) {
                socketServer.addConnection((PlayerMP) player, loginPacket);
            }
            loginPacket.writeData(socketClient);
        }
    }

    public synchronized void start(String data) {
    	System.out.println(data);
        running = true;
        String[] dataA = data.split(",");
        usern = dataA[0];
        thread = new Thread(this, NAME + "_main");
        thread.start();
        if (!isApplet) {
            if (dataA[1].equals("true")) {
                socketServer = new GameServer(this);
                socketServer.start();
                socketClient = new GameClient(this, "localhost");
                socketClient.start();
            } else {
            	socketClient = new GameClient(this, dataA[1]);
            	socketClient.start();
            }
        }
    }

    public synchronized void stop() {
        running = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 60D;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        init();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;

            while (delta >= 1) {
                ticks++;
                tick();
                delta -= 1;
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

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                debug(DebugLevel.INFO, ticks + " ticks, " + frames + " frames");
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void tick() {
        tickCount++;
        level.tick();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        int xOffset = player.x - (screen.width / 2);
        int yOffset = player.y - (screen.height / 2);
        
        level.renderTiles(screen, xOffset, yOffset, false);
        level.renderEntities(screen);
        level.renderTiles(screen, xOffset, yOffset, true);
        level.renderEntitiesData(screen);
        for (int y = 0; y < screen.height; y++) {
            for (int x = 0; x < screen.width; x++) {
                int colourCode = screen.pixels[x + y * screen.width];
                if (colourCode < 255)
                    pixels[x + y * WIDTH] = colourCode;
            }
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

    public static long fact(int n) {
        if (n <= 1) {
            return 1;
        } else {
            return n * fact(n - 1);
        }
    }

    public void debug(DebugLevel level, String msg) {
        switch (level) {
        default:
        case INFO:
            if (debug) {
                System.out.println("[" + NAME + "] " + msg);
            }
            break;
        case WARNING:
            System.out.println("[" + NAME + "] [WARNING] " + msg);
            break;
        case SEVERE:
            System.out.println("[" + NAME + "] [SEVERE]" + msg);
            this.stop();
            break;
        }
    }

    public static enum DebugLevel {
        INFO, WARNING, SEVERE;
    }
}
