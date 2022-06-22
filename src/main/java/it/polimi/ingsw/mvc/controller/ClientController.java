package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.ServerMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.GUI.GUIView;
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

    private boolean isActive = true;

    public ClientController(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, Notifier<Model> modelNotifier) {
        socketOut = objectOutputStream;
        socketIn = objectInputStream;

        this.serverMessageNotifier = new Notifier<>();
        this.modelNotifier = modelNotifier;

        asyncReadObject();
    }

    /**
     * Reads an object asynchronously from the Object Input Stream, while the client
     * is active
     *
     * For this purpose it creates a dedicated thread
     */
    public void asyncReadObject() {
        new Thread(() -> {
            while (isActive) {
                try {
                    Object o = socketIn.readObject();
                    handleObjectFromNetwork(o);
                } catch (IOException e) {
                    // for debug e.printStackTrace();
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace(); //for debug
                }
            }
            try {
                socketIn.close();
                socketOut.close();
            } catch (IOException ignored) {
            }
        }).start();
    }

    /**
     * Sends an object asynchronously to the server through the Object Output Stream
     * For this purpose it creates a dedicated thread
     *
     * @param obj object to send
     */
    public void asyncSendObject(Object obj) {
        new Thread(() -> {
            synchronized (socketOut) {
                try {
                    socketOut.reset();
                    socketOut.writeObject(obj);
                    socketOut.flush();
                } catch (IOException e) {
                    e.printStackTrace(); //for debug
                    if (GUIView.thisGUI != null) {
                        GUIView.thisGUI.closeApp("Connection lost from server.");
                    }
                }
            }
        }).start();
    }

    /**
     * Handle an object received from the network notifying its subscribers
     * The object is handled if it is a Server Message or if it is a Model
     *
     * @param obj an object received from the network.
     */
    public void handleObjectFromNetwork(Object obj) {
        if (obj instanceof ServerMessage message) {
            serverMessageNotifier.notifySubscribers(message);
        } else if (obj instanceof Model newModel) {
            if (newModel.publicModel.getWinner() != null) {
                isActive = false;
            }
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