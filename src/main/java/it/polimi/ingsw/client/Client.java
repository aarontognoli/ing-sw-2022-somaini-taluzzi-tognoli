package it.polimi.ingsw.client;

import it.polimi.ingsw.mvc.controller.ClientControllerBase;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.game.CLI.CLIGameView;
import it.polimi.ingsw.mvc.view.game.GameView;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;
import it.polimi.ingsw.mvc.view.lobby.LobbyView;

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

        ClientControllerBase clientController = new ClientControllerBase(socketIn, socketOut);

        LobbyView lobbyView;
        if (isCLI) {
            lobbyView = new CLILobbyView(clientController.getLobbyMessageNotifier());
        } else {
            throw new RuntimeException("GUI not implemented yet");
        }

        lobbyView.addSubscriber(clientController);

        lobbyView.run();

        Model firstModel = lobbyView.getFirstModel();
        clientController.setModelNotifier(firstModel);

        GameView gameView;
        if (isCLI) {
            gameView = new CLIGameView(firstModel);
        } else {
            throw new RuntimeException("GUI not implemented yet");
        }

        gameView.addSubscriber(clientController);

        ((CLIGameView)gameView).run();
    }
}

