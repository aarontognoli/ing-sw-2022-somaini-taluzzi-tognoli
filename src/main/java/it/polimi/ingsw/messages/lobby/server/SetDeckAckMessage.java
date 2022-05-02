package it.polimi.ingsw.messages.lobby.server;

public class SetDeckAckMessage extends ServerLobbyMessage {
    private final boolean isDeckValid;
    private final boolean isFirstPlayer;

    public SetDeckAckMessage(boolean isDeckValid, boolean isFirstPlayer) {
        this.isDeckValid = isDeckValid;
        this.isFirstPlayer = isFirstPlayer;
    }

    public boolean getIsFirstPlayer() {
        return isFirstPlayer;
    }

    public boolean getIsDeckValid() {
        return isDeckValid;
    }
}
