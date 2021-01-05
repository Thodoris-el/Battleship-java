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
