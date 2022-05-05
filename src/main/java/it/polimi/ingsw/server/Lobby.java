package it.polimi.ingsw.server;

import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.messages.lobby.client.SetGameOptionsMessage;

public class Lobby {

    private boolean gameOptionsChosen;
    private int playersCount;
    private int motherNatureStartPosition;
    private GameMode gameMode;

    public Lobby() {
        gameOptionsChosen = false;
        playersCount = -1;
        motherNatureStartPosition = -1;
        gameMode = null;
    }

    public void setGameOptions(SetGameOptionsMessage message) {
        gameOptionsChosen = true;
        playersCount = message.getPlayerCount();
        motherNatureStartPosition = message.getMotherNatureIslandIndex();
        gameMode = message.getGameMode();
    }

    public boolean getGameOptionsChosen() {
        return gameOptionsChosen;
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public int getMotherNatureStartPosition() {
        return motherNatureStartPosition;
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}
