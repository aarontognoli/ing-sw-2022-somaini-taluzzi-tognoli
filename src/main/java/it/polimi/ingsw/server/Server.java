package it.polimi.ingsw.server;

import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.mvc.controller.Controller;
import it.polimi.ingsw.mvc.controller.ServerController;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.mvc.view.View;
import it.polimi.ingsw.player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 12345;
    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newFixedThreadPool(128);
    private final Map<String, ClientConnection> waitingConnection = new HashMap<>();
    protected final Map<String, DeckName> nicknamesAndDecks = new HashMap<>();
    private final List<ClientConnection> playersConnections = new ArrayList<>();
    protected GameMode gameMode;
    protected int numberOfPlayers;
    protected int motherNatureStartingPosition;


    //Deregister connection
    public synchronized void closePlayersConnections() {
        // this method closes all connections and the server socket because the server
        // manages only one match at a time: multiple matches is an advanced functionality
        // that we can implement later
        for (ClientConnection players : playersConnections) {
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
    public synchronized void lobby(ClientConnection c, String nickname) {
        waitingConnection.put(nickname, c);
        if (numberOfPlayers == 2 && waitingConnection.size() == 2) {
            List<String> keys = new ArrayList<>(waitingConnection.keySet());
            ClientConnection c1 = waitingConnection.get(keys.get(0));
            ClientConnection c2 = waitingConnection.get(keys.get(1));
            Player player1 = new Player(keys.get(0), TowerColor.BLACK, nicknamesAndDecks.get(keys.get(0)), 8);
            Player player2 = new Player(keys.get(1), TowerColor.WHITE, nicknamesAndDecks.get(keys.get(1)), 8);
            Model model = new Model(motherNatureStartingPosition, nicknamesAndDecks, gameMode);
            View player1View = new RemoteView(model, keys.get(0));
            View player2View = new RemoteView(model, keys.get(1));
            Controller controller = new ServerController(model);
            model.addSubscriber(player1View);
            model.addSubscriber(player2View);
            player1View.addSubscriber(controller);
            player2View.addSubscriber(controller);
            playersConnections.add(c1);
            playersConnections.add(c2);
            waitingConnection.clear();

            c1.asyncSend(model);
            c2.asyncSend(model);

            if (model.publicModel.getCurrentPlayer().equals(player1)) {
                c1.asyncSend(gameMessage.assistantCardMessage);
                c2.asyncSend(gameMessage.waitMessage);
            } else {
                c2.asyncSend(gameMessage.assistantCardMessage);
                c1.asyncSend(gameMessage.waitMessage);
            }
        }
    }

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
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
