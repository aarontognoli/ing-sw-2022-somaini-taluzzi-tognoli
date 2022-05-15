package it.polimi.ingsw.mvc.view.lobby.CLI.CLIStringHandler;

import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.messages.lobby.client.ClientLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.LobbyErrorMessage;
import it.polimi.ingsw.messages.lobby.client.SetDeckMessage;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

public class CLIDecknameHandler implements BaseCLIStringHandler {

    @Override
    public ClientLobbyMessage generateMessageFromInput(CLILobbyView cliLobbyView, String input) {
        input = input.trim().replaceAll(" +", " ");
        return switch (input) {
            case "desert king" -> new SetDeckMessage(DeckName.DESERT_KING);
            case "mountain sage" -> new SetDeckMessage(DeckName.MOUNTAIN_SAGE);
            case "cloud witch" -> new SetDeckMessage(DeckName.CLOUD_WITCH);
            case "forest mage" -> new SetDeckMessage(DeckName.FOREST_MAGE);
            default -> new LobbyErrorMessage(cliLobbyView);
        };
    }
}
