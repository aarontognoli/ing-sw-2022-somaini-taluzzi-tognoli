package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

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
    public void updateCLI(CLILobbyView cliLobbyView) {
        if (!getIsDeckValid()) {
            cliLobbyView.setFrontEnd("This deck is already in use.");
        } else {
            cliLobbyView.setFrontEnd("Perfect!");
            cliLobbyView.setCurrentQueryMessage("Now wait for other players to join the lobby!");
            //TODO pass the control to GameClientController and create CLIGameView
            //if first player, make him do the first move
        }
    }
}
