package main.core;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class NewGame {
    public NewPlayer playerPanel,enemyPanel;
    private boolean running = false;
    private int shipsToPlace = 10;
    private int shipLength = 4;
    private boolean vertical=true;
    private Random random = new Random();
    private boolean enemyTurn = false;

    public NewGame(Stage primaryStage){
        primaryStage.setTitle("Sea Battle");
        BorderPane panels = new BorderPane();
        panels.setPrefSize(300,300);
        Button newgamebtn = new Button("NewGame");
        newgamebtn.setOnAction(e -> {
            Scene scene_game = new Scene(DrawGuiPanel());
            primaryStage.setScene(scene_game);
            primaryStage.setResizable(true);
            primaryStage.setResizable(false);
        });

        VBox vbox = new VBox(50,newgamebtn);
        vbox.setAlignment(Pos.CENTER);
        panels.setCenter(vbox);
        panels.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(panels);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    public Parent DrawGuiPanel(){
        BorderPane root = new BorderPane();
        root.setPrefSize(820, 320);

        playerPanel = new NewPlayer(false,event -> {
            if (running)
                return;
            Cell cell = (Cell) event.getSource();
            //Set vertical or horizontal
            if (event.getButton() == MouseButton.SECONDARY) {
                vertical=!vertical;
            }
            if (event.getButton() == MouseButton.PRIMARY) {
                if (playerPanel.board.placeShip(new Ship(shipLength, vertical), cell.x, cell.y)) {
                    if (shipsToPlace == 10 || shipsToPlace == 8 || shipsToPlace == 5){
                        shipLength--;
                    }
                    if (--shipsToPlace == 0) {
                        startGame();
                    }
                }
            }
        });

        enemyPanel = new NewPlayer(true,event -> {
            if (!running)
                return;

            Cell cell = (Cell) event.getSource();
            if (cell.wasShot)
                return;

            enemyTurn = !cell.shoot();

            if (enemyPanel.board.ships == 0) {
                System.out.println("YOU WIN");
                System.exit(0);
            }

            if (enemyTurn)
                enemyMove();
        });
        Pane spa = new Pane();
        spa.setPrefSize(50,300);
        HBox hbox = new HBox(playerPanel.panels,spa,enemyPanel.panels);
        hbox.setAlignment(Pos.CENTER);
        root.setCenter(hbox);
        return root;
    }
    private void enemyMove() {
        while (enemyTurn) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            Cell cell = playerPanel.board.getCell(x, y);
            if (cell.wasShot)
                continue;
            enemyTurn = cell.shoot();
            if (playerPanel.board.ships == 0) {
                System.out.println("YOU LOSE");
                System.exit(0);
            }
        }
    }
    private void startGame() {
        // place enemy ships
        int type = 10;
        shipLength = 4;
        while (type > 0) {
            //System.out.println("type: " + type);
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if (enemyPanel.board.placeShip(new Ship(shipLength, Math.random() < 0.5), x, y)) {
                if (type == 10 || type == 8 || type == 5){
                    shipLength--;
                }
                type--;
            }
        }
        running = true;
    }
}
