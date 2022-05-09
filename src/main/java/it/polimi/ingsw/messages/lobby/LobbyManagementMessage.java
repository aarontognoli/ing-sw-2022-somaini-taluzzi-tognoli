package it.polimi.ingsw.messages.lobby;

public abstract class LobbyManagementMessage {
    String lobbyName;

    public LobbyManagementMessage(String s) {
        lobbyName = s;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public abstract void callbackFunction();
}
