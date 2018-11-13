package main.core;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class NewPlayer {
    public boolean enemy;
    public BorderPane panels;
    public Board board;


    public NewPlayer(boolean enemy, EventHandler<? super MouseEvent> handler){
        this.enemy = enemy;
        panels = new BorderPane();
        panels.setPrefSize(400,400);
        if(enemy) {
            panels.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        }else{
            panels.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        board = new Board(enemy,handler);
        panels.setCenter(board);
    }
}
