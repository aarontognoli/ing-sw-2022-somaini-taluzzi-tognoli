package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyViewUpdate;

public class GameStartMessage extends ServerLobbyMessage {
    final private Model firstModel;

    public GameStartMessage(Model firstModel) {
        this.firstModel = firstModel;
    }

    public Model getFirstModel() {
        return firstModel;
    }

    @Override
    public CLILobbyViewUpdate getUpdateForCLI() {
        throw new RuntimeException("Not Implemented yet");
    }
}
