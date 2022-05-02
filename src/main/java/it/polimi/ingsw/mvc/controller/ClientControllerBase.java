package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.notifier.Notifier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The client controller receives message from the client View (Be it CLI or GUI)
 * and then forwards the message to the server
 */
public abstract class ClientControllerBase extends Controller {
    final protected Notifier<Object> socketNotifier;
    final private ObjectInputStream socketIn;
    final private ObjectOutputStream socketOut;

    public ClientControllerBase(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        socketOut = objectOutputStream;
        socketIn = objectInputStream;

        socketNotifier = new Notifier<>();

        new Thread(() -> {
            try {
                socketNotifier.notifySubscribers(socketIn.readObject());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
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
}
