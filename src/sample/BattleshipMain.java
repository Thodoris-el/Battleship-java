package sample;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.sun.javafx.scene.paint.GradientUtils;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.stage.StageStyle;
import sample.Board.Cell;

public class BattleshipMain extends Application {
    static int Score = 0;
    static int EnemyScore = 0;
    private boolean running = false;
    private Board enemyBoard, playerBoard;
    private ArrayList<Point2D> NextMove = new ArrayList<Point2D>(4);
    private  ArrayList<Point2D> FinalNextMove = new ArrayList<Point2D>();
    Point2D previus = null;
    Point2D start = null;
    private int PlayernummerOfMoves = 40;
    private int EnemynummerOfMoves = 40;

    private int shipsToPlace = 4;
    private int ships[] = new int[5];
    private int scores[] = new int[5];
    private int sinks[] = new int[5];
    private String Names[] = new String[5];

    private boolean enemyTurn = false;

    private Random random = new Random();

    private boolean WhoStarts(){
        int temporary = random.nextInt(2);
        if (temporary == 0){
            return false;
        }
        else{
            return true;
        }
    }

    private Parent createContent() {
        ships[0] = 5;
        ships[1] = 4;
        ships[2] = 3;
        ships[3] = 3;
        ships[4] = 2;

        scores[0] = 350;
        scores[1] = 250;
        scores[2] = 100;
        scores[3] = 100;
        scores[4] = 50;

        sinks[0] = 1000;
        sinks[1] = 500;
        sinks[2] = 250;
        sinks[3] = 0;
        sinks[4] = 0;

        Names[0] = "Carrier";
        Names[1] = "Battleship";
        Names[2] = "Cruiser";
        Names[3] = "Submarine";
        Names[4] = "Destroyer";


        enemyTurn = WhoStarts();


        BorderPane root = new BorderPane();
        root.setPrefSize(1000, 800);
        Label SScore = new Label();
        SScore.setText("PLayer Score: "+ Score+" - Enemy Score: "+EnemyScore);
        SScore.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        SScore.setTextFill(Color.DARKRED);
        SScore.setAlignment(Pos.CENTER);
        root.setTop(SScore);


        Label LabelX = new Label("Axe X");
        LabelX.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        LabelX.setTextFill(Color.DARKRED);

        Label LabelY = new Label("Axe Y");
        LabelY.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        LabelY.setTextFill(Color.DARKRED);

        TextField PositionX = new TextField();
        TextField PositionY = new TextField();

        VBox tmp1 = new VBox(LabelX,PositionX);
        VBox tmp2 = new VBox(LabelY,PositionY);

        Button ShootButton = new Button("Shoot");

        HBox ShootContent = new HBox(tmp1,tmp2,new VBox(new Text(""),ShootButton));
        ShootContent.setAlignment(Pos.CENTER);
        root.setBottom(ShootContent);


        enemyBoard = new Board(true, event -> {
            if (!running)
                return;

            if(shipsToPlace == -1 ){
                shipsToPlace = -2;
                if(enemyTurn){
                    Alert alert = new Alert(Alert.AlertType.WARNING,"Enemy starts first!!!\n Click anywhere in the enemy board to start the game!!!");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING,"You start!!!\n Choose where to shoot!!!");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                }

            }

            Cell cell = (Cell) event.getSource();
            if (cell.wasShot)
                return;

            if(PlayernummerOfMoves == 0 && EnemynummerOfMoves == 0){
                ButtonType retry = new ButtonType("Retry",ButtonBar.ButtonData.OK_DONE);
                ButtonType quit = new ButtonType("Quit",ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert kk = new Alert(Alert.AlertType.WARNING,"You Win!!!",retry,quit);
                if (Score > EnemyScore){
                    kk.setContentText("You Win!!!\n Score: "+ Score + "- "+EnemyScore);
                }else if (Score < EnemyScore){
                    kk.setContentText("You Lose!!!\n Score: "+ Score + "- "+EnemyScore);
                }else{
                    kk.setContentText("It's a tie!!!\n Score: "+ Score + "- "+EnemyScore);
                }
                kk.setHeaderText(null);
                Optional<ButtonType> result = kk.showAndWait();
                if (result.get() == quit){
                    System.exit(0);
                }else{
                    try {
                        restart();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!enemyTurn && PlayernummerOfMoves != 0) {
                try {
                    enemyTurn = !cell.shoot(true);
                    enemyTurn = true;
                    PlayernummerOfMoves--;
                    SScore.setText("PLayer Score: "+ Score+" - Enemy Score: "+EnemyScore);
                    root.setTop(SScore);
                    //if (!cell.ship.isAlive()){
                    //}
                } catch (Exception e) {
                }
            }

            if (enemyBoard.ships == 0) {
                ButtonType retry = new ButtonType("Retry",ButtonBar.ButtonData.OK_DONE);
                ButtonType quit = new ButtonType("Quit",ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert kk = new Alert(Alert.AlertType.WARNING,"You Win!!!",retry,quit);
                kk.setContentText("You Win!!!");
                kk.setHeaderText(null);
                Optional<ButtonType> result = kk.showAndWait();
                if (result.get() == quit){
                    System.exit(0);
                }else{
                    try {
                        restart();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("YOU WIN");
            }

            if (enemyTurn)
                try {
                    enemyMove();
                    SScore.setText("PLayer Score: "+ Score+" - Enemy Score: "+EnemyScore);
                    root.setTop(SScore);
                }catch (Exception e){}
        });

        playerBoard = new Board(false, event -> {
            if (running)
                return;

            Cell cell = (Cell) event.getSource();
            try {
                if (playerBoard.placeShip(new Ship(ships[shipsToPlace], event.getButton() == MouseButton.PRIMARY,scores[shipsToPlace],sinks[shipsToPlace],Names[shipsToPlace]), cell.x, cell.y)) {
                    shipsToPlace--;
                    if (shipsToPlace < 0) {
                        startGame();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        Label enemyLabel = new Label("Enemy Board");
        enemyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        enemyLabel.setTextFill(Color.DARKRED);

        Label playerLabel = new Label("Player's Board");
        playerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        playerLabel.setTextFill(Color.DARKRED);


        VBox vbox1 = new VBox(50,enemyLabel, enemyBoard);
        vbox1.setAlignment(Pos.CENTER);

        VBox vbox2 = new VBox(50,playerLabel, playerBoard);
        vbox2.setAlignment(Pos.CENTER);

        root.setLeft(vbox2);
        root.setCenter(vbox1);

        return root;
    }

    private void enemyMove() throws InterruptedException {
        while (enemyTurn && EnemynummerOfMoves != 0) {
            int x = random.nextInt(10);;
            int y = random.nextInt(10);
            boolean Flag = false;
            if(!FinalNextMove.isEmpty()){
                x = (int)FinalNextMove.get(0).getX();
                y = (int)FinalNextMove.get(0).getY();
                FinalNextMove.remove(0);
                Flag = true;
            }
            else if (!NextMove.isEmpty()){
                x = (int) NextMove.get(0).getX();
                y = (int) NextMove.get(0).getY();
                NextMove.remove(0);
            }



            Cell cell = playerBoard.getCell(x, y);
            if (cell.wasShot)
                continue;
           try {
                enemyTurn = cell.shoot(false);
                if(enemyTurn){
                    if(previus==null){
                        previus = new Point2D(x,y);
                        NextMove.add(new Point2D(x,y+1));
                        NextMove.add(new Point2D(x,y-1));
                        NextMove.add(new Point2D(x+1,y));
                        NextMove.add(new Point2D(x-1,y));
                        Collections.shuffle(NextMove);
                    }else{
                        AI(x,y,Flag);
                    }
                }

                enemyTurn = false;
                EnemynummerOfMoves--;


                if(!cell.ship.isAlive()){
                    previus = null;
                    FinalNextMove.clear();
                    NextMove.clear();
                }
            }catch(Exception e){}

            if (playerBoard.ships == 0) {
                ButtonType retry = new ButtonType("Retry",ButtonBar.ButtonData.OK_DONE);
                ButtonType quit = new ButtonType("Quit",ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert kk = new Alert(Alert.AlertType.WARNING,"You Lose!!!",retry,quit);
                kk.setContentText("You Lose!!!");
                kk.setHeaderText(null);
                Optional<ButtonType> result = kk.showAndWait();
                if (result.get() == quit){
                    System.exit(0);
                }else{
                    try {
                        restart();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                System.out.println("YOU LOSE");

            }
        }
    }

    private void startGame() throws FileNotFoundException {
        // place enemy ships
        int type = 4;

        while (type >= 0) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if (enemyBoard.placeEnemyShip(new Ship(ships[type], Math.random() < 0.5,scores[type],sinks[type],Names[type]), x, y)) {
                type--;
            }
        }

        running = true;
    }

    void cleanup() {
        // stop animations reset model ect.
        this.primaryStage.close();

    }

    void restart() throws Exception {
        cleanup();
        BattleshipMain app = new BattleshipMain();
        app.start(new Stage());
    }

    public Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        Parent root = createContent();
        root.setId("pane");
        Scene scene = new Scene(root);
        primaryStage.setTitle("MediaLab Battleship");
        scene.getStylesheets().addAll(this.getClass().getResource("Styles.css").toExternalForm());
        this.primaryStage.setScene(scene);
        this.primaryStage.setResizable(false);
        this.primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);
    }
    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }

    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    private void AI(double x, double y,boolean flag){
        NextMove.clear();
        if(previus.getX() == x){
            if(previus.getY()+1==y){
                FinalNextMove.add(new Point2D(previus.getX(),previus.getY()+2));
                if(!flag){
                    FinalNextMove.add(new Point2D(previus.getX(),previus.getY()-1));
                }
            }else{
                FinalNextMove.add(new Point2D(previus.getX(),previus.getY()-2));
                if(!flag){
                    FinalNextMove.add(new Point2D(previus.getX(),previus.getY()+1));
                }
            }
        }else{
            if(previus.getX()+1==x){
                FinalNextMove.add(new Point2D(previus.getX()+2,previus.getY()));
                if(!flag) {
                    FinalNextMove.add(new Point2D(previus.getX() - 1, previus.getY()));
                }
            }else{
                FinalNextMove.add(new Point2D(previus.getX()-2,previus.getY()));
                if(!flag){
                    FinalNextMove.add(new Point2D(previus.getX()+1,previus.getY()));
                }
            }
        }
        previus = new Point2D(x,y);
    }

}

