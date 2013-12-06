package com.socialdonut.spriteworld.entities;

import com.socialdonut.spriteworld.level.Level;
import com.socialdonut.spriteworld.level.tiles.Tile;

public abstract class Mob extends Entity {

    protected String name;
    protected int speed;
    protected int numSteps = 0;
    protected boolean isMoving;
    protected int movingDir = 1;
    protected int scale = 1;
    protected int health;

    public Mob(Level level, String name, int x, int y, int speed, int health) {
        super(level);
        this.name = name;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.health = health;
    }

    public void move(int xa, int ya) {
        if (xa != 0 && ya != 0) {
            move(xa, 0);
            move(0, ya);
            numSteps--;
            return;
        }
        numSteps++;
        if (!hasCollided(xa, ya)) {
            if (ya < 0)
                movingDir = 0;
            if (ya > 0)
                movingDir = 1;
            if (xa < 0)
                movingDir = 2;
            if (xa > 0)
                movingDir = 3;
            x += xa * speed;
            y += ya * speed;
        }
    }

    public abstract boolean hasCollided(int xa, int ya);
    public abstract boolean hasSubmerged(int xa, int ya);

    protected boolean isSolidTile(int xa, int ya, int x, int y) {
        if (level == null) {
            return false;
        }
        Tile lastTile = level.getBackTile((this.x + x) >> 4, (this.y + y) >> 4);
        Tile newTile = level.getBackTile((this.x + x + xa) >> 4, (this.y + y + ya) >> 4);
        if (!lastTile.equals(newTile) && newTile.isSolid()) {
            return true;
        }
        return false;
    }
    
    protected boolean isLiquidTile(int xa, int ya, int x, int y) {
        if (level == null) {
            return false;
        }
        //Tile lastTile = level.getBackTile((this.x + x) >> 4, (this.y + y) >> 4);
        Tile newTile = level.getBackTile((this.x + x + xa) >> 4, (this.y + y + ya) >> 4);
        if (newTile.isLiquid()) {
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public int getNumSteps() {
        return numSteps;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public int getMovingDir() {
        return movingDir;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
    }

    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public void setMovingDir(int movingDir) {
        this.movingDir = movingDir;
    }
    
    public void setHealth(int health) {
    	this.health = health;
    }

}
