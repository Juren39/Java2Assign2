package application;

import application.controller.SelectController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientTest_3 extends Application {
    @Override
    public void start(Stage stage) {
        try {
            SelectController selectController = new SelectController();
            Scene scene = new Scene(selectController.getSelectPane().getAllbox(),
                    selectController.getSelectPane().getPrefWidth(), selectController.getSelectPane().getPrefHeight());
            stage.setScene(scene);
            stage.setTitle("连接");
            stage.initStyle(StageStyle.UNIFIED);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}