package sample;

import javafx.scene.Parent;

public class Ship extends Parent {
    public int type;
    public boolean vertical;
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

    }

    public void hit() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean isHited(){
        return !(health == type);
    }
}