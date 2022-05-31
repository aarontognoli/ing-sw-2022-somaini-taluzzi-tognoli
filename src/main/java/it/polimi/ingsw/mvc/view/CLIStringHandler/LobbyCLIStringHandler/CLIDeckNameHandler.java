package it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler;

import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.lobby.client.SetDeckMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;

public class CLIDeckNameHandler implements LobbyCLIStringHandler {

    @Override
    public ClientMessage generateMessageFromInput(CLIView cliLobbyView, String input) throws ClientSideCheckException {
        return switch (input) {
            case "1" -> new SetDeckMessage(DeckName.DESERT_KING);
            case "2" -> new SetDeckMessage(DeckName.MOUNTAIN_SAGE);
            case "3" -> new SetDeckMessage(DeckName.CLOUD_WITCH);
            case "4" -> new SetDeckMessage(DeckName.FOREST_MAGE);
            default -> throw new ClientSideCheckException("Invalid deck number.");
        };
    }
}
