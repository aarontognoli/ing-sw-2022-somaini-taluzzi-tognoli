package it.polimi.ingsw.messages;

public class ErrorMessage extends ServerMessage {
    private final String errorMessageString;

    public ErrorMessage(String s) {
        errorMessageString = s;
    }

    public String getErrorMessageString() {
        return errorMessageString;
    }
}
