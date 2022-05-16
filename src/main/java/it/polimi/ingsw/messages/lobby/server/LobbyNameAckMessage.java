package it.polimi.ingsw.messages.lobby.server;

public class LobbyNameAckMessage extends ServerLobbyMessage {
    final private boolean isValid;

    public LobbyNameAckMessage(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean getIsValid() {
        return isValid;
    }
}
