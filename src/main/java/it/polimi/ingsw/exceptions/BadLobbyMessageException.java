package it.polimi.ingsw.exceptions;

public class BadLobbyMessageException extends Exception {
    public BadLobbyMessageException(Object objectReceived) {
        super("Bad message received during lobby stage: " + objectReceived.getClass().getName());
    }
}
