package it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CharacterArgumentHandler;

import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.game.PlayCharacterCardMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CLICharacterCardHandler;

public class CLIFlagCharacterArgumentHandler extends CLICharacterCardHandler {

    public CLIFlagCharacterArgumentHandler(int initialCardIndex) {
        super(initialCardIndex);
    }

    @Override
    public ClientMessage generateMessageFromInput(CLIView cliView, String input) throws ClientSideCheckException {
        int targetIslandIndex;

        try {
            targetIslandIndex = Integer.parseInt(input.strip()) - 1;
        } catch (NumberFormatException e) {
            throw new ClientSideCheckException("Invalid island index.");
        }

        int islandCount = cliView.getIslandCountFromModel();

        if (targetIslandIndex < 0 || targetIslandIndex >= islandCount) {
            throw new ClientSideCheckException("Invalid Island index.");
        }

        return new PlayCharacterCardMessage(cardIndex, targetIslandIndex);
    }
}
