package application.controller;


import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class Controller{
    private static final int BOUND = 90;
    private static final int OFFSET = 15;
    private final Pane base_square = new Pane();
    private final Rectangle game_panel = new Rectangle();
    private final AnchorPane pane = new AnchorPane();
    private static final boolean TURN = false;
    private final int[][] chessBoard = new int[3][3];
    private String rivalName;
    private final Label labelOrder = new Label("X's turn to play");
    private final Label labelWin = new Label("No Winner");
    private final Button buttonQuit = new Button();
    private final VBox gamebox = new VBox();
    private final HBox centerBox = new HBox();
    private final HBox labelBox = new HBox();
    private final double Width = 600;
    private final double Height = 400;
    private Line line_1;
    private Line line_2;
    private Line line_3;
    private Line line_4;

    public double getWidth () {
        return Width;
    }

    public double getHeight () {
        return Height;
    }

    public int getChessBoard (int i, int j) {
        return chessBoard[i][j];
    }

    public void setChessBoard (int i, int j, int x) {
        chessBoard[i][j] = x;
    }

    public Rectangle getGame_panel () {
        return game_panel;
    }

    public VBox getGamebox () {
        return gamebox;
    }

    public Label getLabelOrder () {
        return labelOrder;
    }

    public Label getLabelWin () {
        return labelWin;
    }

    public Button getButtonQuit () {
        return buttonQuit;
    }

    public String getRivalName () {
        return rivalName;
    }

    public void setRivalName (String x) {
        rivalName = x;
    }
    public void draw () {

        pane.setPrefHeight(Height);
        pane.setPrefWidth(Width);

        base_square.setLayoutX(150.0);
        base_square.setLayoutY(50.0);
        base_square.setPrefHeight(300.0);
        base_square.setPrefWidth(300.0);

        game_panel.setArcHeight(5.0);
        game_panel.setArcWidth(5.0);
        game_panel.setFill(WHITE);
        game_panel.setHeight(270.0);
        game_panel.setLayoutX(15.0);
        game_panel.setLayoutY(15.0);
        game_panel.setStroke(BLACK);
        game_panel.setStrokeType(StrokeType.INSIDE);
        game_panel.setWidth(270.0);

        labelOrder.setPrefWidth(150.0);
        labelOrder.setPrefHeight(50.0);
        labelOrder.setFont(new Font("Cambria", 20));

        labelWin.setPrefWidth(150.0);
        labelWin.setPrefHeight(50.0);
        labelWin.setFont(new Font("Cambria", 20));

        buttonQuit.setText("退出游戏");

        line_1 = new Line();
        line_1.setEndX(170.0);
        line_1.setLayoutX(115.0);
        line_1.setLayoutY(105.0);
        line_1.setStartX(-100.0);

        line_2 = new Line();
        line_2.setEndX(170.0);
        line_2.setLayoutX(115.0);
        line_2.setLayoutY(195.0);
        line_2.setStartX(-100.0);

        line_3 = new Line();
        line_3.setEndX(170.0);
        line_3.setLayoutX(70.0);
        line_3.setLayoutY(150.0);
        line_3.setRotate(270.0);
        line_3.setStartX(-100.0);

        line_4 = new Line();
        line_4.setEndX(170.0);
        line_4.setLayoutX(160.0);
        line_4.setLayoutY(151.0);
        line_4.setRotate(90.0);
        line_4.setStartX(-100.0);

        gamebox.setAlignment(Pos.CENTER);
        centerBox.setAlignment(Pos.CENTER);
        labelBox.setAlignment(Pos.CENTER);
        base_square.getChildren().addAll(game_panel, line_1, line_2, line_3, line_4);
        centerBox.getChildren().addAll(base_square);
        labelBox.getChildren().addAll(labelOrder, labelWin, buttonQuit);
        gamebox.getChildren().addAll(centerBox, labelBox);

    }

    void drawCircle (int i, int j) {
        Circle circle = new Circle();
        base_square.getChildren().add(circle);
        circle.setCenterX(i * BOUND + BOUND / 2.0 + OFFSET);
        circle.setCenterY(j * BOUND + BOUND / 2.0 + OFFSET);
        circle.setRadius(BOUND / 2.0 - OFFSET / 2.0);
        circle.setStroke(Color.RED);
        circle.setFill(Color.TRANSPARENT);
    }

    void drawLine (int i, int j) {
        Line line_a = new Line();
        Line line_b = new Line();
        base_square.getChildren().add(line_a);
        base_square.getChildren().add(line_b);
        line_a.setStartX(i * BOUND + OFFSET * 1.5);
        line_a.setStartY(j * BOUND + OFFSET * 1.5);
        line_a.setEndX((i + 1) * BOUND + OFFSET * 0.5);
        line_a.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_a.setStroke(Color.BLUE);

        line_b.setStartX((i + 1) * BOUND + OFFSET * 0.5);
        line_b.setStartY(j * BOUND + OFFSET * 1.5);
        line_b.setEndX(i * BOUND + OFFSET * 1.5);
        line_b.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_b.setStroke(Color.BLUE);
    }

    void refreshGame () {
        base_square.getChildren().clear();
        base_square.getChildren().addAll(game_panel, line_1, line_2, line_3, line_4);
        labelWin.setText("No Winner");
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                setChessBoard(i, j, 0);
            }
        }
    }

}
