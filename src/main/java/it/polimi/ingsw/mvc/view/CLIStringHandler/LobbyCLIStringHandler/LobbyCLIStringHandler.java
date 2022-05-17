package it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler;

import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.lobby.client.ClientLobbyMessage;
import it.polimi.ingsw.mvc.view.CLIStringHandler.BaseCLIStringHandler;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

public interface LobbyCLIStringHandler extends BaseCLIStringHandler {
    ClientLobbyMessage generateMessageFromInput(CLILobbyView cliLobbyView, String input) throws ClientSideCheckException;
}
