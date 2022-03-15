package it.polimi.ingsw.pawn;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.player.Player;

public class Professor {
    private Color color;
    private Player position;

    public Professor(Color color) {
        position = null;
        this.color = color;
    }

    public void move(Player newPosition) {
        this.position = newPosition;
    }

    public Color getColor() {
        return color;
    }

    public Player getPosition() {
        //TODO Remember to make it immutable
        return position;
    }
}
