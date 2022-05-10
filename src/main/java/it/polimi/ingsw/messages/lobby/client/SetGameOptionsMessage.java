package it.polimi.ingsw.messages.lobby.client;

import it.polimi.ingsw.enums.GameMode;

public class SetGameOptionsMessage extends ClientLobbyMessage {
    final private int playerCount;
    final private GameMode gameMode;
    final private int motherNatureIslandIndex;

    public SetGameOptionsMessage(int playerCount, GameMode gameMode, int motherNatureIslandIndex) {
        this.playerCount = playerCount;
        this.gameMode = gameMode;
        this.motherNatureIslandIndex = motherNatureIslandIndex;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public int getMotherNatureIslandIndex() {
        return motherNatureIslandIndex;
    }
}
