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

        try {
            playerCount = parseInt(input.substring(0, 1));

            String gameModeInput = input.substring(2, input.lastIndexOf(" "));

            motherNatureIslandIndex = parseInt(input.substring(input.lastIndexOf(" ") + 1)) - 1;

            if (gameModeInput.equals("easy")) {
                gameMode = GameMode.EASY_MODE;
            } else if (gameModeInput.equals("difficult")) {
                gameMode = GameMode.EXPERT_MODE;
            } else {
                throw new IllegalArgumentException();
            }

        } catch (Exception e) {
            return new LobbyErrorMessage(cliLobbyView);
        }

        return new SetGameOptionsMessage(playerCount, gameMode, motherNatureIslandIndex);
    }
}
