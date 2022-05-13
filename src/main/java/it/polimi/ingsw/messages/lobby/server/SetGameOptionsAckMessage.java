package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyViewUpdate;

public class SetGameOptionsAckMessage extends ServerLobbyMessage {
    final private boolean isValidOptions;

    public SetGameOptionsAckMessage(boolean isValidOptions) {
        this.isValidOptions = isValidOptions;
    }

    public boolean getIsValidOptions() {
        return isValidOptions;
    }

    @Override
    public CLILobbyViewUpdate getUpdateForCLI() {
        throw new RuntimeException("Not Implemented yet");
    }
}
