package it.polimi.ingsw.messages.lobby.client.lobbysetup;

import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.messages.lobby.server.ServerLobbyMessage;
import it.polimi.ingsw.server.SocketClientConnection;

public class CreateLobbyMessage extends LobbySetupMessage {
    final private int playerCount;
    final private GameMode gameMode;
    final private int motherNatureIslandIndex;

    public CreateLobbyMessage(String s, int playerCount, GameMode gameMode, int motherNatureIslandIndex) {
        super(s);
        this.playerCount = playerCount;
        this.gameMode = gameMode;
        this.motherNatureIslandIndex = motherNatureIslandIndex;
    }


    @Override
    public ServerLobbyMessage callbackFunction(SocketClientConnection sCC) {

        return sCC.createNewLobby(this);
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
