package it.polimi.ingsw.messages.lobby.server;

import java.io.Serializable;

public record LobbyState(String name, int currentPlayersCount, int maxPlayersCount) implements Serializable {
}
