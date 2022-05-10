package it.polimi.ingsw.messages.lobby.server;

public class SetNicknameAckMessage extends ServerLobbyMessage {
    final private boolean isUsed;


    public SetNicknameAckMessage(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public boolean getIsUsed() {
        return isUsed;
    }
}
