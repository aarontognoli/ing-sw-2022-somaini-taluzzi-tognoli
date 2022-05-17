package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

public class GameStartMessage extends ServerLobbyMessage {
    final private Model firstModel;

    public GameStartMessage(Model firstModel) {
        this.firstModel = firstModel;
    }

    public Model getFirstModel() {
        return firstModel;
    }

    @Override
    public void updateCLI(CLILobbyView cliLobbyView) {
        cliLobbyView.setFirstModel(getFirstModel());
        cliLobbyView.setFrontEnd("");
        cliLobbyView.setCurrentQueryMessage("");
        cliLobbyView.stop();
    }
}
