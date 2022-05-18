package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.view.CLI.CLIView;

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

    public boolean getIsDeckValid() {
        return isDeckValid;
    }

    @Override
    public void updateCLI(CLIView cliLobbyView) {
        if (!getIsDeckValid()) {
            cliLobbyView.setFrontEnd("This deck is already in use.");
        } else {
            cliLobbyView.setFrontEnd("Perfect!");
            cliLobbyView.setCurrentQueryMessage("Now wait for other players to join the lobby!");
        }
    }
}
