package it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.game.GameMessage;
import it.polimi.ingsw.messages.game.PlayAssistantMessage;
import it.polimi.ingsw.mvc.view.game.CLI.CLIGameView;

import static java.lang.Integer.parseInt;

public class CLIPlayAssistantHandler implements GameCLIStringHandler {
    @Override
    public GameMessage generateMessageFromInput(CLIGameView cliGameView, String input) throws ClientSideCheckException {
        return switch (parseInt(input)) {
            case 1 -> new PlayAssistantMessage(AssistantCard.CARD_1);
            case 2 -> new PlayAssistantMessage(AssistantCard.CARD_2);
            case 3 -> new PlayAssistantMessage(AssistantCard.CARD_3);
            case 4 -> new PlayAssistantMessage(AssistantCard.CARD_4);
            case 5 -> new PlayAssistantMessage(AssistantCard.CARD_5);
            case 6 -> new PlayAssistantMessage(AssistantCard.CARD_6);
            case 7 -> new PlayAssistantMessage(AssistantCard.CARD_7);
            case 8 -> new PlayAssistantMessage(AssistantCard.CARD_8);
            case 9 -> new PlayAssistantMessage(AssistantCard.CARD_9);
            case 10 -> new PlayAssistantMessage(AssistantCard.CARD_10);
            default -> throw new ClientSideCheckException("Invalid assistant card");
        };
    }
}
