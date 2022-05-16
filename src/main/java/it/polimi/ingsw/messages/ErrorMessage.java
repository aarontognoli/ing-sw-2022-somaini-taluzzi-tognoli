package it.polimi.ingsw.messages;

public class ErrorMessage extends Message {
    String errorMessageString;

    public ErrorMessage(String s) {
        errorMessageString = s;
    }

    public String getErrorMessage() {
        return errorMessageString;
    }
}
