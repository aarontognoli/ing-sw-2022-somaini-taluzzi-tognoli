package it.polimi.ingsw.messages.lobby.server;

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
        throw new RuntimeException("Not implemented yet");
    }
}
