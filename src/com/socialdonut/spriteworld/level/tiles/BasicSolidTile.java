package com.socialdonut.spriteworld.level.tiles;

public class BasicSolidTile extends BasicTile {

    public BasicSolidTile(int id, int x, int y, int levelColour, boolean isBackground) {
        super(id, x, y, levelColour, isBackground, false);
        this.solid = true;
    }

}
