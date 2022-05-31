package it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CharacterArgumentHandler;

import it.polimi.ingsw.cards.characters.WineCharacter.WineCharacterArgument;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.game.PlayCharacterCardMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.ActionHandler.CLIMoveStudentHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CLICharacterCardHandler;


public class CLIWineCharacterArgumentHandler extends CLICharacterCardHandler {

    public CLIWineCharacterArgumentHandler(int initialCardIndex) {
        super(initialCardIndex);
    }

    @Override
    public ClientMessage generateMessageFromInput(CLIView cliView, String input) throws ClientSideCheckException {

        checkForExit(input, cliView);

        String[] words = input.split(" ");

        if (words.length != 2) {
            throw new ClientSideCheckException("Invalid number of arguments.");
        }

        String colorString = words[0];
        String islandString = words[1];

        int targetIslandIndex;
        Color targetStudentColor;

        int islandCount = cliView.getIslandCountFromModel();

        try {
            targetIslandIndex = Integer.parseInt(islandString) - 1;
            if (targetIslandIndex < 0 || targetIslandIndex >= islandCount)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new ClientSideCheckException("Invalid island index.");
        }

        targetStudentColor = CLIMoveStudentHandler.parseColorString(colorString);
        restoreCLIView(cliView);
        return new PlayCharacterCardMessage(cardIndex, new WineCharacterArgument(targetIslandIndex, targetStudentColor));
    }
}
