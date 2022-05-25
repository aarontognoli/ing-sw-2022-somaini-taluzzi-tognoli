package it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CharacterArgumentHandler;

import it.polimi.ingsw.cards.characters.JokerCharacter.JokerCharacterArgument;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.game.PlayCharacterCardMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CLICharacterCardHandler;

import java.util.List;

public class CLIJokerCharacterArgumentHandler extends CLICharacterCardHandler {

    @Override
    public ClientMessage generateMessageFromInput(CLIView cliView, String input) throws ClientSideCheckException {
        String[] words = input.split(" ");

        if (words.length != 2 && words.length != 4 && words.length != 6) {
            throw new ClientSideCheckException("Invalid number of arguments.");
        }

        List<List<Color>> arguments;
        try {
            arguments = generateListsOfColors(words);
        } catch (IllegalArgumentException e) {
            throw new ClientSideCheckException("Invalid color name.");
        }
        restoreCLIView();
        return new PlayCharacterCardMessage(cardIndex, new JokerCharacterArgument(arguments.get(0), arguments.get(1)));
    }
}
