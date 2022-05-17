package it.polimi.ingsw.mvc.controller;

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

    public ClientControllerBase(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        socketOut = objectOutputStream;
        socketIn = objectInputStream;
        networkActive = true;

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
