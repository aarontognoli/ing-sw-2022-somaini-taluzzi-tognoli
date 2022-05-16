package it.polimi.ingsw.mvc.view.game;

import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.notifier.Notifier;

public abstract class ClientGameView extends GameView {
    public ClientGameView(Notifier<Model> modelNotifier) {
        super(modelNotifier);
    }

    public abstract void run();
}
