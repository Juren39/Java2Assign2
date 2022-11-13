package application.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
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
        selectPane.getQuitGame().setOnMouseClicked(e->quitGame());
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
            send("WANTMATCH");
        }
    }

    private void quitMatch() {
        if (isLink && isMatch) {
            send("QUITMATCH");
        }
    }

    private void quitGame() {
        send("QUITGAME");
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
                AlertLink();
            }
        }
    }

    @FXML
    protected void AlertLink() {
        DialogPane dialog = new DialogPane();
        dialog.setHeaderText("????????");
        dialog.setContentText("NO LINKKKKKKKKKKK");
        dialog.getButtonTypes().add(ButtonType.YES);

        Stage dialogStage = new Stage();
        Scene dialogScene = new Scene(dialog);
        dialogStage.setScene(dialogScene);
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.setResizable(false);

        Button yes = (Button)dialog.lookupButton(ButtonType.YES);
        yes.setOnAction(event -> {
            dialogStage.close();
        });

        dialogStage.show();
    }

    public void send(String msg) {
        printStream.println(msg);
        printStream.flush();
    }
}
