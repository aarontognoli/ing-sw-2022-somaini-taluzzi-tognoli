package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.server.Lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SendLobbyNamesListMessage {

    List<String> lobbyNames;

    public SendLobbyNamesListMessage(List<String> names) {
        lobbyNames = new ArrayList<>(names);
    }

    public SendLobbyNamesListMessage(Map<String, Lobby> lobbyMap) {
        this(lobbyMap.keySet().stream().toList());
    }
}
