package it.polimi.ingsw.mvc.view;

import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.notifier.Notifier;

public class CLIView extends View {

    public CLIView(Notifier<Model> modelNotifier) {
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
