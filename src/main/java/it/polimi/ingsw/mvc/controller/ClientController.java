package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.ServerMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.notifier.Notifier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The client controller receives message from the client View (Be it CLI or
 * GUI)
 * and then forwards the message to the server
 */
public class ClientController extends Controller {
    final private ObjectInputStream socketIn;
    final private ObjectOutputStream socketOut;

    protected final Notifier<ServerMessage> serverMessageNotifier;
    final private Notifier<Model> modelNotifier;

    public ClientController(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, Notifier<Model> modelNotifier) {
        socketOut = objectOutputStream;
        socketIn = objectInputStream;

        this.serverMessageNotifier = new Notifier<>();
        this.modelNotifier = modelNotifier;

        asyncReadObject();
    }
    public void asyncReadObject() {
        new Thread(() -> {
            while(true) {
                try {
                    Object o = socketIn.readObject();
                    handleObjectFromNetwork(o);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void asyncSendObject(Object obj) {
        new Thread(() -> {
            synchronized (socketOut) {
                try {
                    socketOut.reset();
                    socketOut.writeObject(obj);
                    socketOut.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void handleObjectFromNetwork(Object obj) {
        if (obj instanceof ServerMessage message) {
            serverMessageNotifier.notifySubscribers(message);
        } else if (obj instanceof Model newModel) {
            modelNotifier.notifySubscribers(newModel);
        }
    }

    @Override
    public void subscribeNotification(ClientMessage newValue) {
        asyncSendObject(newValue);
    }

    public Notifier<ServerMessage> getServerMessageNotifier() {
        return serverMessageNotifier;
    }
}