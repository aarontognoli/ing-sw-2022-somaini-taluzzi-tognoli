package it.polimi.ingsw.exceptions;

public class CloudEmptyException extends Exception {
    public CloudEmptyException() {
        super("Cloud is Empty");
    }
}
