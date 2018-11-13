package main.core;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


public class Board extends Parent {
    private boolean enemy = false;
    private VBox rows = new VBox();
    public int ships = 5;

    public Board(boolean enemy, EventHandler<? super MouseEvent> handler) {
        this.enemy = enemy;
        for (int y = 0; y < 10; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 10; x++) {
                Cell c = new Cell(x, y, this);
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }
            rows.getChildren().add(row);
        }
        getChildren().add(rows);
    }
    public boolean placeShip(Ship ship, int x, int y) {
        if (canPlaceShip(ship, x, y)) {
            int length = ship.type;
            if (ship.vertical) {
                forifplaceShip(x,y,length,ship,ship.vertical);
            }
            else {
                forifplaceShip(y,x,length,ship,ship.vertical);
            }
            return true;
        }
        return false;
    }
    private boolean forifplaceShip(int xx, int yy, int length,Ship ship,boolean vertic){
        Cell cell = null;
        for (int i = yy; i < yy + length; i++) {
            if(vertic) {
                cell = getCell(xx, i);
            }else{
                cell = getCell(i,xx);
            }
            cell.ship = ship;
            if (!enemy) {
                if(ship.type == 5) {
                    cell.setFill(Color.BLUE);
                }else if(ship.type == 4){
                    cell.setFill(Color.AQUA);
                }else if(ship.type == 3){
                    cell.setFill(Color.ORANGE);
                }else if(ship.type == 2){
                    cell.setFill(Color.AZURE);
                }else if(ship.type == 1){
                    cell.setFill(Color.LIGHTCYAN);
                }
                cell.setStroke(Color.GREEN);
            }
        }
        return true;
    }

    private boolean canPlaceShip(Ship ship, int x, int y) {
        int length = ship.type;

        if (ship.vertical) {
            for (int i = y; i < y + length; i++) {
                if (!isValidPoint(x, i)) return false;
                Cell cell = getCell(x, i);
                if (cell.ship != null) return false;
                for (Cell neighbor : getNeighbors(x, i)) {
                    if (!isValidPoint(x, i)) return false;
                    if (neighbor.ship != null) return false;
                }
            }
        }
        else {
            for (int i = x; i < x + length; i++) {
                if (!isValidPoint(i, y)) return false;
                Cell cell = getCell(i, y);
                if (cell.ship != null) return false;
                for (Cell neighbor : getNeighbors(i, y)) {
                    if (!isValidPoint(i, y)) return false;
                    if (neighbor.ship != null) return false;
                }
            }
        }
        return true;
    }
    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }

    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    public Cell getCell(int x, int y) {
        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    private Cell[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[] {
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };
        List<Cell> neighbors = new ArrayList<Cell>();
        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbors.add(getCell((int)p.getX(), (int)p.getY()));
            }
        }
        return neighbors.toArray(new Cell[0]);
    }

}
