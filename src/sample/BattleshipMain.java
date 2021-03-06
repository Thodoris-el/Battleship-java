package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
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
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.util.Duration;
import sample.Board.Cell;

/**
 * <h1>Battleship Game</h1>
 * <p>This class implements the main game</p>
 *
 * @author Thodoris Anagnostopoulos
 * @version 2.0
 * @since 2020-1-5
 */
public class BattleshipMain extends Application {

    //Varriables needed -> Start
    static int Score = 0; // Score for the player
    static int EnemyScore = 0; // Score for the enemy
    private boolean running = false; //if running true
    private Board enemyBoard, playerBoard; //the two boards of the game
    private ArrayList<Point2D> NextMove = new ArrayList<>(4); //used for AI move prediction
    private  ArrayList<Point2D> FinalNextMove = new ArrayList<>(); //used for AI move prediction
    Point2D previus = null; // used for AI move prediction
    private int PlayernummerOfMoves = 40; //number of moves allowed for the player
    private int EnemynummerOfMoves = 40; //number of moves allowed for the player

    private int shipsToPlace = 4; //number of the ships that player can put
    private int[] ships = new int[5]; // player ships array
    private int[] scores = new int[5]; // Scores for the ships
    private int[] sinks = new int[5]; //scores for when the ship is sunked
    private String[] Names = new String[5]; //names of the ships
    private File Scenario; // stores the scenario
    private String ScenarioID; // stores the scenario name
    ArrayList<Cell> PlayerHistory = new ArrayList<>(5); // stores last 5 shots of player
    ArrayList<Cell> EnemyHistory = new ArrayList<>(5); // stores last 5 shots of enemy
    static boolean FlagStart = false; //true if player plays with scenario
    public Label SScore = new Label(); //where the score is displayed

    private boolean enemyTurn = false; //true if it is enemy's turn

    private final Random random = new Random(); // selects who to start
    //->end

    /**
     * This function returns randomly who will start first, the enemy or the player
     * @return true if the enemy starts first
     */
    private boolean WhoStarts(){
        int temporary = random.nextInt(2);
        return temporary != 0;
    }

    /**
     * this function tests if the scenario is valid
     * @return true if it is valid, otherwise returns false
     * @throws FileNotFoundException
     */
   private boolean fileTester() throws FileNotFoundException {
        int numS = 4;
        System.out.println(ScenarioID);
        URL url = getClass().getResource("Medialab/player_"+ScenarioID+".txt");
       if (url != null) {

           Scenario = new File(url.getPath());//("/home/thodoris/BattlesShipTest/src/sample/Scenarios/player_"+ScenarioID+".txt");
           Scanner reader = new Scanner(Scenario);
           while (reader.hasNext()) {
               String data = reader.next();
               int index = Character.getNumericValue(data.charAt(0)) - 1;
               System.out.println(index);
               int CorY = Character.getNumericValue(data.charAt(2));
               int CorX = Character.getNumericValue(data.charAt(4));
               int vert = Character.getNumericValue(data.charAt(6));
               boolean flag = (vert == 2);
               if(CorX < 0 || CorX > 9 || CorY < 0 || CorY >9 ){
                   System.out.println("j");
                   return false;
               }
               Cell cell = playerBoard.getCell(CorX, CorY);
               if (playerBoard.placeShip(new Ship(ships[index], flag, scores[index], sinks[index], Names[index]), cell.x, cell.y)) {
                   numS--;
                   if (numS < 0) {
                       System.out.println("jj");
                       return true;
                   }
               } else {
                   System.out.println("k");
                   return false;
               }
           }
       }
       numS = 4;
       url = getClass().getResource("Medialab/enemy_"+ScenarioID+".txt");
       if (url != null) {
           Scenario = new File(url.getPath());//("/home/thodoris/BattlesShipTest/src/sample/Scenarios/player_"+ScenarioID+".txt");
           Scanner reader = new Scanner(Scenario);
           while (reader.hasNext()) {
               String data = reader.next();
               int index = Character.getNumericValue(data.charAt(0)) - 1;
               System.out.println(index);
               int CorY = Character.getNumericValue(data.charAt(2));
               int CorX = Character.getNumericValue(data.charAt(4));
               int vert = Character.getNumericValue(data.charAt(6));
               boolean flag = (vert == 2);
               if(CorX < 0 || CorX > 9 || CorY < 0 || CorY >9 ){
                   return false;
               }
               Cell cell = playerBoard.getCell(CorX, CorY);
               if (playerBoard.placeEnemyShip(new Ship(ships[index], flag, scores[index], sinks[index], Names[index]), cell.x, cell.y)) {
                   numS--;
                   if (numS < 0) {
                       return true;
                   }
               } else {
                   return false;
               }
           }
       }
       return false;


    }

    /**
     * This function puts the player's ship in the board according to the given scenario
     */
    private void StartPlayer(){
        if(ScenarioID == null){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Load A SCenario Then Start A Game!!!");
            alert.setHeaderText("No Scenario Found");
            alert.showAndWait();
        }
        else {
            try {
                URL url = getClass().getResource("Medialab/player_"+ScenarioID+".txt");
                if (url != null) {
                    Scenario = new File(url.getPath());//("/home/thodoris/BattlesShipTest/src/sample/Scenarios/player_"+ScenarioID+".txt");
                    Scanner reader = new Scanner(Scenario);
                    while (reader.hasNext()) {
                        String data = reader.next();
                        int index = Character.getNumericValue(data.charAt(0)) - 1;
                        System.out.println(index);
                        int CorY = Character.getNumericValue(data.charAt(2));
                        int CorX = Character.getNumericValue(data.charAt(4));
                        int vert = Character.getNumericValue(data.charAt(6));
                        boolean flag = (vert == 2);
                        if(CorX < 0 || CorX > 9 || CorY < 0 || CorY >9 ){
                            FlagStart = false;
                            Alert alert = new Alert(Alert.AlertType.WARNING,"Ship out of order!!!\n Please load another Scenario");
                            alert.setHeaderText("Ship out of border");
                            alert.showAndWait();
                            FlagStart = false;
                            primaryStage.close();
                            cleanup1();
                            restart();
                            return;
                        }
                        Cell cell = playerBoard.getCell(CorX, CorY);
                        if (playerBoard.placeShip(new Ship(ships[index], flag, scores[index], sinks[index], Names[index]), cell.x, cell.y)) {
                            shipsToPlace--;
                            if (shipsToPlace < 0) {
                                startGame();
                            }
                        } else {
                            FlagStart = false;
                            primaryStage.close();
                            cleanup1();
                            restart();
                        }
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING,"Maybe The Name Of The File Is Not Right,\n Please Load The Right Scenario!");
                    alert.setHeaderText("Scenario Not Found!");
                    alert.showAndWait();
                    System.out.print("File not found");
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

    /**
     * This functions create everything needed for the game.
     * Create the application and the deatil menu
     * Create the Shoot button
     * Create the Score
     * Create the enemy's and the player's board
     * @param flag if true it loads the scenario to start the game
     * @return
     * @throws FileNotFoundException exception of the images that are shown in the game is not found
     */
    private static MediaPlayer mediaPlayer;
    private Parent createContent(boolean flag) throws FileNotFoundException {
        ScenarioID = Board.ScenarioID;
        //Load the music -Start
        URL url = getClass().getResource("Makai Symphony - Dragon Castle.mp3");
            try{
            File songFile = new File(url.getPath());
            Media media = new Media(url.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.seek(Duration.ZERO);
                    mediaPlayer.play();
                }
            });
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setVolume(0.3);
        }catch(Exception ignore){
                System.out.println("Music file could not be found");
        }
        //-> end



        //Lengths of the ships
        ships[0] = 5;
        ships[1] = 4;
        ships[2] = 3;
        ships[3] = 3;
        ships[4] = 2;

        //scores of the ships
        scores[0] = 350;
        scores[1] = 250;
        scores[2] = 100;
        scores[3] = 100;
        scores[4] = 50;

        //sink scores of the ships
        sinks[0] = 1000;
        sinks[1] = 500;
        sinks[2] = 250;
        sinks[3] = 0;
        sinks[4] = 0;

        //names of the ships
        Names[0] = "Carrier";
        Names[1] = "Battleship";
        Names[2] = "Cruiser";
        Names[3] = "Submarine";
        Names[4] = "Destroyer";


        //create window
        BorderPane root = new BorderPane();
        root.setPrefSize(1200, 700);

        //put score label -> Start
        SScore.setText("PLayer Score: "+ Score+" - Enemy Score: "+EnemyScore);
        SScore.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        SScore.setTextFill(Color.DARKRED);
        SScore.setAlignment(Pos.CENTER);
        root.setTop(SScore);
        //-> end


        //Label for shoot coordinates - Start
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
        //-> end

        //create the shoot button - Start
        Button ShootButton = new Button("Shoot");
        try{
            URL urlCannon = getClass().getResource("Images/cannon.png");
            FileInputStream cannon = new FileInputStream(urlCannon.getPath());//new FileInputStream("/home/thodoris/BattlesShipTest/src/sample/cannon.png");
            Image cannonImg= new Image(cannon);
            ImageView cannonView = new ImageView(cannonImg);
            cannonView.setFitHeight(40);
            cannonView.setFitWidth(40);
            cannonView.setPreserveRatio(true);
            ShootButton.setGraphic(cannonView);
        }catch (FileNotFoundException e){
            System.out.println(e);
        }

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
        //-> end


        //create the two menus - Start
        Menu Application = new Menu("Application");
        MenuItem Start = new MenuItem("Start");
        //define start button
        Start.setOnAction(eStart -> {
           if(FlagStart == true){

               /* Alert alert = new Alert(Alert.AlertType.CONFIRMATION," Press start to load the last scenario \n or else press click");
                alert.setHeaderText("New Game!");
                alert.showAndWait();*/
                cleanup();
                try {
                    restart();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else{
                try {
                    if (!fileTester()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Scenario is invalid!!! \n Please load another one");
                        alert.setHeaderText("Invalid Scenario");
                        alert.showAndWait();
                    } else {
                        FlagStart = true;
                        cleanup();
                        try {
                            restart();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        });
        //define load button
        MenuItem Load = new MenuItem("Load");
        Load.setOnAction(eLoad -> {
            TextInputDialog LoadText = new TextInputDialog();
            LoadText.setHeaderText("Enter the name of the Scenario");
            LoadText.showAndWait();
            Board.ScenarioID = LoadText.getEditor().getText();
            ScenarioID = Board.ScenarioID;
/*
            cleanup();
            try {
                restart();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        });
        //define click button
        MenuItem Click = new MenuItem("Click");
        Click.setOnAction(eClick -> {
            FlagStart = false;
            cleanup();
            try {
                restart();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //define exit button
        MenuItem Exit = new MenuItem("Exit");
        Exit.setOnAction(eExit -> System.exit(0));

        Application.getItems().addAll(Start,Load,Click,Exit);

        Menu Details = new Menu("Details");
        //define enemy ships button
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
        //define player's shots button
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
        //define enemy shots button
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
        //-> end

        //Create Content for the ship Cruiser - Start
        FileInputStream inputstreamShip;
        Label Cruiser = new Label("Cruiser");
        URL urlCruiser = getClass().getResource("Images/cruiser.png");
        try {
            inputstreamShip = new FileInputStream(urlCruiser.getPath());//("/home/thodoris/BattlesShipTest/src/sample/cruiser.png");
            Image imgCruiser = new Image(inputstreamShip);
            ImageView imgCruiserView = new ImageView(imgCruiser);
            imgCruiserView.setFitHeight(25);
            imgCruiserView.setFitWidth(25);
            Cruiser.setGraphic(imgCruiserView);
            Cruiser.setTextFill(Color.DARKRED);
        }catch (Exception e){
            Cruiser.setTextFill(Color.BROWN);
        }
        Cruiser.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        //-> end

        //Create Content for the ship Carrier - Start
        Label Carrier = new Label("Carrier");
        URL urlCarrier = getClass().getResource("Images/carrier.png");
        try {
            inputstreamShip = new FileInputStream(urlCarrier.getPath());//("/home/thodoris/BattlesShipTest/src/sample/carrier.png");
            Image imgCarrier = new Image(inputstreamShip);
            ImageView imgCarrierView = new ImageView(imgCarrier);
            imgCarrierView.setFitHeight(25);
            imgCarrierView.setFitWidth(25);
            Carrier.setGraphic(imgCarrierView);
            Carrier.setTextFill(Color.DARKRED);
        }catch (Exception e){
            Carrier.setTextFill(Color.YELLOW);
        }
        Carrier.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        //-> end

        //Create Content for the ship Battleship - Start
        Label Battleship = new Label("Battleship");
        URL urlBattleship = getClass().getResource("Images/batteship.png");
        try {
            inputstreamShip = new FileInputStream(urlBattleship.getPath());//("/home/thodoris/BattlesShipTest/src/sample/batteship.png");
            Image imgBattleship = new Image(inputstreamShip);
            ImageView imgBattleshipView = new ImageView(imgBattleship);
            imgBattleshipView.setFitHeight(25);
            imgBattleshipView.setFitWidth(25);
            Battleship.setGraphic(imgBattleshipView);
            Battleship.setTextFill(Color.DARKRED);
        }catch (Exception e){
            Battleship.setTextFill(Color.DARKGREEN);
        }
        Battleship.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        //-> end

        //Create Content for the ship Submarine - Start
        Label Submarine = new Label("Submarine");
        URL urlSubmarine = getClass().getResource("Images/submarine.png");
        try {
            inputstreamShip = new FileInputStream(urlSubmarine.getPath());//("/home/thodoris/BattlesShipTest/src/sample/submarine.png");
            Image imgSubmarine = new Image(inputstreamShip);
            ImageView imgSubmarineView = new ImageView(imgSubmarine);
            imgSubmarineView.setFitHeight(25);
            imgSubmarineView.setFitWidth(25);
            Submarine.setGraphic(imgSubmarineView);
            Submarine.setTextFill(Color.DARKRED);
        }catch (Exception e){
            Submarine.setTextFill(Color.BLUE);
        }
        Submarine.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        //-> end

        //Create Content for the ship Destroyer - Start
        Label Destroyer = new Label("Destroyer");
        URL urlDestroyer = getClass().getResource("Images/destroyer.png");
        try {
            inputstreamShip = new FileInputStream(urlDestroyer.getPath());//("/home/thodoris/BattlesShipTest/src/sample/destroyer.png");
            Image imgDestroyer = new Image(inputstreamShip);
            ImageView imgDestroyerView = new ImageView(imgDestroyer);
            imgDestroyerView.setFitHeight(25);
            imgDestroyerView.setFitWidth(25);
            Destroyer.setGraphic(imgDestroyerView);
            Destroyer.setTextFill(Color.DARKRED);
        }catch (Exception e){
            Destroyer.setTextFill(Color.GRAY);
        }
        Destroyer.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        //put the ships labels
        VBox ShipIcons = new VBox(BattleshipMenu,Carrier,Battleship,Cruiser,Submarine,Destroyer);
        ShipIcons.setAlignment(Pos.CENTER_LEFT);
        ShipIcons.setSpacing(10);
        root.setRight(ShipIcons);

        //create enemy board
        enemyBoard = new Board(true,event -> {
            Cell cell = (Cell) event.getSource();
            PositionX.setText(String.valueOf(cell.x));
            PositionY.setText(String.valueOf(cell.y));
        });

        //create player board
        if(FlagStart) {
            playerBoard = new Board(false);
        }else{
            playerBoard = new Board(false, event -> {
                if(running){
                    return;
                }
                System.out.println("player's board created");
                Cell cell = (Cell) event.getSource();
                try {
                    if (shipsToPlace >= 0 && playerBoard.placeShip(new Ship(ships[shipsToPlace], event.getButton() == MouseButton.PRIMARY,scores[shipsToPlace],sinks[shipsToPlace],Names[shipsToPlace]), cell.x, cell.y)) {
                        shipsToPlace--;
                        if (shipsToPlace < 0) {
                            try {
                                System.out.println("Start game" + enemyTurn);
                                startGame();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }

        if(flag){
            StartPlayer();
        }


        Label enemyLabel = new Label("Enemy Board");
        enemyLabel.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, 24));
        enemyLabel.setTextFill(Color.DARKRED);

        Label playerLabel = new Label("Player's Board");
        playerLabel.setFont(Font.font("Abyssinica SIL", FontWeight.BOLD, 24));
        playerLabel.setTextFill(Color.DARKRED);


        VBox vbox1 = new VBox(50,enemyLabel, enemyBoard);
        vbox1.setAlignment(Pos.CENTER);

        VBox vbox2 = new VBox(50,playerLabel, playerBoard);
        vbox2.setAlignment(Pos.CENTER);

        root.setLeft(vbox2);
        root.setCenter(vbox1);
        return root;
    }

    /**
     * This funtion implements the enemy move. The enemy behaves like ahuman player.
     * @throws InterruptedException
     */
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
            if(x<0 || x>9 || y<0 || y>9){
                continue;
            }
            Cell cell = playerBoard.getCell(x, y);

            if (cell.wasShot)
                continue;
           try {
                enemyTurn = cell.shoot(false);
                if(enemyTurn){
                    if(previus==null){
                        previus = new Point2D(x,y);
                        NextMove.add(new Point2D(x, y + 1));
                        NextMove.add(new Point2D(x, y - 1));
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

    /**
     * this function puts the enemy ships in the enemy's board according to the scenaruio
     * @throws FileNotFoundException
     */
    private void startGame() throws FileNotFoundException, InterruptedException {
        // place enemy ships
        int type = 4;
        if(!FlagStart){
            while (type >= 0) {
                int x = random.nextInt(10);
                int y = random.nextInt(10);

                if (enemyBoard.placeEnemyShip(new Ship(ships[type], Math.random() < 0.5,scores[type],sinks[type],Names[type]), x, y)) {
                    type--;
                }
            }
            Alert alert;
            if(enemyTurn){
                alert = new Alert(Alert.AlertType.WARNING, "Enemy starts first!!!\n ");
            }
            else{
                alert = new Alert(Alert.AlertType.WARNING, "You start!!!\n Choose where to shoot!!!");
            }
            alert.setHeaderText(null);
            alert.showAndWait();
            running = true;
            enemyMove();
        }
        else if(ScenarioID == null){
            Alert alert = new Alert(Alert.AlertType.WARNING,"First Load A Scenario Then Start A Game!");
            alert.setHeaderText("Scenario Not Provided");
            alert.showAndWait();
            System.out.println("Error");
        }
        else {
            try {
                URL url = getClass().getResource("Medialab/enemy_"+ScenarioID+".txt");
                Scenario = new File(url.getPath());//("/home/thodoris/BattlesShipTest/src/sample/Scenarios/enemy_"+ScenarioID+".txt");
                Scanner reader = new Scanner(Scenario);
                while (reader.hasNext()) {
                    String data = reader.next();
                    int index = Character.getNumericValue(data.charAt(0)) - 1;
                    int CorY = Character.getNumericValue(data.charAt(2));
                    int CorX = Character.getNumericValue(data.charAt(4));
                    int vert = Character.getNumericValue(data.charAt(6));
                    boolean flag = (vert == 2);
                    if(CorX < 0 || CorX > 9 || CorY < 0 || CorY >9 ){
                        FlagStart = false;
                        Alert alert = new Alert(Alert.AlertType.WARNING,"Ship out of order!!!\n Please load another Scenario");
                        alert.setHeaderText("Ship out of border");
                        alert.showAndWait();
                        FlagStart = false;
                        primaryStage.close();
                        cleanup1();
                        restart();
                        return;
                    }
                    Cell cell = enemyBoard.getCell(CorX,CorY);
                    if(enemyBoard.placeEnemyShip(new Ship(ships[index], flag,scores[index],sinks[index],Names[index]), cell.x, cell.y)){
                        type--;
                    }
                    else{
                        FlagStart = false;
                        primaryStage.close();
                        cleanup1();
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

    /**
     * Clear all the content that has been created and reset FlagStart
     */
    void cleanup1() {
        // stop animations reset model ect.
        Score = 0;
        EnemyScore = 0;
        FlagStart = false;
        Scenario = null;
        if(Board.DestroyerShip >0) {
            Board.DestroyerShip = 0;
        }
        if(Board.SubmarineShip >0) {
            Board.SubmarineShip = 0;
        }
        if(Board.CruiserShip>0) {
            Board.CruiserShip = 0;
        }
        if(Board.BattleshipShip>0) {
            Board.BattleshipShip = 0;
        }
        if(Board.CarrierShip>0) {
            Board.CarrierShip = 0;
        }
        if(Board.EnemyDestroyerShip>0) {
            Board.EnemyDestroyerShip = 0;
        }
        if(Board.EnemySubmarineShip>0) {
            Board.EnemySubmarineShip = 0;
        }
        if(Board.EnemyCruiserShip>0) {
            Board.EnemyCruiserShip = 0;
        }
        if(Board.EnemyBattleshipShip>0) {
            Board.EnemyBattleshipShip = 0;
        }
        if(Board.EnemyCarrierShip>0) {
            Board.EnemyCarrierShip = 0;
        }

    }

    /**
     * clear all the content
     */
    void cleanup() {
        Score = 0;
        EnemyScore = 0;
        // stop animations reset model ect.
        if(Board.DestroyerShip >0) {
            Board.DestroyerShip = 0;
        }
        if(Board.SubmarineShip >0) {
            Board.SubmarineShip = 0;
        }
        if(Board.CruiserShip>0) {
            Board.CruiserShip = 0;
        }
        if(Board.BattleshipShip>0) {
            Board.BattleshipShip = 0;
        }
        if(Board.CarrierShip>0) {
            Board.CarrierShip = 0;
        }
        if(Board.EnemyDestroyerShip>0) {
            Board.EnemyDestroyerShip = 0;
        }
        if(Board.EnemySubmarineShip>0) {
            Board.EnemySubmarineShip = 0;
        }
        if(Board.EnemyCruiserShip>0) {
            Board.EnemyCruiserShip = 0;
        }
        if(Board.EnemyBattleshipShip>0) {
            Board.EnemyBattleshipShip = 0;
        }
        if(Board.EnemyCarrierShip>0) {
            Board.EnemyCarrierShip = 0;
        }

    }

    /**
     * Restart the scene
     * @throws Exception
     */
    void restart() throws Exception {
        cleanup();
        primaryStage.close();
        BattleshipMain app = new BattleshipMain();
        app.start(new Stage());
    }

    public Stage primaryStage;
    @Override
    /**
     * This Function start the scene and creates everything that is needed for the game
     */
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        Parent root = createContent(FlagStart);
        root.setId("pane");
        Scene scene = new Scene(root);
        primaryStage.setTitle("MediaLab Battleship");
        scene.getStylesheets().addAll(this.getClass().getResource("Styles.css").toExternalForm());
        this.primaryStage.setScene(scene);
        this.primaryStage.setResizable(true);
        this.primaryStage.show();
        enemyTurn = WhoStarts();
        if(shipsToPlace == -1 ){
            System.out.println("sssds");
            shipsToPlace = -2;
            Alert alert;
            if(enemyTurn){
                alert = new Alert(Alert.AlertType.WARNING, "Enemy starts first!!!\n ");
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

    /**
     * launch the game
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The AI that makes the computer play as a human player
     * @param x Coordinate X of the successful hit
     * @param y Coordinate Y of the successful hit
     * @param flag
     */
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

    /**
     * This function implements the player move
     * @param x Coordinate X of the hit
     * @param y Coordinate Y of the hit
     * @throws FileNotFoundException
     */
    private void playermove(String x,String y) throws FileNotFoundException {
        if (!running){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Wait for the game to start");
            alert.showAndWait();
        }
        else if (enemyTurn){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Wait for your turn!!!");
            alert.showAndWait();
        }
        else if(x == null || y == null){
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

    /**
     * Function that implements the end of the game
     */
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

