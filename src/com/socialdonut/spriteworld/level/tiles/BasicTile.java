package com.socialdonut.spriteworld.level.tiles;

import com.socialdonut.spriteworld.gfx.Screen;
import com.socialdonut.spriteworld.level.Level;

public class BasicTile extends Tile {

    protected int tileId;
    protected int tileColour;

    public BasicTile(int id, int x, int y, int levelColour, boolean isBackground, boolean isLiquid) {
        super(id, false, isLiquid, false, levelColour, isBackground);
        this.tileId = x + y * 32;
    }

    public void tick() {
    }

    public void render(Screen screen, Level level, int x, int y) {
        screen.render(x, y, tileId, 0x00, 1);
    }

}
