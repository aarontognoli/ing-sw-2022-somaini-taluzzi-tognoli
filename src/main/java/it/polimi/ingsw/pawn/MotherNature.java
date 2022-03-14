package it.polimi.ingsw.pawn;

import it.polimi.ingsw.places.Island;

public class MotherNature {
    private Island position;

    public MotherNature(Island position) {
        this.position = position;
    }

    public Island getPosition() {
        return position;
    }

    public void move(Island destination) {
        this.position = destination;
    }
}
