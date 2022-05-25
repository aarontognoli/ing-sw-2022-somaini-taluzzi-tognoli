package it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.ActionHandler;

import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.game.MoveMotherNatureMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;

public class CLIMoveMotherNature extends CLIActionPhaseHandler {
    @Override
    public ClientMessage generateMessageFromInput(CLIView cliView, String input) throws ClientSideCheckException {
        int steps;

        try {
            steps = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new ClientSideCheckException("Invalid number of steps");
        }

        int maxSteps = cliView.getCurrentPlayerMaxMotherNatureMovement();

        if (steps <= 0 || steps > maxSteps) {
            throw new ClientSideCheckException("Choose a number between 1 and %d".formatted(maxSteps));
        }

        return new MoveMotherNatureMessage(steps);
    }
}
