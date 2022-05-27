package it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CharacterArgumentHandler;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.game.PlayCharacterCardMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CLICharacterCardHandler;

public class CLIFlagCharacterArgumentHandler extends CLICharacterCardHandler {

    @Override
    public ClientMessage generateMessageFromInput (CLIView cliView, String input) throws ClientSideCheckException {
        int targetIslandIndex;
        try {
            targetIslandIndex = Integer.parseInt(input.strip());
            if (targetIslandIndex < 1 || targetIslandIndex > 12)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new ClientSideCheckException("Invalid island index.");
        }

        return new PlayCharacterCardMessage(cardIndex, targetIslandIndex);
    }
}
