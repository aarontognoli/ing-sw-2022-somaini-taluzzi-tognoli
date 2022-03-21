package it.polimi.ingsw.cards.characters;

// Character Card Argument Exception
public class CCArgumentException extends Exception {
    public static final String INVALID_CLASS_MESSAGE = "Invalid argument class instance";

    public CCArgumentException(String message) {
        super(message);
    }
}
