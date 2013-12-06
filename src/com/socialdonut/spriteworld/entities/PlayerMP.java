package com.socialdonut.spriteworld.entities;

import java.net.InetAddress;

import com.socialdonut.spriteworld.InputHandler;
import com.socialdonut.spriteworld.level.Level;

public class PlayerMP extends Player {

    public InetAddress ipAddress;
    public int port;

    public PlayerMP(Level level, int x, int y, InputHandler input, String username, InetAddress ipAddress, int port) {
        super(level, x, y, input, username, 100);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public PlayerMP(Level level, int x, int y, String username, InetAddress ipAddress, int port) {
        super(level, x, y, null, username, 100);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void tick() {
        super.tick();
    }
}
