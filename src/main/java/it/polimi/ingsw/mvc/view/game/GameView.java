package it.polimi.ingsw.mvc.view.game;

import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.View;
import it.polimi.ingsw.notifier.Notifier;
import it.polimi.ingsw.notifier.Subscriber;

public abstract class GameView extends View implements Subscriber<Model> {

    protected Model model;

    GameView(Notifier<Model> modelNotifier) {
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
