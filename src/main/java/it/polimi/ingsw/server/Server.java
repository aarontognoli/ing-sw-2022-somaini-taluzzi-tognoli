package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.lobby.server.GameStartMessage;
import it.polimi.ingsw.mvc.controller.Controller;
import it.polimi.ingsw.mvc.controller.ServerController;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.game.RemoteView;
import it.polimi.ingsw.notifier.Notifier;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final int PORT = 12345;

    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newFixedThreadPool(128);


    final Map<String, Lobby> lobbyMap = new HashMap<>();

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        System.out.println("Server listening on port " + PORT);
    }

    public String getNameFromLobby(Lobby l) {
        for (Map.Entry<String, Lobby> entry : lobbyMap.entrySet()) {
            if (entry.getValue().equals(l)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Closes all the connection for the player of whichLobby,
     * then removes the lobby from the list
     *
     * @param whichLobby target lobby
     */
    public synchronized void closePlayersConnections(Lobby whichLobby) {

        whichLobby.playersConnections.forEach(SocketClientConnection::closeConnection);

        whichLobby.playersConnections.clear();
        lobbyMap.remove(getNameFromLobby(whichLobby), whichLobby);
    }

    //Wait for players
    public synchronized void lobby(SocketClientConnection c, String nickname, Lobby currentLobby) {
        currentLobby.waitingConnection.put(nickname, c);

        if (currentLobby.waitingConnection.size() != currentLobby.getMaxPlayersCount()) return;

        Model model = new Model(currentLobby.getMotherNatureStartPosition(),
                currentLobby.nicknamesAndDecks,
                currentLobby.getGameMode()
        );
        Notifier<Model> modelNotifier = new Notifier<>();

        Controller controller = new ServerController(model);

        List<String> keys = new ArrayList<>(currentLobby.waitingConnection.keySet());

        // with this implementation the order of the players and the 2 teams (with 4 players) are casual
        for (String key : keys) {
            SocketClientConnection connection = currentLobby.waitingConnection.get(key);

            RemoteView playerView = new RemoteView(modelNotifier, key, connection);
            connection.setRemoteView(playerView);
            modelNotifier.addSubscriber(playerView);
            playerView.addSubscriber(controller);


            connection.send(new GameStartMessage(model));
        }
    }

    public void runServer() {
        while (!serverSocket.isClosed()) {
            try {
                Socket newSocket = serverSocket.accept();
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this);
                executor.submit(socketConnection);
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }

    public void closeServer() throws IOException {
        serverSocket.close();
    }
}
