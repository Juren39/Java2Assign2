package application.controller;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class SelectController {
    private Socket socket = null;
    private PrintStream printStream;
    private boolean isLink = false;
    private boolean isMatch = false;
    private String name;
    private LisenerController lisenerController;
    private final SelectPane selectPane = new SelectPane();

    public SelectPane getSelectPane() {
        return selectPane;
    }
    public String getName() {
        return name;
    }

    public void setIsLink(boolean x) {
        isLink = x;
    }

    public void setIsMatch(boolean x) {
        isMatch = x;
    }

    public SelectController() {
        selectPane.draw();
        selectPane.getButtonLink().setOnMouseClicked(e->linkStart());
        selectPane.getButtonMatch().setOnMouseClicked(e->matchStart());
        selectPane.getButtonGame().setOnMouseClicked(e->playStart());
        selectPane.getQuitMatch().setOnMouseClicked(e->quitMatch());
    }

    private void playStart() {
        if (isLink && isMatch) {
            Stage stage = (Stage) selectPane.getButtonGame().getScene().getWindow();
            Scene scene = new Scene(lisenerController.getController().getGamebox(),
                    lisenerController.getController().getWidth(), lisenerController.getController().getHeight());
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.setTitle("Tic Tac Toe");
            stage.setResizable(false);
            stage.show();
        }
    }

    private void matchStart() {
        if (isLink) {
            send("WANTMATCH:" + name);
        }
    }

    private void quitMatch() {
        if (isLink && isMatch) {
            send("QUITMATCH:" + name);
        }
    }

    private void linkStart() {
        if(socket == null) {
            try {
                name = selectPane.getTextField().getText();
                socket = new Socket(InetAddress.getLocalHost(), 8888);
                System.out.println(socket);
                printStream =new PrintStream(socket.getOutputStream());
                send("LINK:" + name);
                lisenerController = new LisenerController(socket, name, this);
                lisenerController.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void send(String msg) {
        printStream.println(msg);
        printStream.flush();
    }
}
