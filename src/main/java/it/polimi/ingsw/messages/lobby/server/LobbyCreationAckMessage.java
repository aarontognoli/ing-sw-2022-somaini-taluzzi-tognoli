package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler.CLIUsernameHandler;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

public class LobbyCreationAckMessage extends ServerLobbyMessage {
    final private boolean isNameValid;
    final private boolean areOptionsValid;

    public LobbyCreationAckMessage(boolean isNameValid, boolean areOptionsValid) {
        this.isNameValid = isNameValid;
        this.areOptionsValid = areOptionsValid;
    }

    public boolean isNameValid() {
        return isNameValid;
    }

    public boolean areOptionsValid() {
        return areOptionsValid;
    }

    @Override
    public void updateCLI(CLILobbyView cliLobbyView) {
        if (!isNameValid) {
            cliLobbyView.setFrontEnd("Invalid lobby name. Name already in use");
            return;
        }

        if (!areOptionsValid) {
            cliLobbyView.setFrontEnd("Invalid game option");
            return;
        }

        cliLobbyView.setFrontEnd("Lobby created");
        cliLobbyView.setCurrentQueryMessage("Enter your username");
        cliLobbyView.setCliStringHandler(new CLIUsernameHandler());
    }
}
