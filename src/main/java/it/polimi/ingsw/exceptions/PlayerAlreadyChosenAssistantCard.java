package it.polimi.ingsw.exceptions;

public class PlayerAlreadyChosenAssistantCard extends Exception {
    public PlayerAlreadyChosenAssistantCard() {
        super("Assistant Card Already Chosen by another Player");
    }
}
