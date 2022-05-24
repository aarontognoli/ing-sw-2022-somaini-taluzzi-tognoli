package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.GUI.GUIView;

public class SetDeckAckMessage extends ServerLobbyMessage {
    private final boolean isDeckValid;
    private final boolean isFirstPlayer;

    public SetDeckAckMessage(boolean isDeckValid, boolean isFirstPlayer) {
        this.isDeckValid = isDeckValid;
        this.isFirstPlayer = isFirstPlayer;
    }

    public boolean getIsFirstPlayer() {
        return isFirstPlayer;
    }

    public boolean isDeckValid() {
        return isDeckValid;
    }

    @Override
    public void updateCLI(CLIView cliLobbyView) {
        if (!isDeckValid()) {
            cliLobbyView.setFrontEnd("This deck is already in use.");
        } else {
            cliLobbyView.setFrontEnd("Perfect!");
            cliLobbyView.setCurrentQueryMessage("Now wait for other players to join the lobby!");
        }
    }

    @Override
    public void updateGUI(GUIView guiLobbyView) {
        if (!isDeckValid()) {
            guiLobbyView.showError(new ErrorMessage("This deck is already in use."));
        } else {
            guiLobbyView.showInfo("Perfect!", "Username and Deckname ok");
        }
    }
}
