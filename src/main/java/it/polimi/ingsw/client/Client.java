package it.polimi.ingsw.client;

import it.polimi.ingsw.mvc.controller.GameClientController;
import it.polimi.ingsw.mvc.controller.LobbyClientController;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.game.CLI.CLIGameView;
import it.polimi.ingsw.mvc.view.game.ClientGameView;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;
import it.polimi.ingsw.mvc.view.lobby.LobbyView;
import it.polimi.ingsw.notifier.Notifier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private final String ip;
    private final int port;
    private final boolean isCLI;

    public Client(String ip, int port, boolean isCLI) {
        this.ip = ip;
        this.port = port;
        this.isCLI = isCLI;
    }

    public void run() throws IOException, InterruptedException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");

        ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());

        LobbyClientController lobbyClientController = new LobbyClientController(socketIn, socketOut);

        LobbyView lobbyView;
        if (isCLI) {
            lobbyView = new CLILobbyView(lobbyClientController.getLobbyMessageNotifier());
        } else {
            throw new RuntimeException("GUI not implemented yet");
        }

        lobbyView.addSubscriber(lobbyClientController);

        lobbyView.run();

        lobbyClientController.stopObjectRead();

        Notifier<Model> modelNotifier = new Notifier<>();
        GameClientController gameClientController = new GameClientController(socketIn, socketOut, modelNotifier);

        ClientGameView gameView;
        if (isCLI) {
            gameView = new CLIGameView(modelNotifier);
        } else {
            throw new RuntimeException("GUI not implemented yet");
        }

        gameView.addSubscriber(gameClientController);

        modelNotifier.notifySubscribers(lobbyView.getFirstModel());

        gameView.run();

        gameClientController.stopObjectRead();
    }
}

