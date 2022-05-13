package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

public class SetNicknameAckMessage extends ServerLobbyMessage {
    final private boolean isUsed;


    public SetNicknameAckMessage(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public boolean getIsUsed() {
        return isUsed;
    }

    @Override
    public void updateCLI(CLILobbyView cliLobbyView) {
        throw new RuntimeException("Not Implemented yet");
    }
}
