package it.polimi.ingsw.messages;

import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.CLIEmptyHandler;
import it.polimi.ingsw.mvc.view.GUI.GUIView;

public class ConnectionClosedMessage extends ErrorMessage {

    public ConnectionClosedMessage(String s) {
        super(s);
    }

    @Override
    public void updateCLI(CLIView cliLobbyView) {
        cliLobbyView.setErrorFrontEnd(getErrorMessageString());
        cliLobbyView.setCurrentQueryMessage("");
        cliLobbyView.setCliStringHandler(new CLIEmptyHandler("Connection closed! The game ended."));
        cliLobbyView.setModel(null);
    }

    @Override
    public void updateGUI(GUIView guiLobbyView) {

        guiLobbyView.closeApp("Connection closed! The game ended.");
    }
}
