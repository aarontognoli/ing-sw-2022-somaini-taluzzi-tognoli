package it.polimi.ingsw.mvc.view.game.CLI;

import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CLIPlayAssistantHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.GameCLIStringHandler;
import it.polimi.ingsw.mvc.view.game.GameView;
import it.polimi.ingsw.notifier.Notifier;

public class CLIGameView extends GameView {
    private String frontEnd;
    private String currentQueryMessage;
    private GameCLIStringHandler cliStringHandler;

    private Thread readInputThread;

    // True if we created a new lobby, or at least we tried
    private boolean isFirstPlayer;

    public CLIGameView(Notifier<Model> modelNotifier) {
        super(modelNotifier);
    }

    @Override
    public synchronized void subscribeNotification(Model newValue) {
        super.subscribeNotification(newValue);
    }

    private synchronized void updateScreen() {
        // TODO: Draw on screen the updated model
        System.out.println(frontEnd);
        System.out.println(currentQueryMessage);
        System.out.println(model);
    }

    public void run() {
        frontEnd = "Loading first model...";
        currentQueryMessage = "";
        updateScreen();

        frontEnd = "";
        cliStringHandler = new CLIPlayAssistantHandler();

        //notifySubscribers(new RequestLobbyNamesListMessage());

        //asyncReadStdin();
    }
}
