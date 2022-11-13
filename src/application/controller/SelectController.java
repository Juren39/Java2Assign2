package application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SelectController {
    private Socket socket = null;
    private PrintStream printStream;
    private boolean isLink = false;
    private String name;
    private LisenerController lisenerController;
    private SelectPane selectPane = new SelectPane();

    public SelectPane getSelectPane() {
        return selectPane;
    }
    public String getName() {
        return name;
    }

    public SelectController() {
        selectPane.draw();
        selectPane.getButton_1().setOnMouseClicked(e->linkStart());
        selectPane.getButton_2().setOnMouseClicked(e->playStart());
    }

    private void playStart() {
        if(isLink) {
            Stage stage = (Stage) selectPane.getButton_2().getScene().getWindow();
            Scene scene = new Scene(lisenerController.getController().getGamebox(),
                    lisenerController.getController().getWidth(), lisenerController.getController().getHeight());
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.setTitle("Tic Tac Toe");
            stage.setResizable(false);
            stage.show();
        }
    }


    private void linkStart() {
        name = selectPane.getTextField().getText();
        if(socket == null) {
            try {
                socket = new Socket(InetAddress.getLocalHost(), 8888);
                System.out.println(socket);
                printStream =new PrintStream(socket.getOutputStream());
                send("LINK:" + name);
                lisenerController = new LisenerController(socket, name);
                lisenerController.start();
                isLink = true;
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
