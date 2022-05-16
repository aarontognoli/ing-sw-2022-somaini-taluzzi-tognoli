package it.polimi.ingsw.messages.lobby.server;

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

    public boolean isAreOptionsValid() {
        return areOptionsValid;
    }
}
