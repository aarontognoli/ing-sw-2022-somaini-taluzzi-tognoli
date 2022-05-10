package it.polimi.ingsw.mvc.view.game;

import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.notifier.Notifier;

public class CLIGameView extends GameView {

    public CLIGameView(Notifier<Model> modelNotifier) {
        super(modelNotifier);
    }

    @Override
    public void subscribeNotification(Model newValue) {
        super.subscribeNotification(newValue);
        updateScreen();
    }

    private void updateScreen() {
        // TODO: Draw on screen the updated model
    }
}
