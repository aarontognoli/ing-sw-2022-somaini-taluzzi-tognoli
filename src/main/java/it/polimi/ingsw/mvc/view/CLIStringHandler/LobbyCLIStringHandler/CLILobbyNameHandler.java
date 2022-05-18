package it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler;

import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.lobby.client.ClientLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.CreateLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.JoinLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.RequestLobbyNamesListMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;

import static java.lang.Integer.parseInt;

public class CLILobbyNameHandler implements LobbyCLIStringHandler {

    private ClientLobbyMessage newLobby(String input) throws ClientSideCheckException {
        String[] words = input.split(" ");

        if (words.length != 5) {
            throw new ClientSideCheckException("Invalid number of arguments");
        }

        int playerCount;
        int motherNatureIslandIndex;
        try {
            playerCount = parseInt(words[2]);
        } catch (NumberFormatException e) {
            throw new ClientSideCheckException("Player count must be an integer.");
        }

        try {
            motherNatureIslandIndex = parseInt(words[4]);
        } catch (NumberFormatException e) {
            throw new ClientSideCheckException("Mother Nature Starting Island must be an integer.");
        }


        if (playerCount < 1 || playerCount > 4) {
            throw new ClientSideCheckException("The number of player must be 2, 3 or 4. %d found.".formatted(playerCount));
        }

        if (motherNatureIslandIndex < 1 || motherNatureIslandIndex > 12) {
            throw new ClientSideCheckException("The island number must be between 1 and 12. %d found.".formatted(motherNatureIslandIndex));
        }
        motherNatureIslandIndex--;

        GameMode gameMode;
        if (words[3].equals("easy")) {
            gameMode = GameMode.EASY_MODE;
        } else if (words[3].equals("expert")) {
            gameMode = GameMode.EXPERT_MODE;
        } else {
            throw new ClientSideCheckException("Game mode must be 'easy' or 'expert'. %s found.".formatted(words[3]));
        }

        return new CreateLobbyMessage(words[1], playerCount, gameMode, motherNatureIslandIndex);
    }

    @Override
    public ClientMessage generateMessageFromInput(CLIView cliLobbyView, String input) throws ClientSideCheckException {
        if (input.startsWith("new ")) {
            // We want to create a new lobby
            return newLobby(input);
        } else if (input.trim().equals("reload")) {
            // We want to reload the lobbies
            return new RequestLobbyNamesListMessage();
        } else {
            // We want to join an existing lobby
            return new JoinLobbyMessage(input);
        }
    }
}
