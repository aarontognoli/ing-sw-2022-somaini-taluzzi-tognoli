package it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler;

import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.lobby.client.ClientLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.SetNicknameMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;

public class CLIUsernameHandler implements LobbyCLIStringHandler {

    @Override
    public ClientMessage generateMessageFromInput(CLIView cliLobbyView, String input) {
        return new SetNicknameMessage(input);
    }
}
