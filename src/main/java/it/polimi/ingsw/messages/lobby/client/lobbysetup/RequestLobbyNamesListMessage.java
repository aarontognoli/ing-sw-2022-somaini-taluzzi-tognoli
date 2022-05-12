package it.polimi.ingsw.messages.lobby.client.lobbysetup;

import it.polimi.ingsw.messages.lobby.server.ServerLobbyMessage;
import it.polimi.ingsw.server.SocketClientConnection;

public class RequestLobbyNamesListMessage extends LobbyManagementMessage {

    @Override
    public ServerLobbyMessage callbackFunction(SocketClientConnection sCC) {

        return sCC.generateLobbyNamesList();
    }
}
