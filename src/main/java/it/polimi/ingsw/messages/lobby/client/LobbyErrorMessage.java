package it.polimi.ingsw.messages.lobby.client;

import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

public class LobbyErrorMessage extends ClientLobbyMessage{
    private final CLILobbyView lobbyView;
    private final String wrongElement;

    public LobbyErrorMessage(CLILobbyView lobbyView, String wrongElement) {

        this.lobbyView = lobbyView;
        this.wrongElement = wrongElement;
    }

    public void handleErrorLocally() {
        lobbyView.handleError(wrongElement);
    }

}
