package it.polimi.ingsw.exceptions;

public class ObjectIsNotMessageException extends Exception {
    public ObjectIsNotMessageException() {
        super("Received an object which is not an instance of Message");
    }
}
