package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.messages.lobby.LobbyMessage;

public class SetNicknameAckMessage extends LobbyMessage {
    final private boolean isUsed;


    public SetNicknameAckMessage(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public boolean getIsUsed() {
        return isUsed;
    }
}
