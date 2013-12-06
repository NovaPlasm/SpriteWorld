package com.socialdonut.spriteworld.entities;

import com.socialdonut.spriteworld.Game;
import com.socialdonut.spriteworld.InputHandler;
import com.socialdonut.spriteworld.gfx.Font;
import com.socialdonut.spriteworld.gfx.Screen;
import com.socialdonut.spriteworld.level.Level;
import com.socialdonut.spriteworld.net.packets.Packet02Move;
import com.socialdonut.spriteworld.net.packets.Packet03Health;
import com.socialdonut.spriteworld.net.packets.Packet04Death;

public class Player extends Mob {

    private InputHandler input;
    private int scale = 1;
    protected boolean isSwimming = false;
    private int tickCount = 0;
    private String username;

    public Player(Level level, int x, int y, InputHandler input, String username, int health) {
        super(level, "Player", x, y, 1, health);
        this.input = input;
        this.username = username;
    }

    public void tick() {
        int xa = 0;
        int ya = 0;
        int health = this.health;
        boolean healthChange = false;
        if (input != null) {
        	if (input.health.isPressed()) {
        		health--;
        		healthChange = true;
        	}
        	if (input.shift.isPressed()) {
        		if (input.up.isPressed()) {
        			ya-=3;
        		}	
        		if (input.down.isPressed()) {
        			ya+=3;
        		}
        		if (input.left.isPressed()) {
        			xa-=3;
        		}
        		if (input.right.isPressed()) {
        			xa+=3;
        		}
        	} else {
        		if (input.up.isPressed()) {
        			ya-=2;
        		}	
        		if (input.down.isPressed()) {
        			ya+=2;
        		}
        		if (input.left.isPressed()) {
        			xa-=2;
        		}
        		if (input.right.isPressed()) {
        			xa+=2;
        		}
        	}
        }
        if (xa != 0 || ya != 0) {
            move(xa, ya);
            isMoving = true;

            Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y, this.numSteps, this.isMoving,
                    this.movingDir);
            packet.writeData(Game.game.socketClient);
        } else {
            isMoving = false;
        }
        if (healthChange) {
        	Packet03Health packet = new Packet03Health(this.getUsername(), health);
        	packet.writeData(Game.game.socketClient);
        }
        if (this.health < 0) {
        	Packet04Death packet = new Packet04Death(this.getUsername());
        	packet.writeData(Game.game.socketClient);
        }
        isSwimming = hasSubmerged(xa, ya);
        tickCount++;
    }

    public void render(Screen screen) {
        int xTile = 0;
        int yTile = 27;
        int walkingSpeed = 4;
        int flipTop = (numSteps >> walkingSpeed) & 1;
        int flipBottom = (numSteps >> walkingSpeed) & 1;

        if (movingDir == 1) {
            xTile += 2;
        } else if (movingDir > 1) {
            xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
            flipTop = (movingDir - 1) % 2;
        }

        int modifier = 16 * scale;
        int xOffset = x - modifier / 2;
        int yOffset = y - modifier / 2 - 4;
        if (isSwimming) {
            yOffset += 4;
            if (tickCount % 60 < 15) {
            } else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
                yOffset -= 1;
            } else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
            } else {
                yOffset -= 1;
            }
            screen.render(xOffset, yOffset + 5, 0 + 26 * 32, 0x00, 1);
            screen.render(xOffset+16, yOffset + 5, 0 + 26 * 32, 0x01, 1);
        }
        screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, flipTop, scale);
        screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, flipTop,
                scale);

        if (!isSwimming) {
            screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32,
                    flipBottom, scale);
            screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1)
                    * 32, flipBottom, scale);
        }
    }
    
    public void renderData(Screen screen) {
    	int modifier = 8 * scale;
        int xOffset = x - modifier / 2;
        int yOffset = y - modifier / 2 - 4;
        String health1 = ""+health;
    	if (username != null) {
    		Font.render(username, screen, xOffset - ((username.length() - 1) / 2 * 16), yOffset - 22, 1);
    		Font.render(health1, screen, xOffset, yOffset - 36, 1);
    	}
    }

    public boolean hasCollided(int xa, int ya) {
        int xMin = -6;
        int xMax = 20;
        int yMin = 10;
        int yMax = 18;
        for (int x = xMin; x < xMax; x++) {
            if (isSolidTile(xa, ya, x, yMin)) {
                return true;
            }
        }
        for (int x = xMin; x < xMax; x++) {
            if (isSolidTile(xa, ya, x, yMax)) {
                return true;
            }
        }
        for (int y = yMin; y < yMax; y++) {
            if (isSolidTile(xa, ya, xMin, y)) {
                return true;
            }
        }
        for (int y = yMin; y < yMax; y++) {
            if (isSolidTile(xa, ya, xMax, y)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasSubmerged(int xa, int ya) {
        int xMin = 2;
        int xMax = 8;
        int yMin = 8;
        int yMax = 10;
        for (int x = xMin; x < xMax; x++) {
            if (isLiquidTile(xa, ya, x, yMin)) {
                return true;
            }
        }
        for (int x = xMin; x < xMax; x++) {
            if (isLiquidTile(xa, ya, x, yMax)) {
                return true;
            }
        }
        for (int y = yMin; y < yMax; y++) {
            if (isLiquidTile(xa, ya, xMin, y)) {
                return true;
            }
        }
        for (int y = yMin; y < yMax; y++) {
            if (isLiquidTile(xa, ya, xMax, y)) {
                return true;
            }
        }
        return false;
    }

    public String getUsername() {
        return this.username;
    }
}