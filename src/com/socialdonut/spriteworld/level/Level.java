package com.socialdonut.spriteworld.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.socialdonut.spriteworld.entities.Entity;
import com.socialdonut.spriteworld.entities.PlayerMP;
import com.socialdonut.spriteworld.gfx.Screen;
import com.socialdonut.spriteworld.level.tiles.Tile;

public class Level {

    private byte[] tilesBack;
    private byte[] tilesFore;
    public int width;
    public int height;
    private List<Entity> entities = new ArrayList<Entity>();
    private String imageBackPath;
    private String imageForePath;
    private BufferedImage imageBack;
    private BufferedImage imageFore;

    public Level(String imageBackPath, String imageForePath) {
        if (imageBackPath != null && imageForePath != null) {
            this.imageBackPath = imageBackPath;
            this.imageForePath = imageForePath;
            this.loadLevelFromFile();
        } else {
            this.width = 64;
            this.height = 64;
            tilesBack = new byte[width * height];
            tilesFore = new byte[width * height];
            this.generateLevel();
        }
    }

    private void loadLevelFromFile() {
        try {
            this.imageBack = ImageIO.read(Level.class.getResource(this.imageBackPath));
            this.imageFore = ImageIO.read(Level.class.getResource(this.imageForePath));
            this.width = this.imageBack.getWidth();
            this.height = this.imageBack.getHeight();
            tilesBack = new byte[width * height];
            tilesFore = new byte[width * height];
            this.loadTiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTiles() {
        int[] tileColoursBack = this.imageBack.getRGB(0, 0, width, height, null, 0, width);
        int[] tileColoursFore = this.imageFore.getRGB(0, 0, width, height, null, 0, width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tileCheck: for (Tile t : Tile.tiles) {
                    if (t != null && t.getLevelColour() == tileColoursBack[x + y * width]) {
                    	if ((!(y==0 && x==0) && (this.tilesBack[x-1+y*width] == Tile.FENCE.getId()||this.tilesBack[x-1+y*width] == Tile.FENCECONNECTED.getId())) && (t.getId() == Tile.FENCE.getId())) {
                    		this.tilesBack[x + y * width] = Tile.FENCECONNECTED.getId();
                    	} else {
                    		this.tilesBack[x + y * width] = t.getId();
                    	}
                    	if (this.tilesBack[x+y*width] == Tile.GRASS.getId()) {
                    		int rand = (int) (Math.random() * 1000);
                    		if (rand > 230) {
                    			this.tilesBack[x+y*width] = t.getId();
                    		} else if(rand <= 230 && rand >= 130) {
                    			this.tilesBack[x+y*width] = Tile.GRASS2.getId();
                    		} else if(rand < 130 && rand >= 60) {
                    			this.tilesBack[x+y*width] = Tile.GRASS4.getId();
                    		} else if(rand < 60 && rand >= 4) {
                    			this.tilesBack[x+y*width] = Tile.GRASS5.getId();
                    		} else {
                    			this.tilesBack[x+y*width] = Tile.GRASS3.getId();
                    		}
                    	}
                    }
                    if (t != null && t.getLevelColour() == tileColoursFore[x + y * width]) {
                    	this.tilesFore[x + y * width] = t.getId();
                        break tileCheck;
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void saveLevelToFile() {
        try {
            ImageIO.write(imageBack, "png", new File(Level.class.getResource(this.imageBackPath).getFile()));
            ImageIO.write(imageFore, "png", new File(Level.class.getResource(this.imageForePath).getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void alterTile(int x, int y, Tile newTile) {
        this.tilesBack[x + y * width] = newTile.getId();
        imageBack.setRGB(x, y, newTile.getLevelColour());
        this.tilesFore[x + y * width] = newTile.getId();
        imageFore.setRGB(x, y, newTile.getLevelColour());
    }

    public void generateLevel() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x * y % 10 < 7) {
                    tilesBack[x + y * width] = Tile.GRASS.getId();
                } else {
                    tilesBack[x + y * width] = Tile.STONE.getId();
                }
            }
        }
    }

    public synchronized List<Entity> getEntities() {
        return this.entities;
    }

    public void tick() {
        for (Entity e : getEntities()) {
            e.tick();
        }

        for (Tile t : Tile.tiles) {
            if (t == null) {
                break;
            }
            t.tick();
        }
    }

    public void renderTiles(Screen screen, int xOffset, int yOffset, boolean isForeground) {
        if (xOffset < 0)
            xOffset = 0;
        if (xOffset > ((width << 4) - screen.width))
            xOffset = ((width << 4) - screen.width);
        if (yOffset < 0)
            yOffset = 0;
        if (yOffset > ((height << 4) - screen.height))
            yOffset = ((height << 4) - screen.height);

        screen.setOffset(xOffset, yOffset);
        for (int y = (yOffset >> 4); y < (yOffset + screen.height >> 4) + 1; y++) {
            for (int x = (xOffset >> 4); x < (xOffset + screen.width >> 4) + 1; x++) {
            	if (isForeground)
            		getForeTile(x, y).render(screen, this, x << 4, y << 4);
            	else
            		getBackTile(x, y).render(screen, this, x << 4, y << 4);
            }
        }
    }

    public void renderEntities(Screen screen) {
        for (Entity e : getEntities()) {
            e.render(screen);
        }
    }
    
    public void renderEntitiesData(Screen screen) {
        for (Entity e : getEntities()) {
            e.renderData(screen);
        }
    }

    public Tile getBackTile(int x, int y) {
        if (0 > x || x >= width || 0 > y || y >= height) {
            return Tile.VOID;
        }
        return Tile.tiles[tilesBack[x + y * width]];
    }
    
    public Tile getForeTile(int x, int y) {
        if (0 > x || x >= width || 0 > y || y >= height) {
            return Tile.VOID;
        }
        return Tile.tiles[tilesFore[x + y * width]];
    }

    public synchronized void addEntity(Entity entity) {
        this.getEntities().add(entity);
    }

    public synchronized void removePlayerMP(String username) {
        int index = 0;
        for (Entity e : getEntities()) {
            if (e instanceof PlayerMP && ((PlayerMP) e).getUsername().equals(username)) {
                break;
            }
            index++;
        }
        this.getEntities().remove(index);
    }

    private int getPlayerMPIndex(String username) {
        int index = 0;
        for (Entity e : getEntities()) {
            if (e instanceof PlayerMP && ((PlayerMP) e).getUsername().equals(username)) {
                break;
            }
            index++;
        }
        return index;
    }

    public synchronized void movePlayer(String username, int x, int y, int numSteps, boolean isMoving, int movingDir) {
        int index = getPlayerMPIndex(username);
        PlayerMP player = (PlayerMP) this.getEntities().get(index);
        player.x = x;
        player.y = y;
        player.setMoving(isMoving);
        player.setNumSteps(numSteps);
        player.setMovingDir(movingDir);
    }
    
    public synchronized void setPlayerHealth(String username, int health) {
    	int index = getPlayerMPIndex(username);
    	PlayerMP player = (PlayerMP) this.getEntities().get(index);
    	player.setHealth(health);
    }
    
    public synchronized void setPlayerDead(String username) {
    	int index = getPlayerMPIndex(username);
    	PlayerMP player = (PlayerMP) this.getEntities().get(index);
    	player.setHealth(100);
    	player.x = 100;
    	player.y = 100;
    }
}
