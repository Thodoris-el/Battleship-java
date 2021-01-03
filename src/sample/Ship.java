package sample;

import javafx.scene.Parent;

public class Ship extends Parent {
    public int type;
    public boolean vertical = true;
    public int Score;
    public int SinkBonus;
    public String shipType;

    private int health;

    public Ship(int type, boolean vertical,int Score,int SinkBonus,String shipType) {
        this.type = type;
        this.vertical = vertical;
        health = type;
        this.Score = Score;
        this.SinkBonus = SinkBonus;
        this.shipType = shipType;

        /*VBox vbox = new VBox();
        for (int i = 0; i < type; i++) {
            Rectangle square = new Rectangle(30, 30);
            square.setFill(null);
            square.setStroke(Color.BLACK);
            vbox.getChildren().add(square);
        }
        getChildren().add(vbox);*/
    }

    public void hit() {
        health--;
    }

    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean isHited(){
        return !(health == type);
    }
}