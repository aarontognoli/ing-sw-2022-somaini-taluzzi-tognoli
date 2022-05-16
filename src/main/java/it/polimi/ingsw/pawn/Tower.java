package it.polimi.ingsw.pawn;

import it.polimi.ingsw.enums.TowerColor;

import java.io.Serializable;

public class Tower implements Serializable {
    TowerColor color;

    public Tower(TowerColor newColor) {
        color = newColor;
    }

    public TowerColor getColor() {
        return color;
    }
}
