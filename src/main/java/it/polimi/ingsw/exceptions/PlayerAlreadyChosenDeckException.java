package it.polimi.ingsw.exceptions;

public class PlayerAlreadyChosenDeckException extends Exception {
    public PlayerAlreadyChosenDeckException(String playerName) {
        super(playerName + " has already chosen a deck.");
    }
}
