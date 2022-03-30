package it.polimi.ingsw.pawn;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.player.Board;

public class Professor {
    private final Color color;
    private Board position;

    public Professor(Color color) {
        position = null;
        this.color = color;
    }

    public void move(Board newPosition) {
        this.position = newPosition;
    }

    public Color getColor() {
        return color;
    }

    public Board getPosition() {
        return position;
    }
}
