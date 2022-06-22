package it.polimi.ingsw.exceptions;

public class BagEmptyException extends Exception {
    public BagEmptyException() {
        super("Bag is empty");
    }
}
