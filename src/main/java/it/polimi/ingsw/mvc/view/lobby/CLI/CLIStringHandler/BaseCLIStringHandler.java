package it.polimi.ingsw.mvc.view.lobby.CLI.CLIStringHandler;

import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.lobby.client.ClientLobbyMessage;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

public interface BaseCLIStringHandler {
    ClientLobbyMessage generateMessageFromInput(CLILobbyView cliLobbyView, String input) throws ClientSideCheckException;
}
