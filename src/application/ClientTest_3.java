package application;

import application.controller.SelectController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

public class ClientTest_3 extends Application{
    @Override
    public void start(Stage stage){
        try {
            SelectController selectController = new SelectController();
            Scene scene = new Scene(selectController.getSelectPane().getAllbox(),
                    selectController.getSelectPane().getPrefWidth(), selectController.getSelectPane().getPrefHeight());
            stage.setScene(scene);
            stage.setTitle("LINK");
            stage.initStyle(StageStyle.UNIFIED);
            stage.setOnCloseRequest(event -> {
                Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
                alert2.setTitle("Exit");
                alert2.setHeaderText("Are you sure to exit");
                Optional<ButtonType> result = alert2.showAndWait();
                if (result.get() == ButtonType.OK){
                    stage.close();
                    System.exit(0);
                } else{
                    event.consume();
                }
            });
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
