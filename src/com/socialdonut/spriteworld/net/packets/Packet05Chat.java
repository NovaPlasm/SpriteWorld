package com.socialdonut.spriteworld.net.packets;

import com.socialdonut.spriteworld.net.GameClient;
import com.socialdonut.spriteworld.net.GameServer;

public class Packet05Chat extends Packet {

    private String username;
    private String message;

    public Packet05Chat(byte[] data) {
        super(05);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.message = dataArray[1];
    }

    public Packet05Chat(String username, String message) {
        super(03);
        this.username = username;
        this.message = message;
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
        return ("03" + this.username + "," + this.message).getBytes();
    }

    public String getUsername() {
        return username;
    }
    
    public String getMessage() {
    	return message;
    }

}
