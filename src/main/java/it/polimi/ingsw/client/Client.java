package it.polimi.ingsw.client;

import it.polimi.ingsw.mvc.controller.LobbyClientController;
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

    public void run() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");

        ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());

        LobbyClientController clientController = new LobbyClientController(socketIn, socketOut);

        LobbyView lobbyView;
        if (isCLI) {
            lobbyView = new CLILobbyView(clientController.getLobbyMessageNotifier());
        } else {
            throw new RuntimeException("GUI not implemented yet");
        }

        lobbyView.addSubscriber(clientController);

        lobbyView.run();
    }
}

