package it.polimi.ingsw.messages;

import it.polimi.ingsw.mvc.view.CLI.CLIView;

public class ErrorMessage extends ServerMessage {
    private final String errorMessageString;

    public ErrorMessage(String s) {
        errorMessageString = s;
    }

    public String getErrorMessageString() {
        return errorMessageString;
    }

    @Override
    public void updateCLI(CLIView cliLobbyView) {
        cliLobbyView.setFrontEnd(getErrorMessageString());
    }
}
