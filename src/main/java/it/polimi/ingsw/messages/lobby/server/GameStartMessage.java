package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CLIPlayAssistantHandler;
import it.polimi.ingsw.mvc.view.CLI.CLIView;

public class GameStartMessage extends ServerLobbyMessage {
    final private Model firstModel;

    public GameStartMessage(Model firstModel) {
        this.firstModel = firstModel;
    }

    public Model getFirstModel() {
        return firstModel;
    }

    @Override
    public void updateCLI(CLIView cliLobbyView) {
        cliLobbyView.setFrontEnd("");
        cliLobbyView.setCurrentQueryMessage("Choose an assistant card by typing its upper left number: ");
        cliLobbyView.setCliStringHandler(new CLIPlayAssistantHandler());
    }
}
