package it.polimi.ingsw.messages.lobby.server;

public class SetGameOptionsAckMessage extends ServerLobbyMessage {
    final private boolean isValidOptions;

    public SetGameOptionsAckMessage(boolean isValidOptions) {
        this.isValidOptions = isValidOptions;
    }

    public boolean getIsValidOptions() {
        return isValidOptions;
    }
}
