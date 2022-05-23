package it.polimi.ingsw.client;

import it.polimi.ingsw.mvc.controller.ClientController;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.ClientView;
import it.polimi.ingsw.mvc.view.GUI.GUIView;
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

        Notifier<Model> modelNotifier = new Notifier<>();

        ClientController clientController = new ClientController(socketIn, socketOut, modelNotifier);

        ClientView clientView;
        if (isCLI) {
            clientView = new CLIView(clientController.getServerMessageNotifier(), modelNotifier);
        } else {
            clientView = new GUIView(clientController.getServerMessageNotifier(), modelNotifier);
        }

        clientView.addSubscriber(clientController);
        clientView.run();
    }
}
