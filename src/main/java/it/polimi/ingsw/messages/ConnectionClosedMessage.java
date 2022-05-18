package it.polimi.ingsw.messages;

import it.polimi.ingsw.mvc.view.CLI.CLIView;

public class ConnectionClosedMessage extends ErrorMessage {

    public ConnectionClosedMessage(String s) {
        super(s);
    }

    @Override
    public void updateCLI(CLIView cliLobbyView) {
        cliLobbyView.setFrontEnd(getErrorMessageString());
        cliLobbyView.setCurrentQueryMessage("");
    }
}
