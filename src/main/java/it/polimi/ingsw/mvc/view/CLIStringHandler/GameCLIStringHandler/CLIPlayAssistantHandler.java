package it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.game.PlayAssistantMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;

import static java.lang.Integer.parseInt;

public class CLIPlayAssistantHandler implements GameCLIStringHandler {
    @Override
    public ClientMessage generateMessageFromInput(CLIView cliView, String input) throws ClientSideCheckException {
        int turnOrderValue;
        try {
            turnOrderValue = parseInt(input);
        } catch (NumberFormatException e) {
            throw new ClientSideCheckException("Invalid number");
        }

        int targetIndex = turnOrderValue - 1;
        if (targetIndex < 0 || targetIndex >= AssistantCard.values().length) {
            throw new ClientSideCheckException("Invalid Assistant card number. Please chose one from 1 to 10");
        }

        return new PlayAssistantMessage(AssistantCard.values()[targetIndex]);
    }
}
