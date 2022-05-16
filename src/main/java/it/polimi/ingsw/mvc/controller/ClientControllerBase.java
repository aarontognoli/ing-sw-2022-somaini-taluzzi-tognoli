package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.enums.ControllerPhase;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.game.GameMessage;
import it.polimi.ingsw.messages.lobby.client.ClientLobbyMessage;
import it.polimi.ingsw.messages.lobby.server.GameStartMessage;
import it.polimi.ingsw.messages.lobby.server.ServerLobbyMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.notifier.Notifier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The client controller receives message from the client View (Be it CLI or GUI)
 * and then forwards the message to the server
 */
public class ClientControllerBase extends Controller {
    final private ObjectInputStream socketIn;
    final private ObjectOutputStream socketOut;
    private ControllerPhase controllerPhase;

    private final Notifier<ServerLobbyMessage> lobbyMessageNotifier;
    private Notifier<Model> modelNotifier;

    public ClientControllerBase(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        socketOut = objectOutputStream;
        socketIn = objectInputStream;
        controllerPhase = ControllerPhase.LOBBY;

        this.lobbyMessageNotifier = new Notifier<>();


        new Thread(() -> {
            while (true) {
                try {
                    handleObjectFromNetwork(socketIn.readObject());
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
    }

    public void setModelNotifier(Notifier<Model> modelNotifier) {
        this.modelNotifier = modelNotifier;
    }

    protected void handleObjectFromNetwork(Object obj) {
        if (controllerPhase.equals(ControllerPhase.LOBBY)) {
            if (!(obj instanceof ServerLobbyMessage message)) {
                throw new RuntimeException("Invalid message during Lobby, got " + obj.getClass().getName());
            }

            if (message instanceof GameStartMessage) {
                controllerPhase = ControllerPhase.GAME;
            }

            lobbyMessageNotifier.notifySubscribers(message);
        } else {
            if (obj instanceof GameMessage msg) {
                // TODO: Send the message to the view
                return;
            }
            if (!(obj instanceof Model newModel)) {
                throw new RuntimeException("Why did the server send a non-model Object nor a String message?");
            }

            modelNotifier.notifySubscribers(newModel);
        }
    }

    public void asyncSendObject(Object obj) {
        new Thread(() -> {
            synchronized (socketOut) {
                try {
                    socketOut.writeObject(obj);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void subscribeNotification(Message newValue) {
        if (controllerPhase.equals(ControllerPhase.LOBBY)) {
            if (!(newValue instanceof ClientLobbyMessage)) {
                throw new RuntimeException("Invalid message received by LobbyClientController. Why did he subscribe to this?");
            }

        } else {
            //TODO
            if (!(newValue instanceof GameMessage message)) {
                throw new RuntimeException("Why did the view send a bad message to the client controller?");
            }
        }
        // Send the message received from the view to the server
        asyncSendObject(newValue);
    }

    public Notifier<ServerLobbyMessage> getLobbyMessageNotifier() {
        return lobbyMessageNotifier;
    }
}
