package it.polimi.ingsw.mvc.view.GUI;

import it.polimi.ingsw.ServerApp;
import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.exceptions.AssistantCardAlreadyPlayedException;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.mvc.controller.ClientController;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.notifier.Notifier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static it.polimi.ingsw.mvc.model.PublicModelTest.fourPlayersExpertMode;

public class TestModel {
    public static void main(String[] args) throws InterruptedException, IOException, AssistantCardAlreadyPlayedException, NotFoundException {
        LobbyFrame test = new LobbyFrame();
        Model model = fourPlayersExpertMode();
        new Thread(() -> ServerApp.main(new String[]{})).start();

        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("127.0.0.1", 12345);
            } catch (IOException e) {
                Thread.sleep(50);
            }
        }
        System.out.println("Connection established");

        ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());

        Notifier<Model> modelNotifier = new Notifier<>();

        ClientController clientController = new ClientController(socketIn, socketOut, modelNotifier);


        GUIView.thisGUI = new GUIView(clientController.getServerMessageNotifier(), modelNotifier);

        new Thread(() -> test.open()).start();

        while (LobbyFrame.lobbyFrame == null) {
            Thread.sleep(50);
        }
        LobbyFrame.lobbyFrame.loadCreateFrame();
        LobbyFrame.lobbyFrame.loadUsernameAndDeckFrame();
        LobbyFrame.lobbyFrame.showGameView();
        LobbyFrame.lobbyFrame.updateModel(model);
        Thread.sleep(1000);
        model.publicModel.playAssistant(AssistantCard.CARD_1);
        model.publicModel.endTurn();
        LobbyFrame.lobbyFrame.updateModel(model);
        Thread.sleep(1000);
        model.publicModel.playAssistant(AssistantCard.CARD_2);
        model.publicModel.endTurn();
        LobbyFrame.lobbyFrame.updateModel(model);
        Thread.sleep(1000);

        Thread.sleep(1000);
        Thread.sleep(1000);

    }
}
