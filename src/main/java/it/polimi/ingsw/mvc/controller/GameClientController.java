package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.client.SocketClient;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.game.ClientGameMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.notifier.Notifier;

public class GameClientController extends ClientControllerBase {
    final private Notifier<Model> modelNotifier;

    public GameClientController(SocketClient socketClient, Notifier<Model> modelNotifier) {
        super(socketClient);
        this.modelNotifier = modelNotifier;
    }

    /**
     * @param newValue message from the view to the controller
     * @implNote message must be a GameMessage
     */
    @Override
    public void subscribeNotification(ClientMessage newValue) {
        if (!(newValue instanceof ClientGameMessage message)) {
            return;
        }
        // Send the message received from the view to the server
        socketClient.asyncSendObject(newValue);
    }

    /**
     * @param obj object received from the network
     * @implNote obj must be a String, for error messages, or the latest Model
     */
    @Override
    public void handleObjectFromNetwork(Object obj) {
        if (obj instanceof ErrorMessage errorMessage) {
            serverMessageNotifier.notifySubscribers(errorMessage);
            return;
        }

        if (!(obj instanceof Model newModel)) {
            throw new RuntimeException("Why did the server send a non-model Object nor a String message?");
        }

        modelNotifier.notifySubscribers(newModel);
    }
}
