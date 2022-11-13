package application.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class SelectPane {
    private final AnchorPane pane = new AnchorPane();
    private final VBox allbox = new VBox();
    private final HBox infobox = new HBox();
    private final HBox matchbox = new HBox();
    private final TextField textField = new TextField();
    private final Label linkinfo = new Label();
    private final Label matchinfo = new Label();
    private final Button buttonLink = new Button();
    private final Button buttonGame = new Button();
    private final Button buttonMatch = new Button();
    private final Button quitMatch = new Button();
    private final double prefHeight = 400.0;
    private final double prefWidth = 600.0;

    public VBox getAllbox() {
        return allbox;
    }

    public double getPrefHeight() {
        return  prefHeight;
    }

    public double getPrefWidth() {
        return  prefWidth;
    }

    public Button getButtonLink() {
        return buttonLink;
    }

    public Button getButtonGame() {
        return buttonGame;
    }

    public Button getButtonMatch() {
        return buttonMatch;
    }

    public Button getQuitMatch() {
        return quitMatch;
    }

    public Label getLinkinfo() {
        return linkinfo;
    }

    public Label getMatchinfo() {
        return matchinfo;
    }

    public TextField getTextField() {
        return textField;
    }

    public void draw() {
        pane.setPrefHeight(prefHeight);
        pane.setPrefWidth(prefWidth);

        buttonLink.setText("开始链接");
        buttonMatch.setText("开始匹配");
        quitMatch.setText("退出匹配");
        buttonGame.setText("开始游戏");

        linkinfo.setText("No link");
        matchinfo.setText("No match");

        infobox.getChildren().addAll(buttonLink, textField, linkinfo);
        matchbox.getChildren().addAll(buttonMatch, quitMatch, matchinfo);
        allbox.getChildren().addAll(infobox, matchbox, buttonGame);
    }

}
