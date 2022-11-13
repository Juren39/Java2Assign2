package application.controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.StringTokenizer;

public class PlayerController extends Thread{
    private final Socket socket;
    private String playerName;
    private int status = 0;
    private final PrintStream printstream;
    private final DataInputStream dataInputStream;
    private final List<PlayerController> players;
    private final List<Composition> compositions;
    private boolean flag = true;

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
        while(flag) {
            try {
                line = dataInputStream.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line != null) {
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
                } else if (strs[0].equals("GAMERQUIT")) {
                    PlayerController player1, player2;
                    for (int j = 0; j < compositions.size(); j++) {
                        if (compositions.get(j).contains(playerName)) {
                            player1 = compositions.get(j).getCircle();
                            player1.setStatus(0);
                            player1.send("DISCONNECT");
                            player2 = compositions.get(j).getLine();
                            player2.setStatus(0);
                            player2.send("DISCONNECT");
                            compositions.remove(compositions.get(j));
                            j--;
                        }
                    }
                } else if (strs[0].equals("WANTMATCH")) {
                    setStatus(1);
                } else if (strs[0].equals("QUITMATCH")) {
                    for (int j = 0; j < compositions.size(); j++) {
                        if (compositions.get(j).contains(playerName)) {
                            PlayerController player1, player2;
                            player1 = compositions.get(j).getCircle();
                            player2 = compositions.get(j).getLine();
                            player1.send("QUITMATCH");
                            player2.send("QUITMATCH");
                            player1.setStatus(0);
                            player2.setStatus(0);
                            compositions.remove(compositions.get(j));
                            j--;
                        }
                    }
                } else if (strs[0].equals("MOVEWRONG")) {
                    send("MOVEWRONG");
                }
            }
        }
    }
    private void close() throws IOException {
        socket.close();
        flag = false;
        printstream.close();
        dataInputStream.close();
    }

    public PlayerController(Socket socket, List<PlayerController> players, List<Composition> compositions) {
        this.socket = socket;
        this.players = players;
        this.compositions = compositions;
        try {
            printstream = new PrintStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            StringTokenizer st = new StringTokenizer(dataInputStream.readLine(), ":");
            if (st.nextToken().equalsIgnoreCase("LINK")){
                this.playerName = st.nextToken();
            }
            InetAddress ip = socket.getLocalAddress();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String msg){
        printstream.println(msg);
        printstream.flush();
    }
}