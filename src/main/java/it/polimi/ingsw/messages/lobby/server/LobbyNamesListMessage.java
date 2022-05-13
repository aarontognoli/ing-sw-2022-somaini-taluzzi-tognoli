package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.server.Lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LobbyNamesListMessage extends ServerLobbyMessage {

    final private List<LobbyState> lobbies;

    public LobbyNamesListMessage(Map<String, Lobby> lobbyMap) {
        lobbies = new ArrayList<>();
        for (Map.Entry<String, Lobby> entry : lobbyMap.entrySet()) {
            String lobbyName = entry.getKey();
            Lobby lobby = entry.getValue();

            lobbies.add(new LobbyState(lobbyName, lobby.getCurrentPlayersCount(), lobby.getMaxPlayersCount()));
        }
    }

    public List<LobbyState> getLobbies() {
        return lobbies;
    }
}
