package it.polimi.ingsw.messages;

import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.GUI.GUIView;

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
        cliLobbyView.setErrorFrontEnd(errorMessageString);
    }

    @Override
    public void updateGUI(GUIView guiLobbyView) {
        guiLobbyView.showError(this);
    }
}
