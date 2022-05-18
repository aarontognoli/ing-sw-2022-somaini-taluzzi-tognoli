package it.polimi.ingsw.client;

import it.polimi.ingsw.mvc.controller.GameClientController;
import it.polimi.ingsw.mvc.controller.LobbyClientController;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.ClientView;
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
        Notifier<Model> modelNotifier = new Notifier<>();
        ClientView clientView;
        if (isCLI) {
            clientView = new CLIView(lobbyClientController.getServerMessageNotifier(), modelNotifier);
        } else {
            throw new RuntimeException("GUI not implemented yet");
        }

        clientView.addSubscriber(lobbyClientController);

        clientView.run();


        //GameClientController gameClientController = new GameClientController(socketIn, socketOut, modelNotifier);

        //clientView.addSubscriber(gameClientController);

       // gameClientController.stopObjectRead();

        //socket.close();
    }
}
