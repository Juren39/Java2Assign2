package application.controller;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static final int PLAY_1 = 1;
    private static final int PLAY_2 = 2;
    private static final int EMPTY = 0;
    private static final int BOUND = 90;
    private static final int OFFSET = 15;

    @FXML
    private Pane base_square;

    @FXML
    private Rectangle game_panel;

    private static boolean TURN = false;

    private static final int[][] chessBoard = new int[3][3];
    private static final boolean[][] flag = new boolean[3][3];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game_panel.setOnMouseClicked(event -> {
            int x = (int) (event.getX() / BOUND);
            int y = (int) (event.getY() / BOUND);
            if (refreshBoard(x, y)) {
                TURN = !TURN;
            }
        });
    }

    private boolean isGameOver (int player) {
        for (int i = 0; i < 3; i++) {
            if (chessBoard[i][0] == player
                    && chessBoard[i][1] == player
                    && chessBoard[i][2] == player){
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (chessBoard[0][i] == player
                    && chessBoard[1][i] == player
                    && chessBoard[2][i] == player){
                return true;
            }
        }
        if (chessBoard[0][0] == player
                && chessBoard[1][1] == player
                && chessBoard[2][2] == player){
            return true;
        }
        if (chessBoard[2][0] == player
                && chessBoard[1][1] == player
                && chessBoard[0][2] == player){
            return true;
        }

        return false;
    }
    private boolean refreshBoard (int x, int y) {
        if (chessBoard[x][y] == EMPTY) {
            chessBoard[x][y] = TURN ? PLAY_1 : PLAY_2;
            drawChess();
            if (isGameOver(chessBoard[x][y] = TURN ? PLAY_1 : PLAY_2)) {
                onHelloButtonClick();
            }
            return true;
        }
        return false;
    }

    private void drawChess () {
        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard[0].length; j++) {
                if (flag[i][j]) {
                    // This square has been drawing, ignore.
                    continue;
                }
                switch (chessBoard[i][j]) {
                    case PLAY_1:
                        drawCircle(i, j);
                        break;
                    case PLAY_2:
                        drawLine(i, j);
                        break;
                    case EMPTY:
                        // do nothing
                        break;
                    default:
                        System.err.println("Invalid value!");
                }
            }
        }
    }

    private void drawCircle (int i, int j) {
        Circle circle = new Circle();
        base_square.getChildren().add(circle);
        circle.setCenterX(i * BOUND + BOUND / 2.0 + OFFSET);
        circle.setCenterY(j * BOUND + BOUND / 2.0 + OFFSET);
        circle.setRadius(BOUND / 2.0 - OFFSET / 2.0);
        circle.setStroke(Color.RED);
        circle.setFill(Color.TRANSPARENT);
        flag[i][j] = true;
    }

    private void drawLine (int i, int j) {
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
        flag[i][j] = true;
    }

    private void refreshgame() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                flag[i][j] = false;
            }
        }
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                chessBoard[i][j] = EMPTY;
            }
        }
        TURN = false;
    }

    @FXML
    protected void onHelloButtonClick() {
        DialogPane dialog = new DialogPane();
        dialog.setHeaderText("有玩家胜利！");
        dialog.setContentText("是否继续？");

        dialog.getButtonTypes().add(ButtonType.YES);
        dialog.getButtonTypes().add(ButtonType.NO);
        dialog.getButtonTypes().add(ButtonType.CLOSE);

        Stage dialogStage = new Stage();
        Scene dialogScene = new Scene(dialog);
        dialogStage.setScene(dialogScene);

        Button close = (Button)dialog.lookupButton(ButtonType.NO);
        close.setOnAction(event -> {
            Stage stage = (Stage) base_square.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("Select.fxml"));
            Alert(dialogStage, stage, fxmlLoader);
        });

        Button apply = (Button)dialog.lookupButton(ButtonType.YES);
        apply.setOnAction(event -> {
            Stage stage = (Stage) base_square.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("mainUI.fxml"));
            Alert(dialogStage, stage, fxmlLoader);
        });

        dialogStage.show();
    }

    private void Alert(Stage dialogStage, Stage stage, FXMLLoader fxmlLoader) {
        Pane root;
        try {
            root = fxmlLoader.load();
            stage.setTitle("Tic Tac Toe");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            refreshgame();
            dialogStage.close();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
