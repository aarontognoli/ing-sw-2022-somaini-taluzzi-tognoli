package it.polimi.ingsw.mvc.view.lobby.CLIStringHandler;

import it.polimi.ingsw.messages.lobby.client.ClientLobbyMessage;

public interface BaseCLIStringHandler {
    ClientLobbyMessage generateMessageFromInput(String input);
}
