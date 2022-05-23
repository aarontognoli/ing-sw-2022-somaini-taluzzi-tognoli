package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler.CLIUsernameHandler;
import it.polimi.ingsw.mvc.view.GUI.GUIView;

public class LobbyNameAckMessage extends ServerLobbyMessage {
    final private boolean isValid;

    public LobbyNameAckMessage(boolean isValid) {
        this.isValid = isValid;
    }

    //for testing
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void updateCLI(CLIView cliLobbyView) {
        if (isValid) {
            cliLobbyView.setFrontEnd("Lobby joined successfully.\n");
            cliLobbyView.setCurrentQueryMessage("Enter your username");
            cliLobbyView.setCliStringHandler(new CLIUsernameHandler());
        } else {
            cliLobbyView.setFrontEnd("Cannot join that lobby. Please try again: ");
        }
    }

    @Override
    public void updateGUI(GUIView guiLobbyView) {
        if (!isValid) {
            guiLobbyView.showError(new ErrorMessage("Cannot join that lobby. Please try again: "));
            return;
        }
        guiLobbyView.setUsernameAndDeck();
    }
}
