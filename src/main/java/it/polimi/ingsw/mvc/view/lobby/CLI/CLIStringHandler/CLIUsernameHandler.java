package it.polimi.ingsw.mvc.view.lobby.CLI.CLIStringHandler;

import it.polimi.ingsw.messages.lobby.client.ClientLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.SetNicknameMessage;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

public class CLIUsernameHandler implements BaseCLIStringHandler {

    @Override
    public ClientLobbyMessage generateMessageFromInput(CLILobbyView cliLobbyView, String input) {
        return new SetNicknameMessage(input);
    }
}
