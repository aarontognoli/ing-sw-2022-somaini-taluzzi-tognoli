package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.enums.Color;

public class DiningRoomFullException extends Exception {
    public DiningRoomFullException(Color targetColor) {
        super("Dining room of color " + targetColor.name() + " is full");
    }
}
