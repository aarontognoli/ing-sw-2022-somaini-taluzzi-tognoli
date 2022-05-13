package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyViewUpdate;

public class LobbyNameAckMessage extends ServerLobbyMessage {
    final private boolean isValid;

    public LobbyNameAckMessage(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean getIsValid() {
        return isValid;
    }

    @Override
    public CLILobbyViewUpdate getUpdateForCLI() {
        throw new RuntimeException("Not Implemented yet");
    }
}
