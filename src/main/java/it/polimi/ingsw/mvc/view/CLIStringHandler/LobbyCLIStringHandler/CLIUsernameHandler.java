package it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler;

import it.polimi.ingsw.messages.lobby.client.ClientLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.SetNicknameMessage;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

public class CLIUsernameHandler implements LobbyCLIStringHandler {

    @Override
    public ClientLobbyMessage generateMessageFromInput(CLILobbyView cliLobbyView, String input) {
        return new SetNicknameMessage(input);
    }
}
