package it.polimi.ingsw.mvc.view;

import it.polimi.ingsw.messages.ServerMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.notifier.Notifier;
import it.polimi.ingsw.notifier.Subscriber;

public abstract class ClientView extends View implements Subscriber<ServerMessage> {
    protected Model model;

    class ModelSubscriber implements Subscriber<Model>{

        ModelSubscriber(Notifier<Model> modelNotifier) {
            modelNotifier.addSubscriber(this);}

        /**
         * Notification sent by the model every time it updates
         *
         * @param newValue newValue of the model
         */
        @Override
        public void subscribeNotification(Model newValue) {
            model = newValue;
        }
    }

    public ClientView(Notifier<ServerMessage> messageNotifier, Notifier<Model> modelNotifier) {
        messageNotifier.addSubscriber(this);
        modelNotifier.addSubscriber(new ModelSubscriber(modelNotifier));
    }

    public abstract void run() throws InterruptedException;

    /**
     * Notification sent by the model every time it updates
     *
     * @param newMessage newValue of the model
     */
    @Override
    public abstract void subscribeNotification(ServerMessage newMessage);
}
