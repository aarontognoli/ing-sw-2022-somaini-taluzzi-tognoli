package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.server.Lobby;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class LobbyNamesListMessage extends ServerLobbyMessage {

    Map<String, Map.Entry<Integer, Integer>> lobbyNames;


    public LobbyNamesListMessage(Map<String, Lobby> lobbyMap) {
        lobbyNames = new HashMap<>();
        for (Map.Entry<String, Lobby> entry : lobbyMap.entrySet()) {
            Map.Entry<Integer, Integer> thisEntry = new AbstractMap.SimpleEntry<>(entry.getValue().waitingConnection.size(), entry.getValue().getPlayersCount());
            lobbyNames.put(entry.getKey(), thisEntry);
        }
    }
}
