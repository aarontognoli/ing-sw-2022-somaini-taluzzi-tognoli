package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.client.SocketClient;
import it.polimi.ingsw.messages.ServerMessage;
import it.polimi.ingsw.notifier.Notifier;

/**
 * The client controller receives message from the client View (Be it CLI or
 * GUI)
 * and then forwards the message to the server
 */
public abstract class ClientControllerBase extends Controller {
    protected final SocketClient socketClient;

    protected final Notifier<ServerMessage> serverMessageNotifier;

    public ClientControllerBase(SocketClient socketClient) {
        this.socketClient = socketClient;
        this.serverMessageNotifier = new Notifier<>();
    }

    public abstract void handleObjectFromNetwork(Object obj);

}
