package it.polimi.ingsw.mvc.view.lobby.CLI.CLIStringHandler;

import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.lobby.client.ClientLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.SetGameOptionsMessage;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

import static java.lang.Integer.parseInt;

public class CLIGameOptionsHandler implements BaseCLIStringHandler {
    @Override
    public ClientLobbyMessage generateMessageFromInput(CLILobbyView cliLobbyView, String input) throws ClientSideCheckException {
        String[] words = input.split(" ");

        if (words.length != 3) {
            throw new ClientSideCheckException("Message should contain 3 words. %d found.".formatted(words.length));
        }
        int playerCount;
        int motherNatureIslandIndex;
        try {
            playerCount = parseInt(words[0]);
        } catch (NumberFormatException e) {
            throw new ClientSideCheckException("Player count must be an integer.");
        }

        try {
            motherNatureIslandIndex = parseInt(words[2]);
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
        if (words[1].equals("easy")) {
            gameMode = GameMode.EASY_MODE;
        } else if (words[1].equals("expert")) {
            gameMode = GameMode.EXPERT_MODE;
        } else {
            throw new ClientSideCheckException("Game mode must be 'easy' or 'expert'. %s found.".formatted(words[1]));
        }

        return new SetGameOptionsMessage(playerCount, gameMode, motherNatureIslandIndex);
    }
}
