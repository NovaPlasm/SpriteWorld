package com.socialdonut.spriteworld.level.tiles;

import com.socialdonut.spriteworld.gfx.Screen;
import com.socialdonut.spriteworld.level.Level;

public abstract class Tile {

    public static final Tile[] tiles = new Tile[256];
    public static final Tile VOID = new BasicSolidTile(0, 0, 0, 0xFF000000, true);
    public static final Tile STONE = new BasicSolidTile(1, 1, 0, 0xFF555555, true);
    public static final Tile GRASS = new BasicTile(2, 2, 0, 0xFF00FF00, true, false);
    public static final Tile GRASS2 = new BasicTile(3, 3, 0, 0xFF00FF01, true, false);
    public static final Tile GRASS3 = new BasicTile(4, 3, 1, 0xFF00FF01, true, false);
    public static final Tile GRASS4 = new BasicTile(5, 3, 2, 0xFF00FF01, true, false);
    public static final Tile GRASS5 = new BasicTile(6, 3, 3, 0xFF00FF01, true, false);
    public static final Tile WATER = new AnimatedTile(7, new int[][] { { 0, 5 }, { 1, 5 }, { 2, 5 }, { 1, 5 } }, 0xFF0000FF, 1000, true, true);
    public static final Tile FENCE = new BasicSolidTile(9, 4, 0, 0xFFAAAA1E, true);
    public static final Tile FENCECONNECTED = new BasicSolidTile(8, 5, 0, 0xFFAAAA1E, true);
    public static final Tile STONEFLOOR = new BasicTile(10, 6, 0, 0xFFADADAD, true, false);
    public static final Tile PATH = new BasicTile(11, 6, 0, 0xFF97651E, true, false);
    public static final Tile TRUNKBASE = new BasicSolidTile(12, 7, 0, 0xFF603E0D, true);
    public static final Tile CLIFF_BASE = new BasicSolidTile(13, 0, 3, 0xFF725006, true);
    public static final Tile CLIFF_MID = new BasicSolidTile(14, 0, 2, 0xFFAC790B, true);
    public static final Tile CLIFF_TOP_GRASS = new BasicSolidTile(15, 0, 1, 0xFFCA8E0C, true);
    public static final Tile TRUNK = new BasicTile(16, 7, 0, 0xFF825312, false, false);
    public static final Tile LEAVES = new BasicTile(17, 1, 0, 0xFF145A14, false, false);
    public static final Tile AIR = new BasicTile(18, 0, 0, 0xFFFFFFFF, false, false);
    
    private byte id;
    protected boolean solid;
    protected boolean liquid;
    protected boolean emitter;
    private int levelColour;

    public Tile(int id, boolean isSolid, boolean isLiquid, boolean isEmitter, int levelColour, boolean isBackground) {
        this.id = (byte) id;
        if (tiles[id] != null)
            throw new RuntimeException("Duplicate tile id on " + id);
        this.solid = isSolid;
        this.liquid = isLiquid;
        this.emitter = isEmitter;
        this.levelColour = levelColour;
        tiles[id] = this;
    }

    public byte getId() {
        return id;
    }

    public boolean isSolid() {
        return solid;
    }
    
    public boolean isLiquid() {
		return liquid;
	}

    public boolean isEmitter() {
        return emitter;
    }

    public int getLevelColour() {
        return levelColour;
    }

    public abstract void tick();

    public abstract void render(Screen screen, Level level, int x, int y);
}
