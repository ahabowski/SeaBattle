package main.core;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends Rectangle {
    public int x, y;
    public Board board;
    public Ship ship=null;
    public boolean wasShot = false;

    public Cell(int x, int y, Board board) {
        super(30, 30);
        this.x = x;
        this.y = y;
        this.board = board;

        setFill(Color.LIGHTGRAY);
        setStroke(Color.BLACK);
    }
    public boolean shoot() {
        wasShot = true;
        setFill(Color.BLACK);
        if (ship != null) {
            ship.hit();
            setFill(Color.RED);
            if (!ship.isAlive()) {
                NewGame.textArea.setText("Ship is dead!\n" + NewGame.textArea.getText());
                board.ships--;
            }
            return true;
        }
        return false;
    }
}
