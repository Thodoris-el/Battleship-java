package sample;


import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import sample.Exceptions.AdjacentTilesException;
import sample.Exceptions.InvalidCountExeception;
import sample.Exceptions.OverlapTilesException;
import sample.Exceptions.OversizeException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Board Class</h1>
 * <p>This Class implements the board in whitch the game is played.</p>
 *
 * @author Thodoris Anagnostopoulos
 * @version 2.0
 * @since 2020-1-5
 */

public class Board extends Parent {

    private VBox rows = new VBox();
    private boolean enemy;
    public int ships = 5; //number of ships
    static int CarrierShip = 0;
    static int BattleshipShip = 0;
    static int CruiserShip = 0;
    static int SubmarineShip = 0;
    static int DestroyerShip = 0;
    static int EnemyCarrierShip = 0;
    static int EnemyBattleshipShip = 0;
    static int EnemyCruiserShip = 0;
    static int EnemySubmarineShip = 0;
    static int EnemyDestroyerShip = 0;
    ArrayList<Ship> Ships = new ArrayList<>();
    static String ScenarioID;


    /**
     * First Constructor of the board class
     * @param enemy Boolean that defines if the board belongs to the enemy or to the player
     */
    public Board(boolean enemy) {


        this.enemy = enemy;
        for (int y = 0; y < 10; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 10; x++) {
                Cell c = new Cell(x, y, this);

                row.getChildren().add(c);
            }

            rows.getChildren().add(row);
        }


        getChildren().add(rows);
    }

    /**
     * Second Constructor of the board class
     * @param enemy Boolean that defines if the board belongs to the enemy or to the player
     * @param handler Receives the cell in which the palyer clicks
     */
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

    /**
     * Place a player's ship in the board if it is possible
     * @param ship the ship that is to be puttied in the board
     * @param x Coordinate X of the board
     * @param y Coordinate Y of the board
     * @return true if the ship is placed
     * @throws FileNotFoundException if image of the ship is not found
     */
    public boolean placeShip(Ship ship, int x, int y) throws FileNotFoundException {
        if (canPlaceShip(ship, x, y)) {
            int length = ship.type;
            Color ShipColor = Color.BLACK;

            if (ship.vertical) {
                for (int i = y; i < y + length; i++) {
                    Cell cell = getCell(x, i);
                    cell.ship = ship;
                    if (!enemy) {
                        try {
                            FileInputStream inputstreamShip;
                            switch (ship.shipType) {
                                case "Carrier":
                                    inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/carrier.png");
                                    ShipColor = Color.YELLOW;
                                    break;
                                case "Battleship":
                                    inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/batteship.png");
                                    ShipColor = Color.DARKGREEN;
                                    break;
                                case "Cruiser":
                                    inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/cruiser.png");
                                    ShipColor = Color.BROWN;
                                    break;
                                case "Submarine":
                                    inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/submarine.png");
                                    ShipColor = Color.BLUE;
                                    break;
                                default:
                                    inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/destroyer.png");
                                    ShipColor = Color.GRAY;
                                    break;
                            }


                            Image shipimg = new Image(inputstreamShip);

                            cell.setFill(new ImagePattern(shipimg));
                        }catch (FileNotFoundException e) {
                            cell.setFill(ShipColor);
                            System.out.println(e);
                        }
                            cell.setStroke(Color.GREEN);
                    }
                }
            }
            else {
                for (int i = x; i < x + length; i++) {
                    Cell cell = getCell(i, y);
                    cell.ship = ship;
                    if (!enemy) {
                        try {
                            FileInputStream inputstreamShip;
                            switch (ship.shipType) {
                                case "Carrier":
                                    inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/carrier.png");
                                    ShipColor = Color.YELLOW;
                                    break;
                                case "Battleship":
                                    inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/batteship.png");
                                    ShipColor = Color.DARKGREEN;
                                    break;
                                case "Cruiser":
                                    inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/cruiser.png");
                                    ShipColor = Color.BROWN;
                                    break;
                                case "Submarine":
                                    inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/submarine.png");
                                    ShipColor = Color.BLUE;
                                    break;
                                default:
                                    inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/destroyer.png");
                                    ShipColor = Color.GRAY;
                                    break;
                            }
                            Image shipimg = new Image(inputstreamShip);

                            cell.setFill(new ImagePattern(shipimg));
                        }catch(FileNotFoundException e) {
                            cell.setFill(ShipColor);
                            System.out.println(e);
                        }
                        cell.setStroke(Color.GREEN);
                    }
                }
            }
            Ships.add(ship);
            return true;
        }

        return false;
    }

    /**
     * Place a enemy's ship in the board if it is possible
     * @param ship the ship that is to be puttied in the board
     * @param x Coordinate X of the board
     * @param y Coordinate Y of the board
     * @return true if the ship is placed
     * @throws FileNotFoundException if image of the ship is not found
     */
    public boolean placeEnemyShip(Ship ship, int x, int y) throws FileNotFoundException {
        if (canPlaceEnemyShip(ship, x, y)) {
            int length = ship.type;

            if (ship.vertical) {
                for (int i = y; i < y + length; i++) {
                    Cell cell = getCell(x, i);
                    cell.ship = ship;
                    if (!enemy) {
                        try {
                            FileInputStream inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/ship.png");
                            Image shipimg = new Image(inputstreamShip);
                            cell.setFill(new ImagePattern(shipimg));
                        }catch (FileNotFoundException e) {
                            cell.setFill(Color.YELLOW);
                            System.out.println(e);
                        }

                        cell.setStroke(Color.GREEN);
                    }
                }
            }
            else {
                for (int i = x; i < x + length; i++) {
                    Cell cell = getCell(i, y);
                    cell.ship = ship;
                    if (!enemy) {
                        try {
                            FileInputStream inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/ship.png");
                            Image shipimg = new Image(inputstreamShip);
                            cell.setFill(new ImagePattern(shipimg));
                        }catch (FileNotFoundException e){
                            cell.setFill(Color.YELLOW);
                            System.out.println(e);
                        }
                        cell.setStroke(Color.GREEN);
                    }
                }
            }
            Ships.add(ship);
            return true;
        }

        return false;
    }

    /**
     * Return the cell with the given coordinates
     * @param x Coordinate X of the board
     * @param y Coordinate Y of the Board
     * @return Return a cell
     */
    public Cell getCell(int x, int y) {
        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    /**
     * Return the neighbors of the cell with the given coordinates.
     * This neighbors are the (x+1,y) - (x-1,y) - (x,y+1) - (x,y-1)
     * @param x Coordinate X of the board
     * @param y Coordinate Y of the board
     * @return Returns a cell array that has the neighboors ot the given cell
     */
    private Cell[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[] {
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };

        List<Cell> neighbors = new ArrayList<>();

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

    /**
     * Function that return true only if the given player's ship can be placed in the given coordinates
     * @param ship The player's ship
     * @param x Coordinate X of the board
     * @param y Coordinate Y of the board
     * @return True if the ship can be placed
     */
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
                           System.out.println(e);
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
                            System.out.println(e);
                        }
                    }

                } catch (OversizeException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                    alert.showAndWait();
                    return false;
                }

            }
        }

       try {
            ShipNumber(ship);
        } catch (InvalidCountExeception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Function that return true only if the given enemy's ship can be placed in the given coordinates
     * @param ship The player's ship
     * @param x Coordinate X of the board
     * @param y Coordinate Y of the board
     * @return True if the ship can be placed
     */
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
                           System.out.println(e);
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
                            System.out.println(e);
                        }
                    }

                } catch (OversizeException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                    alert.showAndWait();
                    return false;
                }

            }
        }

       try {
            EnemyShipNumber(ship);
        } catch (InvalidCountExeception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Returns true if the given point belongs to the board.
     * @param point The given point
     * @return true if the given point belongs to the board
     * @throws OversizeException exception if the point is out of board
     */
    private boolean isValidPoint(Point2D point) throws OversizeException {
        return isValidPoint(point.getX(), point.getY());
    }

    /**
     * Returns true if the given coordinates belongs to the board.
     * @param x Coordinate X
     * @param y Coordinate Y
     * @return true if the given coordinates belongs to the board
     * @throws OversizeException exception if the coordinates is out of board
     */
    private boolean isValidPoint(double x, double y) throws OversizeException {
        if (x >= 0 && x < 10 && y >= 0 && y < 10){
            return true;
        }else{
            throw new OversizeException("Ship out of board!!!");
        }
    }

    /**
     * <h1>Cell Class</h1>
     * <p>This class implements the cell class that shapes the board</p>
     */
    public class Cell extends Rectangle {
        public int x, y;
        public Ship ship = null;
        public boolean wasShot = false;

        private Board board;

        /**
         * The Cell constructor
         * @param x Cell coordinate X
         * @param y Cell coordinate Y
         * @param board Board that will include the cell
         */
        public Cell(int x, int y, Board board) {
            super(25, 25);
            this.x = x;
            this.y = y;
            this.board = board;
            setFill(Color.LIGHTGRAY);
            setStroke(Color.BLACK);
        }

        /**
         * Shoot the given cell and repaint the cell according to the result of the shoot
         * @param flag true if the player shots
         * @return true if the shot is successful
         * @throws FileNotFoundException exception if the images for the fire(successful shot) and the wave(unsuccessful shot) is not found
         */
        public boolean shoot(boolean flag) throws FileNotFoundException {
            wasShot = true;
            try {
                FileInputStream inputstreamWave = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/wave.png");
                Image wave = new Image(inputstreamWave);

                setFill(new ImagePattern(wave));
            }catch (FileNotFoundException e){
                setFill(Color.BLACK);
                System.out.println(e);
            }

            if (ship != null) {
                ship.hit();
                if(flag){ BattleshipMain.Score += ship.Score;
                }
                else{
                    BattleshipMain.EnemyScore += ship.Score;
                }
                try {
                    FileInputStream inputstreamFire = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/fire.png");
                    Image fire = new Image(inputstreamFire);
                    setFill(new ImagePattern(fire));
                }catch(FileNotFoundException e){
                    setFill(Color.RED);
                    System.out.println(e);
                }
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

    /**
     * Retyrns true if the given cell doesn't have a ship
     * @param cell given cell
     * @return true if the cell is empty
     * @throws OverlapTilesException Exception if the cell has a ship
     */
    private boolean iSCellEmpty(Cell cell) throws OverlapTilesException {
        if (cell.ship == null){
            return true;
        }else{
            throw new OverlapTilesException("Already a ship in this position!!!");
        }
    }

    /**
     * Returns true if the neighboors of a cell are empty
     * @param cell given cell
     * @return true if the neighboors doesn't have a ship
     * @throws AdjacentTilesException Exception if one ore more neughboors aren't empty
     */
    private boolean isNeighborEmpty(Cell cell) throws AdjacentTilesException {
        if (cell.ship == null){
            return true;
        }
        else{
            throw  new AdjacentTilesException("Too close to another ship!!!");
        }
    }

    /**
     * Returns true if the given type of the player's ship hasn't already been put to a cell
     * @param ship given ship
     * @return true if the given type of the ship hasn't already been put to a cell
     * @throws InvalidCountExeception Exception if the given shop type has a;ready benn put to a cell
     */
    private boolean ShipNumber(Ship ship) throws InvalidCountExeception {
        if (ship.shipType.equals("Carrier") && (CarrierShip + 1) <= 1){
            CarrierShip += 1;
            return true;
        }
        if (ship.shipType.equals("Battleship") && (BattleshipShip + 1) <= 1){
            BattleshipShip += 1;
            return true;
        }
        if (ship.shipType.equals("Cruiser") && (CruiserShip + 1) <= 1){
            CruiserShip += 1;
            return true;
        }
        if (ship.shipType.equals("Submarine") && (SubmarineShip + 1) <= 1){
            SubmarineShip += 1;
            return true;
        }
        if (ship.shipType.equals("Destroyer") && (DestroyerShip + 1) <= 1){
            DestroyerShip += 1;
            return true;
        }else{
            throw new InvalidCountExeception("You can only place one ship of type: " + ship.shipType);
        }
    }

    /**
     * Returns true if the given type of the player's ship hasn't already been put to a cell
     * @param ship given ship
     * @return true if the given type of the ship hasn't already been put to a cell
     * @throws InvalidCountExeception Exception if the given shop type has a;ready benn put to a cell
     */
    private boolean EnemyShipNumber(Ship ship) throws InvalidCountExeception {
        if (ship.shipType.equals("Carrier") && (EnemyCarrierShip + 1) <= 1){
            EnemyCarrierShip += 1;
            return true;
        }
        if (ship.shipType.equals("Battleship") && (EnemyBattleshipShip + 1) <= 1){
            EnemyBattleshipShip += 1;
            return true;
        }
        if (ship.shipType.equals("Cruiser") && (EnemyCruiserShip + 1) <= 1){
            EnemyCruiserShip += 1;
            return true;
        }
        if (ship.shipType.equals("Submarine") && (EnemySubmarineShip + 1) <= 1){
            EnemySubmarineShip += 1;
            return true;
        }
        if (ship.shipType.equals("Destroyer") && (EnemyDestroyerShip + 1) <= 1){
            EnemyDestroyerShip += 1;
            return true;
        }else{
            throw new InvalidCountExeception("You can only place one ship of type: " + ship.shipType);
        }
    }
}
