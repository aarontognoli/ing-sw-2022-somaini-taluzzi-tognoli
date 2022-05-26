package it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CharacterArgumentHandler;

import it.polimi.ingsw.cards.characters.WineCharacter.WineCharacterArgument;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.game.PlayCharacterCardMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CLICharacterCardHandler;


public class CLIWineCharacterArgumentHandler extends CLICharacterCardHandler {

    @Override
    public ClientMessage generateMessageFromInput(CLIView cliView, String input) throws ClientSideCheckException {

        checkForExit(input, cliView);

        String[] words = input.split(" ");

        if (words.length != 2) {
            throw new ClientSideCheckException("Invalid number of arguments.");
        }

        int targetIslandIndex;
        Color targetStudentColor;
        try {
            targetIslandIndex = Integer.parseInt(words[1]);
            if (targetIslandIndex < 1 || targetIslandIndex > 12)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new ClientSideCheckException("Invalid island index.");
        }

        try {
            targetStudentColor = Color.valueOf(words[0]);
        } catch (IllegalArgumentException e) {
            throw new ClientSideCheckException("Invalid color name.");
        }
        restoreCLIView(cliView);
        return new PlayCharacterCardMessage(cardIndex, new WineCharacterArgument(targetIslandIndex, targetStudentColor));
    }
}
