package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyViewUpdate;

public class SetNicknameAckMessage extends ServerLobbyMessage {
    final private boolean isUsed;


    public SetNicknameAckMessage(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public boolean getIsUsed() {
        return isUsed;
    }

    @Override
    public CLILobbyViewUpdate getUpdateForCLI() {
        throw new RuntimeException("Not Implemented yet");
    }
}
