package com.socialdonut.spriteworld.net.packets;

import com.socialdonut.spriteworld.net.GameClient;
import com.socialdonut.spriteworld.net.GameServer;

public class Packet03Health extends Packet {

    private String username;
    private int health;

    public Packet03Health(byte[] data) {
        super(03);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.health = Integer.parseInt(dataArray[1]);
    }

    public Packet03Health(String username, int health) {
        super(03);
        this.username = username;
        this.health = health;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("03" + this.username + "," + getHealth()).getBytes();
    }

    public String getUsername() {
        return username;
    }

    public int getHealth() {
        return health;
    }

}
