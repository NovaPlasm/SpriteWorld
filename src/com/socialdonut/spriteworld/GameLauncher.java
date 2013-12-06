package com.socialdonut.spriteworld;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameLauncher extends Applet {

    private static Game game = new Game();
    public static final boolean DEBUG = false;

    @Override
    public void init() {
        setLayout(new BorderLayout());
        add(game, BorderLayout.CENTER);
        setMaximumSize(Game.DIMENSIONS);
        setMinimumSize(Game.DIMENSIONS);
        setPreferredSize(Game.DIMENSIONS);
        BufferedImage icon = null;
		try {
			icon = ImageIO.read(GameLauncher.class.getResourceAsStream("/icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        game.frame.setIconImage(icon);
        game.debug = DEBUG;
        game.isApplet = true;
    }

    @Override
    public void start() {
        game.start("You broke it,true");
    }

    @Override
    public void stop() {
        game.stop();
    }

    public static void main(String[] args) {
    	System.out.println(args[0]);
        game.setMinimumSize(Game.DIMENSIONS);
        game.setMaximumSize(Game.DIMENSIONS);
        game.setPreferredSize(Game.DIMENSIONS);

        game.frame = new JFrame(Game.NAME);

        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLayout(new BorderLayout());

        game.frame.add(game, BorderLayout.CENTER);
        game.frame.pack();

        game.frame.setResizable(false);
        game.frame.setLocationRelativeTo(null);
        game.frame.setVisible(true);

        game.windowHandler = new WindowHandler(game);
        game.debug = DEBUG;
        	
        game.start(args[0]);
    }
}
