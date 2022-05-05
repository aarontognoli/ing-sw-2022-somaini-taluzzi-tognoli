package it.polimi.ingsw.server;

import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.messages.lobby.server.GameStartMessage;
import it.polimi.ingsw.mvc.controller.Controller;
import it.polimi.ingsw.mvc.controller.ServerController;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.player.Player;

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
    protected final Map<String, DeckName> nicknamesAndDecks = new HashMap<>();
    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newFixedThreadPool(128);
    private final Map<String, SocketClientConnection> waitingConnection = new HashMap<>();
    private final List<SocketClientConnection> playersConnections = new ArrayList<>();

    Lobby currentLobby;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    //Deregister connection
    public synchronized void closePlayersConnections() {
        // this method closes all connections and the server socket because the server
        // manages only one match at a time: multiple matches is an advanced functionality
        // that we can implement later
        for (SocketClientConnection players : playersConnections) {
            players.closeConnection();
        }
        playersConnections.clear();
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error when closing Server socket!");
        }
    }

    //Wait for players
    public synchronized void lobby(SocketClientConnection c, String nickname) {
        waitingConnection.put(nickname, c);

        if (waitingConnection.size() != currentLobby.getPlayersCount()) return;

        Model model = new Model(currentLobby.getMotherNatureStartPosition(),
                nicknamesAndDecks,
                currentLobby.getGameMode()
        );

        Controller controller = new ServerController(model);

        List<String> keys = new ArrayList<>(waitingConnection.keySet());
        int numberOfTowersBase = currentLobby.getPlayersCount() == 3 ? 6 : 8;

        // with this implementation the order of the players and the 2 teams (with 4 players) are casual
        for (int i = 0; i < keys.size(); i++) {
            SocketClientConnection connection = waitingConnection.get(keys.get(i));

            int numberOfTowersPlayer = numberOfTowersBase;
            if (currentLobby.getPlayersCount() == 4 && i % 2 == 1) numberOfTowersPlayer = 0;

            Player player = new Player(keys.get(i),
                    TowerColor.values()[i],
                    nicknamesAndDecks.get(keys.get(0)),
                    numberOfTowersPlayer
            );
            RemoteView playerView = new RemoteView(model, keys.get(i), connection);
            connection.setRemoteView(playerView);
            model.addSubscriber(playerView);
            playerView.addSubscriber(controller);
            playersConnections.add(connection);
            waitingConnection.clear();

            connection.asyncSend(new GameStartMessage(model));
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
}
