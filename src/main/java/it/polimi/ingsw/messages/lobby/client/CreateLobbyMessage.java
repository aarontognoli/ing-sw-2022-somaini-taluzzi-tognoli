package it.polimi.ingsw.messages.lobby.client;

import it.polimi.ingsw.messages.lobby.LobbySetupMessage;

public class CreateLobbyMessage extends LobbySetupMessage {
    public CreateLobbyMessage(String s) {
        super(s);
    }

    @Override
    public void callbackFunction() {
        //todo;
    }
}
