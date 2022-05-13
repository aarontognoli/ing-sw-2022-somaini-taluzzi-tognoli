package it.polimi.ingsw.messages.lobby.server;

public record LobbyState(String name, int currentPlayersCount, int maxPlayersCount) {
}
