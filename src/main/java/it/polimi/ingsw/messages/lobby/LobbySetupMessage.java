package it.polimi.ingsw.messages.lobby;

public abstract class LobbySetupMessage extends LobbyManagementMessage {
    String lobbyName;

    public LobbySetupMessage(String s) {
        lobbyName = s;
    }

    public String getLobbyName() {
        return lobbyName;
    }


}
