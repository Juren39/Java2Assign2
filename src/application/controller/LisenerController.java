package application.controller;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;


public class LisenerController extends Thread {
    private final Controller controller = new Controller();
    private SelectController selectController;
    private final Socket socket;
    private static final int BOUND = 90;
    private static final int EMPTY = 0;
    private static boolean TURN = false;
    private final String name;
    private DataInputStream dataInputStream;
    private PrintStream printStream;
    private static final int PLAY_1 = 1;
    private static final int PLAY_2 = 2;
    private boolean flag = true;

    public Controller getController() {
        return controller;
    }

    private void send(String msg) {
        printStream.println(msg);
        printStream.flush();
    }


    public LisenerController(Socket socket, String name, SelectController selectController) {
        controller.draw();
        controller.getButtonQuit().setOnMouseClicked(e -> disconnect());
        this.name = name;
        this.socket = socket;
        this.selectController = selectController;
        if (socket != null){
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                printStream = new PrintStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        String line = null;
        while (flag){
            try {
                line = dataInputStream.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line != null) {
                String[] strs = line.split(":");
                System.out.println(Arrays.toString(strs));
                if (strs[0].equals("MATCHWITH")){
                    String otherName = strs[1];
                    String order = strs[2];
                    controller.setRivalName(otherName);
                    Platform.runLater(() ->{
                        selectController.getSelectPane().getMatchinfo().setText("Match:" + otherName);
                    });
                    selectController.setIsMatch(true);
                    if (order.equals("0")) { //first move
                        controller.getLabelOrder().setText("It's Your Turn");
                        TURN = true;
                        move();
                    }
                    else if (order.equals("1")){
                        controller.getLabelOrder().setText("It's Rival Turn");
                        TURN = false;
                    }
                }
                else if (strs[0].equals("LUOZI")) {
                    int i = Integer.parseInt(strs[1]);
                    int j = Integer.parseInt(strs[2]);
                    if (refreshRival(i, j)) {
                        if (isGameOver(TURN ? PLAY_2 : PLAY_1)) {
                            try {
                                AlertClick(1);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            move();
                        }
                    }
                }
                else if (strs[0].equals("RESTART")) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    String turn = strs[1];
                    if (turn.equals("rival")) {
                        TURN = false;
                        Platform.runLater(() ->{
                            controller.refreshGame();
                            controller.getLabelOrder().setText("It's Rival Turn");
                        });
                    } else {
                        TURN = true;
                        Platform.runLater(() ->{
                            controller.refreshGame();
                            controller.getLabelOrder().setText("It's Your Turn");
                        });
                        move();
                    }
                }
                else if (strs[0].equals("DISCONNECT")) {
                    Platform.runLater(() -> {
                        controller.getLabelOrder().setText("Gamer Quit");
                    });
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Platform.runLater(() -> {
                        selectController = new SelectController();
                        Stage stage = (Stage) controller.getGamebox().getScene().getWindow();
                        Scene scene = new Scene(selectController.getSelectPane().getAllbox(),
                                selectController.getSelectPane().getPrefWidth(), selectController.getSelectPane().getPrefHeight());
                        selectController.setIsMatch(false);
                        stage.setScene(scene);
                        stage.setTitle("连接");
                        stage.show();
                    });
                }
                else if (strs[0].equals("LINKSUCCESS")) {
                    Platform.runLater(() -> {
                        selectController.getSelectPane().getLinkinfo().setText("Link Success!");
                    });
                    selectController.setIsLink(true);
                }
                else if (strs[0].equals("QUITMATCH")) {
                    Platform.runLater(() -> {
                        selectController.getSelectPane().getMatchinfo().setText("No Match");
                    });
                    selectController.setIsMatch(false);
                }
                else if (strs[0].equals("MOVEWRONG")) {
                    move();
                }
            }
        }
    }

    private void close() throws IOException {
        socket.close();
        flag = false;
        dataInputStream.close();
        printStream.close();
    }

    private void move() {
        controller.getGame_panel().setOnMouseClicked(event -> {
            int x = (int) (event.getX() / BOUND);
            int y = (int) (event.getY() / BOUND);
            if (refreshYour(x, y)) {
                send("LUOZI:" + controller.getRivalName() + ":" + x + ":" + y);
                if (isGameOver(TURN ? PLAY_1 : PLAY_2)) {
                    try {
                        AlertClick(0);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                send("MOVEWRONG:" + name);
            }
            controller.getGame_panel().setOnMouseClicked(null);
        });
    }

    private boolean refreshRival(int x, int y) {
        if (controller.getChessBoard(x, y) == EMPTY) {
            if(TURN) {
                controller.setChessBoard(x, y, PLAY_2);
                Platform.runLater(() -> {
                    controller.drawCircle(x, y);
                    controller.getLabelOrder().setText("It's Your Turn");
                });
            } else {
                controller.setChessBoard(x, y, PLAY_1);
                Platform.runLater(() -> {
                    controller.drawLine(x, y);
                    controller.getLabelOrder().setText("It's Your Turn");
                });
            }
            return true;
        }
        return false;
    }
    private boolean refreshYour(int x, int y) {
        if (controller.getChessBoard(x, y) == EMPTY) {
            if(TURN) {
                controller.setChessBoard(x, y, PLAY_1);
                controller.drawLine(x, y);
            } else {
                controller.setChessBoard(x, y, PLAY_2);
                controller.drawCircle(x, y);
            }
            controller.getLabelOrder().setText("It's Rival Turn");
            return true;
        }
        return false;
    }

    private boolean isGameOver (int player) {
        for (int i = 0; i < 3; i++) {
            if (controller.getChessBoard(i, 0) == player
                    && controller.getChessBoard(i, 1) == player
                    && controller.getChessBoard(i, 2) == player){
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (controller.getChessBoard(0, i) == player
                    && controller.getChessBoard(1, i) == player
                    && controller.getChessBoard(2, i) == player){
                return true;
            }
        }
        if (controller.getChessBoard(0, 0) == player
                && controller.getChessBoard(1, 1) == player
                && controller.getChessBoard(2, 2) == player){
            return true;
        }
        if (controller.getChessBoard(2, 0) == player
                && controller.getChessBoard(1, 1) == player
                && controller.getChessBoard(0, 2) == player){
            return true;
        }

        return false;
    }

    protected void AlertClick(int player) throws InterruptedException {
        //0 means you win, 1 means rival win, 2 means tie.
        if ((TURN && player == 0) || (!TURN && player == 1)) {
            send("WINNER:" + name + ":" + TURN);
            Platform.runLater(() -> {
                controller.getLabelWin().setText("Line Win!");

            });
        } else if ((TURN && player == 1) || (!TURN && player == 0)) {
            send("WINNER:" + name + ":" + TURN);
            Platform.runLater(() -> {
                controller.getLabelWin().setText("Circle Win!");
            });

        } else {
            send("WINNER:" + name + ":" + TURN);
            Platform.runLater(() -> {
                controller.getLabelWin().setText("Tie!");
            });
        }
    }

    private void disconnect () {
        send("GAMERQUIT:" + name);
    }
}
