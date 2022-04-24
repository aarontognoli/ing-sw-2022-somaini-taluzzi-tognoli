package it.polimi.ingsw.mvc.view;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.notifier.Notifier;
import it.polimi.ingsw.notifier.Subscriber;

/**
 * The RemoteView class gets updates from the model, as the base View, and
 * redirects them to the clients via the network.
 * It also receives messages from the clients via the network, and notifies them
 * (The controller has subscribed to the RemoveView, so it is the one that at the
 * end receives this messages)
 */
public class RemoteView extends View {

    private final Notifier<Message> messageNotifier;

    public RemoteView(Notifier<Model> modelNotifier) {
        super(modelNotifier);

        messageNotifier = new Notifier<>();

        // TODO: subscribe MessageReceiver to ClientConnection, which should be
        // passed as argument to the constructor
        // clientConnection.addSubscriber(new MessageReceiver())
    }

    /**
     * @param controller the controller class, that subscribes for messages
     */
    public void subscribeForMessage(Subscriber<Message> controller) {
        messageNotifier.addSubscriber(controller);
    }

    private void redirectMessageToController(Message message) {
        messageNotifier.notifySubscribers(message);
    }

    @Override
    public void subscribeNotification(Model newValue) {
        super.subscribeNotification(newValue);

        // TODO: Using client connection, send the new value to the client
    }

    private class MessageReceiver implements Subscriber<Message> {
        @Override
        public void subscribeNotification(Message newValue) {
            redirectMessageToController(newValue);
        }
    }
}
