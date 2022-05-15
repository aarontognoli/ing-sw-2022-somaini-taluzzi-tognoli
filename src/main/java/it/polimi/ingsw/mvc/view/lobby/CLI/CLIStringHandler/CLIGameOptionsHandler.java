package it.polimi.ingsw.mvc.view.lobby.CLI.CLIStringHandler;

import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.messages.lobby.client.ClientLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.LobbyErrorMessage;
import it.polimi.ingsw.messages.lobby.client.SetGameOptionsMessage;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

import static java.lang.Integer.parseInt;

public class CLIGameOptionsHandler implements BaseCLIStringHandler {
    @Override
    public ClientLobbyMessage generateMessageFromInput(CLILobbyView cliLobbyView, String input) {
        //throw new RuntimeException("Not implemented yet");
        int playerCount;
        GameMode gameMode;
        int motherNatureIslandIndex;
        input = input.trim().replaceAll(" +", " ");
        String[] words = input.split(" ");

        try {
            if (words.length != 3) {
                throw new IllegalArgumentException();
            }

            playerCount = parseInt(words[0]);
            if (playerCount < 1 || playerCount > 4) {
                throw new IllegalArgumentException();
            }

            motherNatureIslandIndex = parseInt(words[2]) - 1;
            if(motherNatureIslandIndex < 0 || motherNatureIslandIndex > 11) {
                throw new IllegalArgumentException();
            }

            if (words[1].equals("easy")) {
                gameMode = GameMode.EASY_MODE;
            } else if (words[2].equals("difficult")) {
                gameMode = GameMode.EXPERT_MODE;
            } else {
                throw new IllegalArgumentException();
            }

        } catch (Exception e) {
            return new LobbyErrorMessage(cliLobbyView, "game options");
        }

        return new SetGameOptionsMessage(playerCount, gameMode, motherNatureIslandIndex);
    }
}
