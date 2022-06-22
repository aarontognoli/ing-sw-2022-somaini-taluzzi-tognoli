package it.polimi.ingsw.pawn;

import it.polimi.ingsw.places.Island;

import java.io.Serializable;

public class MotherNature implements Serializable {
    private Island position;

    public MotherNature(Island position) {
        this.position = position;
    }

    public Island getPosition() {
        return position;
    }

    /**
     * @param destination island where we want to move mother nature
     */
    public void move(Island destination) {
        this.position = destination;
    }
}
