package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.game.GameMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.notifier.Notifier;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameClientController extends ClientControllerBase {
    final private Notifier<Model> modelNotifier;

    public GameClientController(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, Notifier<Model> modelNotifier) {
        super(objectInputStream, objectOutputStream);

        this.modelNotifier = modelNotifier;
    }

    /**
     * @param newValue message from the view to the controller
     * @implNote message must be a GameMessage
     */
    @Override
    public void subscribeNotification(ClientMessage newValue) {
        if (!(newValue instanceof GameMessage message)) {
            throw new RuntimeException("Why did the view send a bad message to the client controller?");
        }

        // Send the message received from the view to the server
        this.asyncSendObject(newValue);
    }

    /**
     * @param obj object received from the network
     * @implNote obj must be a String, for error messages, or the latest Model
     */
    @Override
    protected void handleObjectFromNetwork(Object obj) {
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
