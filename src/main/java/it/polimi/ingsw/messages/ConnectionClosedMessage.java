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
        cliLobbyView.setErrorFrontEnd(getErrorMessageString() + "\nPress enter to quit");
        cliLobbyView.setCurrentQueryMessage("");
        cliLobbyView.setCliStringHandler(new CLIEmptyHandler(""));
        cliLobbyView.setModel(null);
        cliLobbyView.setActive(false);
    }

    @Override
    public void updateGUI(GUIView guiLobbyView) {

        guiLobbyView.closeApp(getErrorMessageString());
    }
}
