package application.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class ServerController {
    private ServerSocket server;
    private List<PlayerController> players;
    private List<Composition> compositions;
    private Socket socket;

    public void init() {
        int port = 8888;
        players = new Vector<>();
        compositions = new Vector<>();
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Link link = new Link(this);
        link.start();
        Separate separate = new Separate();
        separate.start();
    }

    private class Link extends Thread {
        private final ServerController serverController;

        public Link (ServerController serverController) {
            this.serverController = serverController;
        }
        public void run() {
            while (true){
                try {
                    socket = server.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PlayerController player = new PlayerController(socket, players, compositions);
                synchronized ("aaaa") {
                    players.add(player);
                }
                player.send("LINKSUCCESS");
                player.start();
            }
        }
    }

    private class Separate extends Thread {
        public void run() {
            while (true){
                PlayerController line = null;
                PlayerController circle = null;
                synchronized ("aaaa"){
                    for (PlayerController player : players){
                        if (player.getStatus() == 1){
                            line = player;
                            break;
                        }
                    }
                    for (PlayerController player : players){
                        if (player.getStatus() == 1 && player != line){
                            circle = player;
                            break;
                        }
                    }
                }
                if (line != null && circle != null){
                    line.setStatus(2);
                    circle.setStatus(2);
                    Composition composition = new Composition(circle, line);
                    compositions.add(composition);
                    line.send("MATCHWITH:" + circle.getPlayerName() + ":0");
                    circle.send("MATCHWITH:" + line.getPlayerName() + ":1");
                }
            }
        }
    }
}
