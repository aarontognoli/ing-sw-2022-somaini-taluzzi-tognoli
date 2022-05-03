package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.model.Model;

public class GameStartMessage extends ServerLobbyMessage {
    final private Model firstModel;

    public GameStartMessage(Model firstModel) {
        this.firstModel = firstModel;
    }

    public Model getFirstModel() {
        return firstModel;
    }
}
