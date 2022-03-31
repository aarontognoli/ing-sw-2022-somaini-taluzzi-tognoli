package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.enums.TowerColor;

public class TowerDifferentColorException extends Exception {
    public TowerDifferentColorException(TowerColor color1, TowerColor color2) {
        super("Tower operation between different colors." +
                "\nFirst Color: " + color1.toString() +
                "; Second color: " + color2.toString());
    }
}
