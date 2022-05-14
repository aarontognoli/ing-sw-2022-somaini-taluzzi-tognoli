package it.polimi.ingsw.messages.lobby.client;

import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

public class LobbyErrorMessage extends ClientLobbyMessage{
    private final CLILobbyView lobbyView;

    public LobbyErrorMessage(CLILobbyView lobbyView) {
        this.lobbyView = lobbyView;
    }

    public void handleErrorLocally() {
        lobbyView.handleError();
    }

}
