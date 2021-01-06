package sample;

import javafx.scene.Parent;

/**
 * <h1>Ship Object</h1>
 *
 * <p>The Ship Class implements the Ship for the battleship game.
 * This class has all the features neede for the game.</p>
 *
 * @author Thodoris Anagnostopoulos
 * @version 2.0
 * @since 2020-1-5
 */

public class Ship extends Parent {
    public int type; //length of the ship
    public boolean vertical; //true if the ship is puttied vertical
    public int Score; //score for a successful hit
    public int SinkBonus; //score given when ship is sunk
    public String shipType; //name of the ship
    private int health; //health of the ship -> same as length

    /**
     * This is the constructor of the ship class
     * @param type This is the length of the ship
     * @param vertical this defines if the ship is puttied vertical or horizontal
     * @param Score This defines the score for every successful hit
     * @param SinkBonus This defines the score when the ship gets sunk
     * @param shipType This defines the name of the ship
     */
    public Ship(int type, boolean vertical,int Score,int SinkBonus,String shipType) {
        this.type = type;
        this.vertical = vertical;
        health = type;
        this.Score = Score;
        this.SinkBonus = SinkBonus;
        this.shipType = shipType;

    }

    /**
     * For every successful hit the ship loses 1 hp
     */
    public void hit() {
        health--;
    }

    //returns true if the ship is not sunk

    /**
     * Return true if the ship is alive (the health of the ship is bigger than zero
     */
    public boolean isAlive() {
        return health > 0;
    }

    //returns true if the ship is hit

    /**
     * if the ship is hit it returns true
     */
    public boolean isHit(){
        return !(health == type);
    }
}

