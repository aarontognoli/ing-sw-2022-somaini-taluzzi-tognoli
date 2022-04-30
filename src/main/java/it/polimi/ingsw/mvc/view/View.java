package it.polimi.ingsw.mvc.view;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.mvc.controller.Controller;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.notifier.Notifier;
import it.polimi.ingsw.notifier.Subscriber;

/**
 * Base Class for the View, subscribes to the model so that every time
 * the model updates, subscribeNotification is called
 */
public abstract class View extends Notifier<Message> implements Subscriber<Model> {
    protected Model model;

    View(Notifier<Model> modelNotifier) {
        modelNotifier.addSubscriber(this);
    }

    /**
     * Notification sent by the model every time it updates
     *
     * @param newValue newValue of the model
     */
    @Override
    public void subscribeNotification(Model newValue) {
        this.model = newValue;
    }
}
