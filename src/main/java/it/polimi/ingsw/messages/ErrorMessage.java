package it.polimi.ingsw.messages;

public class ErrorMessage extends Message {
    private final String errorMessageString;

    public ErrorMessage(String s) {
        errorMessageString = s;
    }

    public String getErrorMessageString() {
        return errorMessageString;
    }
}
