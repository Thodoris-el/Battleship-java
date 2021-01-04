package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import sample.Board.Cell;

public class BattleshipMain extends Application {
    static int Score = 0;
    static int EnemyScore = 0;
    private boolean running = false;
    private Board enemyBoard, playerBoard;
    private ArrayList<Point2D> NextMove = new ArrayList<>(4);
    private  ArrayList<Point2D> FinalNextMove = new ArrayList<>();
    Point2D previus = null;
    private int PlayernummerOfMoves = 40;
    private int EnemynummerOfMoves = 40;

    private int shipsToPlace = 4;
    private int[] ships = new int[5];
    private int[] scores = new int[5];
    private int[] sinks = new int[5];
    private String[] Names = new String[5];
    private File Scenario;
    private String ScenarioID;
    ArrayList<Cell> PlayerHistory = new ArrayList<>(5);
    ArrayList<Cell> EnemyHistory = new ArrayList<>(5);
    static boolean FlagStart = false;
    public Label SScore = new Label();

    private boolean enemyTurn = false;

    private final Random random = new Random();

    private boolean WhoStarts(){
        int temporary = random.nextInt(2);
        return temporary != 0;
    }

    private void StartPlayer(){
        if(ScenarioID == null){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Load A SCenario Then Start A Game!!!");
            alert.setHeaderText("No Scenario Found");
            alert.showAndWait();
        }
        else {
            try {
                Scenario = new File("/home/thodoris/BattlesShipTest/src/sample/Scenarios/player_"+ScenarioID+".txt");
                Scanner reader = new Scanner(Scenario);
                while (reader.hasNext()) {
                    String data = reader.next();
                    int index = Character.getNumericValue(data.charAt(0)) - 1;
                    int CorY = Character.getNumericValue(data.charAt(2));
                    int CorX = Character.getNumericValue(data.charAt(4));
                    int vert = Character.getNumericValue(data.charAt(6));
                    boolean flag = (vert == 2);
                    Cell cell = playerBoard.getCell(CorX,CorY);
                    if(playerBoard.placeShip(new Ship(ships[index], flag,scores[index],sinks[index],Names[index]), cell.x, cell.y)){
                        shipsToPlace--;
                        if(shipsToPlace<0){
                            startGame();
                        }
                    }else{
                        FlagStart = false;
                        cleanup();
                        restart();
                    }
                }
            } catch (FileNotFoundException ee) {
                Alert alert = new Alert(Alert.AlertType.WARNING,"Maybe The Name Of The File Is Not Right,\n Please Load The Right Scenario!");
                alert.setHeaderText("Scenario Not Found!");
                alert.showAndWait();
                System.out.print("File not found");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Parent createContent(boolean flag) throws FileNotFoundException {
        ScenarioID = Board.ScenarioID;
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


        BorderPane root = new BorderPane();
        root.setPrefSize(1200, 700);

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
        ShootContent.setAlignment(Pos.TOP_CENTER);
        root.setBottom(ShootContent);
        EventHandler<ActionEvent> ButonEvent = e -> {
            String corX = PositionX.getText();
            String corY = PositionY.getText();
            try {
                if(PlayernummerOfMoves>0){
                playermove(corX,corY);
                }

            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
            try {
                if(EnemynummerOfMoves>0) {
                    enemyMove();
                }
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            SScore.setText("PLayer Score: "+ Score+" - Enemy Score: "+EnemyScore);
            root.setTop(SScore);
            End();
        };
        ShootButton.setOnAction(ButonEvent);


        Menu Application = new Menu("Application");
        MenuItem Start = new MenuItem("Start");
        Start.setOnAction(eStart -> {
            FlagStart = true;
            cleanup();
            try {
                restart();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        MenuItem Load = new MenuItem("Load");
        Load.setOnAction(eLoad -> {
            TextInputDialog LoadText = new TextInputDialog();
            LoadText.setHeaderText("Enter the name of the Scenario");
            LoadText.showAndWait();
            Board.ScenarioID = LoadText.getEditor().getText();
            cleanup();
            try {
                restart();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        MenuItem Exit = new MenuItem("Exit");
        Exit.setOnAction(eExit -> System.exit(0));

        Application.getItems().addAll(Start,Load,Exit);

        Menu Details = new Menu("Details");
        MenuItem EnemyShips = new MenuItem("Enemy Ships");
        EnemyShips.setOnAction(eDetails -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"");
            alert.setHeaderText("Enemy Ships Information");
            Ship tmpShip;
            StringBuilder alertText = new StringBuilder();
            if(enemyBoard.Ships.isEmpty()){
                alertText = new StringBuilder("No enemy ships are in the board");
            }
            for (int i = 0;i <enemyBoard.Ships.size();i++){
                tmpShip = enemyBoard.Ships.get(i);
                if(!tmpShip.isHit()){
                    alertText.append(tmpShip.shipType).append(": healthy\n");
                }
                else{
                    if(tmpShip.isAlive()){
                        alertText.append(tmpShip.shipType).append(": hitted\n");
                    }
                    else{
                        alertText.append(tmpShip.shipType).append(": sunk\n");
                    }
                }
            }
            alert.setContentText(alertText.toString());
            alert.showAndWait();
        });
        MenuItem PlayerShots = new MenuItem("Player Shots");
        PlayerShots.setOnAction(ePlayerShots -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"");
            StringBuilder alertText = new StringBuilder();
            if(PlayerHistory.isEmpty()){
                alertText = new StringBuilder("You haven't shoot anything yet");
            }
            for (Cell tmpCell : PlayerHistory) {
                if (tmpCell.ship == null) {
                    alertText.append("(").append(tmpCell.x).append(",").append(tmpCell.y).append(")").append(": Missed Shot\n");
                } else {
                    alertText.append("(").append(tmpCell.x).append(",").append(tmpCell.y).append(")").append(": Shot hit ").append(tmpCell.ship.shipType).append("\n");
                }
            }
            alert.setContentText(alertText.toString());
            alert.setHeaderText("Your Shot History");
            alert.showAndWait();
        });
        MenuItem EnemyShots = new MenuItem("EnemyShots");
        EnemyShots.setOnAction(eEnemyShots -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"");
            StringBuilder alertText = new StringBuilder();
            if(EnemyHistory.isEmpty()){
                alertText = new StringBuilder("No enemy moves yet");
            }
            for (Cell tmpCell : EnemyHistory) {
                if (tmpCell.ship == null) {
                    alertText.append("(").append(tmpCell.x).append(",").append(tmpCell.y).append(")").append(": Missed Shot\n");
                } else {
                    alertText.append("(").append(tmpCell.x).append(",").append(tmpCell.y).append(")").append(": Shot hit ").append(tmpCell.ship.shipType).append("\n");
                }
            }
            alert.setContentText(alertText.toString());
            alert.setHeaderText("Enemy Shot History");
            alert.showAndWait();
        });

        Details.getItems().addAll(EnemyShips,PlayerShots,EnemyShots);

        MenuBar BattleshipMenu = new MenuBar();
        BattleshipMenu.getMenus().addAll(Application,Details);
        root.setRight(BattleshipMenu);

        FileInputStream inputstreamShip;
        Label Cruiser = new Label("Cruiser");
        inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/cruiser.png");
        Image imgCruiser = new Image(inputstreamShip);
        ImageView imgCruiserView = new ImageView(imgCruiser);
        imgCruiserView.setFitHeight(25);
        imgCruiserView.setFitWidth(25);
        Cruiser.setGraphic(imgCruiserView);
        Cruiser.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        Cruiser.setTextFill(Color.DARKRED);

        Label Carrier = new Label("Carrier");
        inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/carrier.png");
        Image imgCarrier = new Image(inputstreamShip);
        ImageView imgCarrierView = new ImageView(imgCarrier);
        imgCarrierView.setFitHeight(25);
        imgCarrierView.setFitWidth(25);
        Carrier.setGraphic(imgCarrierView);
        Carrier.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        Carrier.setTextFill(Color.DARKRED);

        Label Battleship = new Label("Battleship");
        inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/batteship.png");
        Image imgBattleship = new Image(inputstreamShip);
        ImageView imgBattleshipView = new ImageView(imgBattleship);
        imgBattleshipView.setFitHeight(25);
        imgBattleshipView.setFitWidth(25);
        Battleship.setGraphic(imgBattleshipView);
        Battleship.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        Battleship.setTextFill(Color.DARKRED);

        Label Submarine = new Label("Submarine");
        inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/submarine.png");
        Image imgSubmarine = new Image(inputstreamShip);
        ImageView imgSubmarineView = new ImageView(imgSubmarine);
        imgSubmarineView.setFitHeight(25);
        imgSubmarineView.setFitWidth(25);
        Submarine.setGraphic(imgSubmarineView);
        Submarine.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        Submarine.setTextFill(Color.DARKRED);

        Label Destroyer = new Label("Destroyer");
        inputstreamShip = new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/destroyer.png");
        Image imgDestroyer = new Image(inputstreamShip);
        ImageView imgDestroyerView = new ImageView(imgDestroyer);
        imgDestroyerView.setFitHeight(25);
        imgDestroyerView.setFitWidth(25);
        Destroyer.setGraphic(imgDestroyerView);
        Destroyer.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        Destroyer.setTextFill(Color.DARKRED);


        VBox ShipIcons = new VBox(BattleshipMenu,Carrier,Battleship,Cruiser,Submarine,Destroyer);
        ShipIcons.setAlignment(Pos.CENTER);
        root.setRight(ShipIcons);

        enemyBoard = new Board(true);


        playerBoard = new Board(false);

        if(flag){
            StartPlayer();
        }


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
            int x = random.nextInt(10);
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
               if(EnemyHistory.size() == 5){
                   EnemyHistory.remove(0);
               }
               EnemyHistory.add(cell);
                enemyTurn = false;
                EnemynummerOfMoves--;


                if(!cell.ship.isAlive()){
                    previus = null;
                    FinalNextMove.clear();
                    NextMove.clear();
                }
            }catch(Exception ignored){}

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
        if(ScenarioID == null){
            Alert alert = new Alert(Alert.AlertType.WARNING,"First Load A Scenario Then Start A Game!");
            alert.setHeaderText("Scenario Not Provided");
            alert.showAndWait();
            System.out.println("Error");
        }
        else {
            try {
                Scenario = new File("/home/thodoris/BattlesShipTest/src/sample/Scenarios/enemy_"+ScenarioID+".txt");
                Scanner reader = new Scanner(Scenario);
                while (reader.hasNext()) {
                    String data = reader.next();
                    int index = Character.getNumericValue(data.charAt(0)) - 1;
                    int CorY = Character.getNumericValue(data.charAt(2));
                    int CorX = Character.getNumericValue(data.charAt(4));
                    int vert = Character.getNumericValue(data.charAt(6));
                    boolean flag = (vert == 2);
                    Cell cell = enemyBoard.getCell(CorX,CorY);
                    if(enemyBoard.placeEnemyShip(new Ship(ships[index], flag,scores[index],sinks[index],Names[index]), cell.x, cell.y)){
                        type--;
                    }
                    else{
                        FlagStart = false;
                        restart();
                    }
                }
            } catch (FileNotFoundException ee) {
                Alert alert = new Alert(Alert.AlertType.WARNING,"Scenario Name Might Not Be Right,\n Please Reload With The Right Name!");
                alert.setHeaderText("Scenario Not Found");
                alert.showAndWait();
                System.out.print("File not found");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        running = true;
    }

    void cleanup() {
        // stop animations reset model ect.
        this.primaryStage.close();
        if(Board.DestroyerShip >0) {
            Board.DestroyerShip -= 1;
        }
        if(Board.SubmarineShip >0) {
            Board.SubmarineShip -= 1;
        }
        if(Board.CruiserShip>0) {
            Board.CruiserShip = -1;
        }
        if(Board.BattleshipShip>0) {
            Board.BattleshipShip -= 1;
        }
        if(Board.CarrierShip>0) {
            Board.CarrierShip = -1;
        }
        if(Board.EnemyDestroyerShip>0) {
            Board.EnemyDestroyerShip -= 1;
        }
        if(Board.EnemySubmarineShip>0) {
            Board.EnemySubmarineShip -= 1;
        }
        if(Board.EnemyCruiserShip>0) {
            Board.EnemyCruiserShip = -1;
        }
        if(Board.EnemyBattleshipShip>0) {
            Board.EnemyBattleshipShip -= 1;
        }
        if(Board.EnemyCarrierShip>0) {
            Board.EnemyCarrierShip = -1;
        }

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
        Parent root = createContent(FlagStart);
        root.setId("pane");
        Scene scene = new Scene(root);
        primaryStage.setTitle("MediaLab Battleship");
        scene.getStylesheets().addAll(this.getClass().getResource("Styles.css").toExternalForm());
        this.primaryStage.setScene(scene);
        this.primaryStage.setResizable(false);
        this.primaryStage.show();
        enemyTurn = WhoStarts();
        if(shipsToPlace == -1 ){
            shipsToPlace = -2;
            Alert alert;
            if(enemyTurn){
                alert = new Alert(Alert.AlertType.WARNING, "Enemy starts first!!!\n Click anywhere in the enemy board to start the game!!!");
            }
            else{
                alert = new Alert(Alert.AlertType.WARNING, "You start!!!\n Choose where to shoot!!!");
            }
            alert.setHeaderText(null);
            alert.showAndWait();

        }

        if (enemyTurn && FlagStart) {
            try {
                enemyMove();
            } catch (Exception ignored) {
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
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

    private void playermove(String x,String y) throws FileNotFoundException {
        if (!running){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Wait for the game to start");
            alert.showAndWait();
        }
        else if (enemyTurn){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Wait for your turn!!!");
            alert.showAndWait();
        }
        if(x == null || y == null){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Please Provide Coordinates!!!");
            alert.showAndWait();
        }else {
            int Corx = Integer.parseInt(x);
            int Cory = Integer.parseInt(y);
            if (Corx >= 0 && Corx <= 9 && Cory >= 0 && Cory <= 9) {
                Cell cell = enemyBoard.getCell(Corx, Cory);
                if (cell.wasShot) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "You have already shoot this cell \n Please shoot another one");
                    alert.showAndWait();
                } else {
                    cell.shoot(true);
                    PlayernummerOfMoves--;
                    if (PlayerHistory.size() == 5) {
                        PlayerHistory.remove(0);
                    }
                    PlayerHistory.add(cell);
                    enemyTurn = true;
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Invalid coordinates!!! \n Please try again");
                alert.showAndWait();
            }
        }
    }

    public void End() {
        if (PlayernummerOfMoves == 0 && EnemynummerOfMoves == 0) {
            ButtonType retry = new ButtonType("Retry", ButtonBar.ButtonData.OK_DONE);
            ButtonType quit = new ButtonType("Quit", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert kk = new Alert(Alert.AlertType.WARNING, "You Win!!!", retry, quit);
            if (Score > EnemyScore) {
                kk.setContentText("You Win!!!\n Score: " + Score + "- " + EnemyScore);
            } else if (Score < EnemyScore) {
                kk.setContentText("You Lose!!!\n Score: " + Score + "- " + EnemyScore);
            } else {
                kk.setContentText("It's a tie!!!\n Score: " + Score + "- " + EnemyScore);
            }
            kk.setHeaderText(null);
            Optional<ButtonType> result = kk.showAndWait();
            if (result.get() == quit) {
                System.exit(0);
            } else {
                try {
                    restart();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING,"An Error Occurred,\n Please Re-Open The Game!!!");
                    alert.showAndWait();
                    e.printStackTrace();
                }
            }
        }

        if (enemyBoard.ships == 0) {
            ButtonType retry = new ButtonType("Retry", ButtonBar.ButtonData.OK_DONE);
            ButtonType quit = new ButtonType("Quit", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert kk = new Alert(Alert.AlertType.WARNING, "You Win!!!", retry, quit);
            kk.setContentText("You Win!!!");
            kk.setHeaderText(null);
            Optional<ButtonType> result = kk.showAndWait();
            if (result.get() == quit) {
                System.exit(0);
            } else {
                try {
                    restart();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING,"An Error Occurred,\n Please Re-Open The Game!!!");
                    alert.showAndWait();
                    e.printStackTrace();
                }
            }

            System.out.println("YOU WIN");
        }
    }
}

