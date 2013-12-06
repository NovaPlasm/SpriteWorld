package com.socialdonut.spriteworld.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.socialdonut.spriteworld.Game;
import com.socialdonut.spriteworld.entities.PlayerMP;
import com.socialdonut.spriteworld.net.packets.Packet;
import com.socialdonut.spriteworld.net.packets.Packet.PacketTypes;
import com.socialdonut.spriteworld.net.packets.Packet00Login;
import com.socialdonut.spriteworld.net.packets.Packet01Disconnect;
import com.socialdonut.spriteworld.net.packets.Packet02Move;
import com.socialdonut.spriteworld.net.packets.Packet03Health;
import com.socialdonut.spriteworld.net.packets.Packet04Death;

public class GameClient extends Thread {

    private InetAddress ipAddress;
    private DatagramSocket socket;
    private Game game;

    public GameClient(Game game, String ipAddress) {
        this.game = game;
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type) {
        default:
        case INVALID:
            break;
        case LOGIN:
            packet = new Packet00Login(data);
            handleLogin((Packet00Login) packet, address, port);
            break;
        case DISCONNECT:
            packet = new Packet01Disconnect(data);
            System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                    + ((Packet01Disconnect) packet).getUsername() + " has left the world...");
            game.level.removePlayerMP(((Packet01Disconnect) packet).getUsername());
            break;
        case MOVE:
            packet = new Packet02Move(data);
            handleMove((Packet02Move) packet);
            break;
        case HEALTH:
        	packet = new Packet03Health(data);
        	handleHealth((Packet03Health) packet);
        	break;
        case DEATH:
        	packet = new Packet04Death(data);
        	handleDeath((Packet04Death) packet);
        }
    }

    public void sendData(byte[] data) {
        if (!game.isApplet) {
            DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleLogin(Packet00Login packet, InetAddress address, int port) {
        System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername()
                + " has joined the game...");
        PlayerMP player = new PlayerMP(game.level, packet.getX(), packet.getY(), packet.getUsername(), address, port);
        game.level.addEntity(player);
    }

    private void handleMove(Packet02Move packet) {
        this.game.level.movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getNumSteps(),
                packet.isMoving(), packet.getMovingDir());
    }
    
    private void handleHealth(Packet03Health packet) {
    	this.game.level.setPlayerHealth(packet.getUsername(), packet.getHealth());
    }
    
    private void handleDeath(Packet04Death packet) {
    	this.game.level.setPlayerDead(packet.getUsername());
    }
}
