package it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler;

import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.lobby.client.ClientLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.SetDeckMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;

public class CLIDeckNameHandler implements LobbyCLIStringHandler {

    @Override
    public ClientMessage generateMessageFromInput(CLIView cliLobbyView, String input) throws ClientSideCheckException {
        return switch (input) {
            case "desert king" -> new SetDeckMessage(DeckName.DESERT_KING);
            case "mountain sage" -> new SetDeckMessage(DeckName.MOUNTAIN_SAGE);
            case "cloud witch" -> new SetDeckMessage(DeckName.CLOUD_WITCH);
            case "forest mage" -> new SetDeckMessage(DeckName.FOREST_MAGE);
            default -> throw new ClientSideCheckException("Invalid deck name.");
        };
    }
}
