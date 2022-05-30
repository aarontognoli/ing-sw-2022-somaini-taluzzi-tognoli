package it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CharacterArgumentHandler;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.game.PlayCharacterCardMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.ActionHandler.CLIMoveStudentHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CLICharacterCardHandler;

public class CLIColorCharacterArgumentHandler extends CLICharacterCardHandler {

    public CLIColorCharacterArgumentHandler(int initialCardIndex) {
        super(initialCardIndex);
    }

    @Override
    public ClientMessage generateMessageFromInput(CLIView cliView, String input) throws ClientSideCheckException {

        Color targetColor = CLIMoveStudentHandler.parseColorString(input.strip());

        return new PlayCharacterCardMessage(cardIndex, targetColor);
    }
}
