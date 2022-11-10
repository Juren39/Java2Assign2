package application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SelectController {
    @FXML
    private Button play;

    @FXML
    private void playStart(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Stage stage = (Stage) play.getScene().getWindow();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("mainUI.fxml"));
        Pane root = fxmlLoader.load();
        stage.setTitle("Tic Tac Toe");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}
