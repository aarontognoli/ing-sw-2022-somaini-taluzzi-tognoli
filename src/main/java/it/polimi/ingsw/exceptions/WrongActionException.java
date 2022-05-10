package it.polimi.ingsw.exceptions;


public class WrongActionException extends Exception {
    public static final String MESSAGE_PREFIX = "Forbidden move, ";

    public WrongActionException(String s) {
        super(MESSAGE_PREFIX + s);
    }
}
