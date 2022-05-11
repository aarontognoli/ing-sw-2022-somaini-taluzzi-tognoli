package it.polimi.ingsw.messages.lobby.client.lobbysetup;

import it.polimi.ingsw.messages.lobby.server.ServerLobbyMessage;
import it.polimi.ingsw.server.SocketClientConnection;

public class CreateLobbyMessage extends LobbySetupMessage {
    public CreateLobbyMessage(String s) {
        super(s);
    }

    @Override
    public ServerLobbyMessage callbackFunction(SocketClientConnection sCC) {

        return sCC.createNewLobby(this.getLobbyName());
    }
}
