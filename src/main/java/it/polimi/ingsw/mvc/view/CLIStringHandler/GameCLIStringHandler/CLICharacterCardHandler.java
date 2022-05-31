package it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler;

import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.CLIEmptyHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.ActionHandler.CLIMoveStudentHandler;

import java.util.ArrayList;
import java.util.List;

public class CLICharacterCardHandler implements GameCLIStringHandler {

    public CLICharacterCardHandler(int initialCardIndex) {
        this.cardIndex = initialCardIndex;
    }

    protected int cardIndex;

    @Override
    public ClientMessage generateMessageFromInput(CLIView cliView, String input) throws ClientSideCheckException {

        checkForExit(input, cliView);

        try {
            cardIndex = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            throw new ClientSideCheckException("Invalid number");
        }

        CharacterCard targetCard;
        try {
            targetCard = cliView.getCurrentGameCards().get(cardIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new ClientSideCheckException("Invalid Character Card number. Please chose one from 1 to 3");
        }

        return targetCard.CLIClientSideActivate(cliView, this.cardIndex);
    }

    protected void restoreCLIView(CLIView cliView) {
        cliView.setCliStringHandler(new CLIEmptyHandler());
    }

    protected void checkForExit(String input, CLIView cliView) throws ClientSideCheckException {
        if (input.trim().equals("exit")) {
            restoreCLIView(cliView);
            throw new ClientSideCheckException("");
        }
    }

    protected List<List<Color>> generateListsOfColors(String[] words) throws ClientSideCheckException {
        List<List<Color>> listsOfColors = new ArrayList<>();

        List<Color> color1 = new ArrayList<>();
        List<Color> color2 = new ArrayList<>();
        int i = 0;
        for (; i < words.length / 2; i++) {
            color1.add(CLIMoveStudentHandler.parseColorString(words[i]));
        }
        for (; i < words.length; i++) {
            color2.add(CLIMoveStudentHandler.parseColorString(words[i]));
        }
        listsOfColors.add(color1);
        listsOfColors.add(color2);
        return listsOfColors;
    }
}
