package application.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
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
    private boolean isGame = false;
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

    public void setIsGame(boolean x) {
        isGame = x;
    }

    public boolean getIsGame() {
        return isGame;
    }

    public SelectController() {
        selectPane.draw();
        selectPane.getButtonLink().setOnMouseClicked(e->linkStart());
        selectPane.getButtonMatch().setOnMouseClicked(e->matchStart());
        selectPane.getButtonGame().setOnMouseClicked(e->playStart());
        selectPane.getQuitMatch().setOnMouseClicked(e->quitMatch());
        selectPane.getQuitGame().setOnMouseClicked(e->quitGame());
    }

    private void playStart() {
        if (isLink && isMatch) {
            setIsGame(true);
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
            send("WANTMATCH");
        }
    }

    private void quitMatch() {
        if (isLink && isMatch) {
            send("QUITMATCH");
        }
    }

    private void quitGame() {
        if (socket != null) {
            send("QUITGAME");
        } else {
            Stage stage = (Stage) selectPane.getButtonGame().getScene().getWindow();
            stage.close();
            System.exit(0);
        }
    }

    private void linkStart() {
        if(socket == null || !isLink) {
            try {
                name = selectPane.getTextField().getText();
                socket = new Socket(InetAddress.getLocalHost(), 8888);
                printStream =new PrintStream(socket.getOutputStream());
                lisenerController = new LisenerController(socket, name, this);
                lisenerController.start();
                send("LINK:" + name);
            } catch (IOException e) {
                AlertLink();
            }
        }
    }

    @FXML
    protected void AlertLink() {
        DialogPane dialog = new DialogPane();
        dialog.setHeaderText("????????");
        dialog.setContentText("NO LINKKKKKKKKKKK");
        LisenerController.AlertMode(dialog);
    }

    public void send(String msg) {
        printStream.println(msg);
        printStream.flush();
    }
}
