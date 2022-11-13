package application.controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

public class ServerController {
    private ServerSocket server;
    private int port = 0;
    private List<PlayerController> players;
    private Socket socket;
    private Link link;
    private Separate separate;

    public void init() {
        port = 8888;
        players = new Vector<>();
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        link = new Link(this);
        link.start();
        separate = new Separate();
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
                PlayerController player = new PlayerController(socket);
                synchronized (new Link(serverController)) {
                    players.add(player);
                }
                player.send("LINK:SUCCESS");
                player.start();
            }
        }
    }

    private class Separate extends Thread {
        public void run() {
            while (true){
                PlayerController line = null;
                PlayerController circle = null;
                synchronized (new Separate()){
                    for (PlayerController player : players){
                        if (player.getStatus() == 0){
                            line = player;
                            break;
                        }
                    }
                    for (PlayerController player : players){
                        if (player.getStatus() == 0 && player != line){
                            circle = player;
                            break;
                        }
                    }
                }
                if (line != null && circle != null){
                    line.setStatus(1);
                    circle.setStatus(1);
                    line.send("MATCHWITH:" + circle.getPlayerName() + ":0");
                    circle.send("MATCHWITH:" + line.getPlayerName() + ":1");
                }
            }
        }
    }

    private class PlayerController extends Thread{
        private Socket socket;
        private String playerName;
        private int status = 0;
        private InetAddress ip;
        private PrintStream printstream;
        private DataInputStream dataInputStream;
        public String getPlayerName() {
            return playerName;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int x) {
            status = x;
        }

        public void run() {
            String line = null;
            while(true) {
                try {
                    line = dataInputStream.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert line != null;
                String[] strs = line.split(":");
                if(strs[0].equals("LUOZI")) {
                    String name = strs[1];
                    String x = strs[2];
                    String y = strs[3];
                    for(PlayerController player : players) {
                        if(player.getPlayerName().equals(name)) {
                            player.send("LUOZI:" + x + ":" + y);
                        }
                    }
                } else if (strs[0].equals("WINNER")) {
                    String name = strs[1];
                    String TURN = strs[2];
                    for(PlayerController player : players) {
                        if(player.getPlayerName().equals(name)) {
                            if (TURN.equals("true")) {
                                player.send("RESTART:rival");
                            } else {
                                player.send("RESTART:you");
                            }
                        }
                    }
                }

            }
        }

        public PlayerController(Socket socket) {
            this.socket = socket;
            try {
                printstream = new PrintStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                StringTokenizer st = new StringTokenizer(dataInputStream.readLine(), ":");
                if (st.nextToken().equalsIgnoreCase("LINK")){
                    this.playerName = st.nextToken();
                }
                this.ip = socket.getLocalAddress();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void send(String msg){
            printstream.println(msg);
            printstream.flush();
        }
    }

}
