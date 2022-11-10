package application.controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class PlayerController extends Thread{
    private static Socket socket;
    private static String playerName;

    private static PrintStream printstream;
    private static DataInputStream dataInputStream;

    public String getPlayerNameName() {
        return playerName;
    }

    public void run() {

    }

    public PlayerController(Socket socket) {
        PlayerController.socket = socket;
        try {
            printstream = new PrintStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            StringBuffer stringBuffer = new StringBuffer(dataInputStream.readLine());
            System.out.println(stringBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String msg){
        printstream.println(msg);
        printstream.flush();
    }
}
