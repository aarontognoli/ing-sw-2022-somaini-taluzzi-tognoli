package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

public class SetGameOptionsAckMessage extends ServerLobbyMessage {
    final private boolean isValidOptions;

    public SetGameOptionsAckMessage(boolean isValidOptions) {
        this.isValidOptions = isValidOptions;
    }

    public boolean getIsValidOptions() {
        return isValidOptions;
    }

    @Override
    public void updateCLI(CLILobbyView cliLobbyView) {
        throw new RuntimeException("Not Implemented yet");
    }
}
