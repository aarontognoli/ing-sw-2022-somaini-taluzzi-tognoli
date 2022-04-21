package it.polimi.ingsw.exceptions;

public class AssistantCardAlreadyPlayedException extends Exception {
    public AssistantCardAlreadyPlayedException() {
        super("Assistant Card Already Chosen by another Player");
    }
}
