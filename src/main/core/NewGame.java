package main.core;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class NewGame {
    public NewPlayer playerPanel,enemyPanel;
    public Image image;
    private boolean running = false;
    private int shipsToPlace = 10;
    private int shipLength = 4;
    private boolean vertical=true;
    private Random random = new Random();
    private boolean enemyTurn = false;
    private VBox vbox;
    private HBox hbox,hbox1;
    private BorderPane bp,bp1,bp2;
    private ImageView img1;
    private Image image1;
    public static TextArea textArea;
    private TextArea stats;

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
        root.setPrefSize(820, 520);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        bp = new BorderPane();
        bp1 = new BorderPane();
        bp2 = new BorderPane();
        bp.setPrefSize(300,200);
        img1 = new ImageView();
        image1 = CreateImageShip(shipLength,vertical);
        img1.setImage(image1);
        bp.setCenter(img1);
        bp.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        playerPanel = new NewPlayer(false, event -> {
            if (running)
                return;
            Cell cell = (Cell) event.getSource();
            //Set vertical or horizontal
            if (event.getButton() == MouseButton.SECONDARY) {
                vertical=!vertical;
                image1 = CreateImageShip(shipLength,vertical);
                img1.setImage(image1);
            }
            if (event.getButton() == MouseButton.PRIMARY) {
                if (playerPanel.board.placeShip(new Ship(shipLength, vertical), cell.x, cell.y)) {
                    if (shipsToPlace == 10 || shipsToPlace == 8 || shipsToPlace == 5){
                        --shipLength;
                        image1 = CreateImageShip(shipLength,vertical);
                        img1.setImage(image1);
                    }
                    if (--shipsToPlace == 0) {
                        textArea = new TextArea();
                        stats = new TextArea();
                        hbox1 = new HBox();
                        bp.getChildren().remove(img1);
                        textArea.setEditable(false);
                        stats.setEditable(false);
                        textArea.setText("Game Started\n");
                        textArea.setPrefSize(610,200);
                        stats.setPrefSize(210,200);
                        hbox1.getChildren().addAll(textArea,stats);
                        bp.setCenter(hbox1);
                        startGame();
                    }
                }
            }
        });

        enemyPanel = new NewPlayer(true,event -> {
            if (!running)
                return;

            Cell cell = (Cell) event.getSource();
            if (cell.wasShot) {
                return;
            }

            enemyTurn = !cell.shoot();

            if (enemyPanel.board.ships == 0) {
                System.out.println("YOU WIN");
                System.exit(0);
            }

            if (enemyTurn) {
                enemyMove();
            }
        });
        Pane spa = new Pane();
        spa.setPrefSize(50,300);
        hbox = new HBox(playerPanel.panels,spa,enemyPanel.panels);
        hbox.setAlignment(Pos.CENTER);
        vbox = new VBox(hbox,bp);
        root.setCenter(vbox);
        return root;
    }
    private void enemyMove() {
        //textArea.setText("Enemy move\n"+textArea.getText());
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

    private Image CreateImageShip(int length,boolean vertical){
        int width,height;
        if(vertical) {
            width = 30;
            height = 30 * length;
        }else{
            width = 30 * length;
            height = 30;
        }
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(java.awt.Color.BLUE);
        g2d.fillRect(0, 0, width, height);
        g2d.fillOval(0, 0, width, height);
        g2d.dispose();
        Image img = SwingFXUtils.toFXImage(bufferedImage, null);
        return img;
    }
}
