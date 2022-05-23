package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.GUI.GUIView;

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
        cliLobbyView.setModel(firstModel);
    }

    @Override
    public void updateGUI(GUIView guiLobbyView) {
        //todo
    }
}
