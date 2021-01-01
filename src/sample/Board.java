package sample;
//libraries needed
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import sample.Exceptions.AdjacentTilesException;
import sample.Exceptions.InvalidCountExeception;
import sample.Exceptions.OverlapTilesException;
import sample.Exceptions.OversizeException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Board extends Parent {

    private VBox rows = new VBox();
    private boolean enemy = false;
    public int ships = 5; //number of ships
    static int CarrierShip = 0;
    static int BattleshipShip = 0;
    static int CruiserShip = 0;
    static int SubmarineShip = 0;
    static int DestroyerShip = 0;



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

    public boolean placeShip(Ship ship, int x, int y) throws FileNotFoundException {
        if (canPlaceShip(ship, x, y)) {
            int length = ship.type;

            if (ship.vertical) {
                for (int i = y; i < y + length; i++) {
                    Cell cell = getCell(x, i);
                    cell.ship = ship;
                    if (!enemy) {
                        FileInputStream inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/ship.png");
                        Image shipimg = new Image(inputstreamShip);

                        cell.setFill(new ImagePattern(shipimg));
                        cell.setStroke(Color.GREEN);
                    }
                }
            }
            else {
                for (int i = x; i < x + length; i++) {
                    Cell cell = getCell(i, y);
                    cell.ship = ship;
                    if (!enemy) {
                        FileInputStream inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/ship.png");
                        Image shipimg = new Image(inputstreamShip);

                        cell.setFill(new ImagePattern(shipimg));
                        //cell.setFill(Color.WHITE);
                        cell.setStroke(Color.GREEN);
                    }
                }
            }

            return true;
        }

        return false;
    }

    public boolean placeEnemyShip(Ship ship, int x, int y) throws FileNotFoundException {
        if (canPlaceEnemyShip(ship, x, y)) {
            int length = ship.type;

            if (ship.vertical) {
                for (int i = y; i < y + length; i++) {
                    Cell cell = getCell(x, i);
                    cell.ship = ship;
                    if (!enemy) {
                        FileInputStream inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/ship.png");
                        Image shipimg = new Image(inputstreamShip);

                        cell.setFill(new ImagePattern(shipimg));
                        cell.setStroke(Color.GREEN);
                    }
                }
            }
            else {
                for (int i = x; i < x + length; i++) {
                    Cell cell = getCell(i, y);
                    cell.ship = ship;
                    if (!enemy) {
                        FileInputStream inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/ship.png");
                        Image shipimg = new Image(inputstreamShip);

                        cell.setFill(new ImagePattern(shipimg));
                        //cell.setFill(Color.WHITE);
                        cell.setStroke(Color.GREEN);
                    }
                }
            }

            return true;
        }

        return false;
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
            try{
                isValidPoint(p);
                neighbors.add(getCell((int)p.getX(), (int)p.getY()));
            }catch (OversizeException e ){
                System.out.println(e);
            }
        }

        return neighbors.toArray(new Cell[0]);
    }

    private boolean canPlaceShip(Ship ship, int x, int y) {
        int length = ship.type;

        if (ship.vertical) {
            for (int i = y; i < y + length; i++) {
                try {
                    isValidPoint(x, i);
                    Cell cell = getCell(x, i);
                    try {
                        iSCellEmpty(cell);
                    } catch (OverlapTilesException e) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage() + x + i);
                        alert.showAndWait();
                        return false;
                    }

                    for (Cell neighbor : getNeighbors(x, i)) {
                        try {
                            isValidPoint(x, i);
                            try {
                                isNeighborEmpty(neighbor);
                            } catch (AdjacentTilesException e) {
                                Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                                alert.showAndWait();
                                return false;
                            }
                        } catch (OversizeException e) {
                           /* Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                            alert.showAndWait();
                            return false;*/
                        }
                    }

                } catch (OversizeException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                    alert.showAndWait();
                    return false;
                }
            }
        } else {
            for (int i = x; i < x + length; i++) {
                try {
                    isValidPoint(i, y);

                    Cell cell = getCell(i, y);
                    try {
                        iSCellEmpty(cell);
                    } catch (OverlapTilesException e) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                        alert.showAndWait();
                        return false;
                    }

                    for (Cell neighbor : getNeighbors(i, y)) {
                        try {
                            isValidPoint(i, y);
                            try {
                                isNeighborEmpty(neighbor);
                            } catch (AdjacentTilesException e) {
                                Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                                alert.showAndWait();
                                return false;
                            }
                        } catch (OversizeException e) {
                            /*Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                            alert.showAndWait();
                            return false;*/
                        }
                    }

                } catch (OversizeException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                    alert.showAndWait();
                    return false;
                }

            }
        }

       /* try {
            ShipNumber(ship);
        } catch (InvalidCountExeception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
            return false;
        }*/
        return true;
    }

    private boolean canPlaceEnemyShip(Ship ship, int x, int y) {
        int length = ship.type;

        if (ship.vertical) {
            for (int i = y; i < y + length; i++) {
                try {
                    isValidPoint(x, i);
                    Cell cell = getCell(x, i);
                    try {
                        iSCellEmpty(cell);
                    } catch (OverlapTilesException e) {
                        /*Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage() + x + i);
                        alert.showAndWait();*/
                        return false;
                    }

                    for (Cell neighbor : getNeighbors(x, i)) {
                        try {
                            isValidPoint(x, i);
                            try {
                                isNeighborEmpty(neighbor);
                            } catch (AdjacentTilesException e) {
                                /*Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                                alert.showAndWait();*/
                                return false;
                            }
                        } catch (OversizeException e) {
                           /* Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                            alert.showAndWait();
                            return false;*/
                        }
                    }

                } catch (OversizeException e) {
                    /*Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                    alert.showAndWait();*/
                    return false;
                }
            }
        } else {
            for (int i = x; i < x + length; i++) {
                try {
                    isValidPoint(i, y);

                    Cell cell = getCell(i, y);
                    try {
                        iSCellEmpty(cell);
                    } catch (OverlapTilesException e) {
                        /*Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                        alert.showAndWait();*/
                        return false;
                    }

                    for (Cell neighbor : getNeighbors(i, y)) {
                        try {
                            isValidPoint(i, y);
                            try {
                                isNeighborEmpty(neighbor);
                            } catch (AdjacentTilesException e) {
                                /*Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                                alert.showAndWait();*/
                                return false;
                            }
                        } catch (OversizeException e) {
                            /*Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                            alert.showAndWait();*/
                            return false;
                        }
                    }

                } catch (OversizeException e) {
                    /*Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                    alert.showAndWait();*/
                    return false;
                }

            }
        }

       /* try {
            ShipNumber(ship);
        } catch (InvalidCountExeception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
            return false;
        }*/
        return true;
    }

    private boolean isValidPoint(Point2D point) throws OversizeException {
        return isValidPoint(point.getX(), point.getY());
    }

    private boolean isValidPoint(double x, double y) throws OversizeException {
        if (x >= 0 && x < 10 && y >= 0 && y < 10){
            return true;
        }else{
            throw new OversizeException("Ship out of board!!!");
        }
    }

    public class Cell extends Rectangle {
        public int x, y;
        public Ship ship = null;
        public boolean wasShot = false;

        private Board board;

        public Cell(int x, int y, Board board) {
            super(25, 25);
            this.x = x;
            this.y = y;
            this.board = board;
            setFill(Color.LIGHTGRAY);
            setStroke(Color.BLACK);
        }

        public boolean shoot(boolean flag) throws FileNotFoundException {
            wasShot = true;
            FileInputStream inputstreamWave = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/wave.png");
            Image wave = new Image(inputstreamWave);

            setFill(new ImagePattern(wave));

            FileInputStream inputstreamFire = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/fire.png");
            Image fire = new Image(inputstreamFire);

            if (ship != null) {
                ship.hit();
                if(flag){ BattleshipMain.Score += ship.Score;
                }
                else{
                    BattleshipMain.EnemyScore += ship.Score;
                }
                setFill(new ImagePattern(fire));
                if (!ship.isAlive()) {
                    if(flag){
                        BattleshipMain.Score += ship.SinkBonus;
                    }
                    else{
                        BattleshipMain.EnemyScore += ship.SinkBonus;
                    }
                    board.ships--;
                }
                return true;
            }

            return false;
        }
    }

    private boolean iSCellEmpty(Cell cell) throws OverlapTilesException {
        if (cell.ship == null){
            return true;
        }else{
            throw new OverlapTilesException("Already a ship in this position!!!");
        }
    }

    private boolean isNeighborEmpty(Cell cell) throws AdjacentTilesException {
        if (cell.ship == null){
            return true;
        }
        else{
            throw  new AdjacentTilesException("Too close to another ship!!!");
        }
    }

    private boolean ShipNumber(Ship ship) throws InvalidCountExeception {
        if (ship.shipType == "Carrier" && (CarrierShip + 1) <= 1){
            CarrierShip += 1;
            return true;
        }
        if (ship.shipType == "Battleship" && (CarrierShip + 1) <= 1){
            BattleshipShip += 1;
            return true;
        }
        if (ship.shipType == "Cruiser" && (CarrierShip + 1) <= 1){
            CruiserShip += 1;
            return true;
        }
        if (ship.shipType == "Submarine" && (CarrierShip + 1) <= 1){
            SubmarineShip += 1;
            return true;
        }
        if (ship.shipType == "Destroyer" && (CarrierShip + 1) <= 1){
            DestroyerShip += 1;
            return true;
        }else{
            throw new InvalidCountExeception("You can only place one ship of type: " + ship.shipType);
        }
    }
}
