package it.polimi.ingsw.messages.lobby.client;

import it.polimi.ingsw.messages.lobby.LobbyManagementMessage;

public class CreateLobbyMessage extends LobbyManagementMessage {
    public CreateLobbyMessage(String s) {
        super(s);
    }

    @Override
    public void callbackFunction() {
        //todo;
    }
}
