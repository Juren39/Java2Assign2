package application.controller;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class SelectPane {
    private AnchorPane pane = new AnchorPane();
    private VBox allbox = new VBox();
    private HBox infobox = new HBox();
    private TextField textField = new TextField();
    private Button button_1 = new Button();
    private Button button_2 = new Button();
    private double prefHeight = 400.0;
    private double prefWidth = 600.0;

    public VBox getAllbox() {
        return allbox;
    }

    public double getPrefHeight() {
        return  prefHeight;
    }

    public double getPrefWidth() {
        return  prefWidth;
    }

    public Button getButton_1() {
        return button_1;
    }

    public Button getButton_2() {
        return button_2;
    }

    public TextField getTextField() {
        return textField;
    }

    public void draw() {
        pane.setPrefHeight(prefHeight);
        pane.setPrefWidth(prefWidth);

        button_1.setText("开始链接");
        button_2.setText("开始游戏");

        infobox.getChildren().addAll(button_1, textField);
        allbox.getChildren().addAll(infobox, button_2);
    }

}
