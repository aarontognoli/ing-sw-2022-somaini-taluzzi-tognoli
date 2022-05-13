package it.polimi.ingsw.mvc.view.lobby.CLIStringHandler;

import it.polimi.ingsw.messages.lobby.client.ClientLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.CreateLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.JoinLobbyMessage;

public class CLILobbyNameHandler implements BaseCLIStringHandler {
    @Override
    public ClientLobbyMessage generateMessageFromInput(String input) {
        if (input.startsWith("new ")) {
            // We want to create a new lobby, get the name of the new lobby
            String newLobbyName = input.substring(input.indexOf(" ") + 1);
            return new CreateLobbyMessage(newLobbyName);
        } else {
            // We want to join an existing lobby
            return new JoinLobbyMessage(input);
        }
    }
}
