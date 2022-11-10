package application.controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class ServerController {
    private static ServerSocket server;
    private static int port = 0;
    private static List<PlayerController> players;
    private static Socket socket;
    private Link link;

    public static List<PlayerController> getPlayers() {
        return players;
    }

    public void init() {
        port = 8888;
        players = new Vector<>();   //构造线程安全的Vector
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        link = new Link();
        link.start();
    }

    private class Link extends Thread {
        public void run() {
            while (true){
                if (players.size() < 2){   //小于10开始连接
                    try {
                        socket = server.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    PlayerController player = new PlayerController(socket);
                    synchronized (new Link()) {
                        players.add(player);
                    }
                    player.send("LINK:" + player.getName());
                    player.start();
                }
                else {
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
