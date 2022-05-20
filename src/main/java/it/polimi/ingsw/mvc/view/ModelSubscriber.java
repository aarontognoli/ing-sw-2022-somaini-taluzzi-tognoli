package it.polimi.ingsw.mvc.view;

import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.View;
import it.polimi.ingsw.notifier.Notifier;
import it.polimi.ingsw.notifier.Subscriber;

public abstract class ModelSubscriber extends View implements Subscriber<Model> {

    protected Model model;

    public ModelSubscriber(Notifier<Model> modelNotifier) {
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
