package it.polimi.ingsw.messages.lobby.client.lobbysetup;

import it.polimi.ingsw.messages.lobby.server.ServerLobbyMessage;
import it.polimi.ingsw.server.SocketClientConnection;

public abstract class LobbyManagementMessage {

    public abstract ServerLobbyMessage callbackFunction(SocketClientConnection sCC);
}
