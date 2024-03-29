package it.polimi.ingsw.mvc.view;

import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.game.ClientGameMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.notifier.Notifier;
import it.polimi.ingsw.server.SocketClientConnection;

/**
 * The RemoteView class gets updates from the model, as the base View, and
 * redirects them to the clients via the network.
 * It also receives messages from the clients via the network, and notifies them
 * (The controller has subscribed to the RemoveView, so it is the one that at the
 * end receives this messages)
 */
public class RemoteView extends ModelSubscriber {

    private final String username;
    private final SocketClientConnection connection;

    public RemoteView(Notifier<Model> modelNotifier, String username, SocketClientConnection connection) {
        super(modelNotifier);

        this.username = username;
        this.connection = connection;
    }

    public void sendErrorMessage(String error) {
        connection.asyncSend(new ErrorMessage(error));
    }

    /**
     * @param message message to be sent to the ServerController
     * @implNote Updated username of the message before sending it to the ServerController,
     * so that it can know who did this move
     */
    public void redirectMessageToController(ClientGameMessage message) {
        message.setUsername(username);
        message.setRemoteView(this);
        notifySubscribers(message);
    }

    @Override
    public void subscribeNotification(Model newValue) {
        super.subscribeNotification(newValue);
        connection.asyncSend(newValue);
    }
}
