package it.polimi.ingsw.exceptions;

public class TooMuchStepsException extends Exception {
    public TooMuchStepsException(int maxSteps, int chosenSteps) {
        super("Trying to move mother nature for " + chosenSteps + " steps, maximum is " + maxSteps);
    }
}
