package com.socialdonut.spriteworld.net.packets;

import com.socialdonut.spriteworld.net.GameClient;
import com.socialdonut.spriteworld.net.GameServer;

public class Packet04Death extends Packet {

    private String username;

    public Packet04Death(byte[] data) {
        super(04);
        String dataArray = readData(data);
        this.username = dataArray;
    }

    public Packet04Death(String username) {
        super(04);
        this.username = username;
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
        return ("04" + this.username).getBytes();
    }

    public String getUsername() {
        return username;
    }
}
