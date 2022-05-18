package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.messages.ServerMessage;
import it.polimi.ingsw.messages.lobby.server.ServerLobbyMessage;
import it.polimi.ingsw.notifier.Notifier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The client controller receives message from the client View (Be it CLI or
 * GUI)
 * and then forwards the message to the server
 */
public abstract class ClientControllerBase extends Controller {
    final private ObjectInputStream socketIn;
    final private ObjectOutputStream socketOut;

    private boolean networkActive;

    protected final Notifier<ServerMessage> serverMessageNotifier;

    public ClientControllerBase(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        socketOut = objectOutputStream;
        socketIn = objectInputStream;
        networkActive = true;

        this.serverMessageNotifier = new Notifier<>();

        new Thread(() -> {
            while (networkActive) {
                try {
                    Object o = socketIn.readObject();
                    handleObjectFromNetwork(o);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
    }

    protected abstract void handleObjectFromNetwork(Object obj);

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

    public void stopObjectRead() {
        networkActive = false;
    }
}
