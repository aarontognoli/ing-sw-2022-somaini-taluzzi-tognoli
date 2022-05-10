package it.polimi.ingsw.server;

import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.messages.lobby.client.SetGameOptionsMessage;

import java.io.IOException;

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

    synchronized public void setGameOptions(SetGameOptionsMessage message) {
        gameOptionsChosen = true;
        playersCount = message.getPlayerCount();
        motherNatureStartPosition = message.getMotherNatureIslandIndex();
        gameMode = message.getGameMode();
        this.notifyAll();
    }

    synchronized public void waitForGameOptions(Server server) throws IOException {
        while (!server.currentLobby.gameOptionsChosen) {
            try {
                server.currentLobby.wait();
            } catch (InterruptedException e) {
                throw new IOException("Interrupt received while server.currentLobby.wait()");
            }
        }
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
