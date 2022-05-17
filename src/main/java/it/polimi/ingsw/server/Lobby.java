package it.polimi.ingsw.server;

import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.CreateLobbyMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Lobby {

    private boolean gameOptionsChosen;
    private int maxPlayersCount;
    private int motherNatureStartPosition;
    private GameMode gameMode;
    public final Map<String, SocketClientConnection> waitingConnection = new LinkedHashMap<>();
    public final List<SocketClientConnection> playersConnections = new ArrayList<>();
    public final Map<String, DeckName> nicknamesAndDecks = new LinkedHashMap<>();

    public Lobby() {
        gameOptionsChosen = false;
        maxPlayersCount = -1;
        motherNatureStartPosition = -1;
        gameMode = null;
    }

    synchronized public void setGameOptions(CreateLobbyMessage message) {
        gameOptionsChosen = true;
        maxPlayersCount = message.getPlayerCount();
        motherNatureStartPosition = message.getMotherNatureIslandIndex();
        gameMode = message.getGameMode();
        this.notifyAll();
    }

    synchronized public void waitForGameOptions(Lobby currentLobby) throws IOException {
        while (!currentLobby.gameOptionsChosen) {
            try {
                currentLobby.wait();
            } catch (InterruptedException e) {
                throw new IOException("Interrupt received while server.currentLobby.wait()");
            }
        }
    }

    public int getMaxPlayersCount() {
        return maxPlayersCount;
    }

    public int getCurrentPlayersCount() {
        return playersConnections.size();
    }

    public int getMotherNatureStartPosition() {
        return motherNatureStartPosition;
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}
