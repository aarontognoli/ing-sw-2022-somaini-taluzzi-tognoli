package it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler;

import it.polimi.ingsw.messages.lobby.client.ClientLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.CreateLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.JoinLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.RequestLobbyNamesListMessage;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

public class CLILobbyNameHandler implements LobbyCLIStringHandler {
    @Override
    public ClientLobbyMessage generateMessageFromInput(CLILobbyView cliLobbyView, String input) {
        if (input.startsWith("new ")) {
            // We want to create a new lobby, get the name of the new lobby
            cliLobbyView.setFirstPlayer(true);
            String newLobbyName = input.substring(input.indexOf(" ") + 1);
            throw new RuntimeException("TODO: implement me");
            // return new CreateLobbyMessage(newLobbyName);
        } else if (input.trim().equals("reload")) {
            // We want to reload the lobbies
            return new RequestLobbyNamesListMessage();
        } else {
            // We want to join an existing lobby
            cliLobbyView.setFirstPlayer(false);
            return new JoinLobbyMessage(input);
        }
    }
}