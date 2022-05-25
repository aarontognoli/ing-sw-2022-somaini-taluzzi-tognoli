package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.enums.GameMode;

import java.io.Serializable;

public record LobbyState(String name, int currentPlayersCount, int maxPlayersCount,
                         GameMode gameMode) implements Serializable {
}
