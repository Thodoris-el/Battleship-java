package sample;

import javafx.scene.Parent;

public class Ship extends Parent {
    public int type; //length of the ship
    public boolean vertical; //true if the ship is puttied vertical
    public int Score; //score for a successful hit
    public int SinkBonus; //score given when ship is sunk
    public String shipType; //name of the ship
    private int health; //health of the ship -> same as length

    public Ship(int type, boolean vertical,int Score,int SinkBonus,String shipType) {
        this.type = type;
        this.vertical = vertical;
        health = type;
        this.Score = Score;
        this.SinkBonus = SinkBonus;
        this.shipType = shipType;

    }

    //when a ship is hit it loses 1 health point
    public void hit() {
        health--;
    }

    //returns true if the ship is not sunk
    public boolean isAlive() {
        return health > 0;
    }

    //returns true if the ship is hit
    public boolean isHit(){
        return !(health == type);
    }
}

