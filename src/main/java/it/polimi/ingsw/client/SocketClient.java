package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.ConnectionClosedMessage;
import it.polimi.ingsw.messages.FinishedLobbyPhaseMessage;
import it.polimi.ingsw.mvc.controller.ClientControllerBase;
import it.polimi.ingsw.notifier.Notifier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SocketClient extends Notifier<FinishedLobbyPhaseMessage> {
    final private ObjectInputStream socketIn;
    final private ObjectOutputStream socketOut;

    private boolean networkActive;

    private ClientControllerBase controller;

    public SocketClient(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        socketOut = objectOutputStream;
        socketIn = objectInputStream;

        networkActive = true;
        asyncReadObject();
    }

    public void asyncReadObject() {
        new Thread(() -> {
            while (networkActive) {
                try {
                    Object o = socketIn.readObject();
                    controller.handleObjectFromNetwork(o);
                    if (o instanceof ConnectionClosedMessage) {
                        stop();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
    }
    public void asyncSendObject(Object obj) {
        new Thread(() -> {
            synchronized (socketOut) {
                try {
                    if (networkActive) {
                        socketOut.writeObject(obj);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stop() {
        networkActive = false;
    }

    public void stopToChangePhase() {
        stop();
        notifySubscribers(new FinishedLobbyPhaseMessage());
    }

    public void setController(ClientControllerBase controller) {
        this.controller = controller;
        networkActive = true;
    }
}

