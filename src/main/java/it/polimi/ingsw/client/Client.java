package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.ConnectionClosedMessage;
import it.polimi.ingsw.messages.FinishedLobbyPhaseMessage;
import it.polimi.ingsw.mvc.controller.GameClientController;
import it.polimi.ingsw.mvc.controller.LobbyClientController;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.ClientView;
import it.polimi.ingsw.notifier.Notifier;
import it.polimi.ingsw.notifier.Subscriber;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Subscriber<FinishedLobbyPhaseMessage> {
    private final String ip;
    private final int port;
    private final boolean isCLI;

    private SocketClient socketClient;

    private LobbyClientController lobbyClientController;
    private GameClientController gameClientController;

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

        socketClient = new SocketClient(socketIn, socketOut);

        lobbyClientController = new LobbyClientController(socketClient);

        socketClient.setController(lobbyClientController);

        Notifier<Model> modelNotifier = new Notifier<>();
        gameClientController = new GameClientController(socketClient, modelNotifier);

        ClientView clientView;
        if (isCLI) {
            clientView = new CLIView(lobbyClientController.getServerMessageNotifier(), modelNotifier);
        } else {
            throw new RuntimeException("GUI not implemented yet");
        }

        clientView.addSubscriber(lobbyClientController);
        clientView.addSubscriber(gameClientController);
        clientView.run();
    }

    @Override
    public void subscribeNotification(FinishedLobbyPhaseMessage newValue) {
        socketClient.setController(gameClientController);
    }
}
