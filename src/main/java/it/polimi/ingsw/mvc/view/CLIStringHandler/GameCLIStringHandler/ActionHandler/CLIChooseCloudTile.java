package it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.ActionHandler;

import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.game.DrawStudentIntoEntranceMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;

public class CLIChooseCloudTile extends CLIActionPhaseHandler {
    @Override
    public ClientMessage generateMessageFromInput(CLIView cliView, String input) throws ClientSideCheckException {
        int cloudIndex;

        try {
            cloudIndex = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            throw new ClientSideCheckException("Invalid cloud number");
        }

        int cloudCount = cliView.getCloudsCount();
        if (cloudIndex < 0 || cloudIndex >= cloudCount) {
            throw new ClientSideCheckException("Choose a cloud number between 1 and %d".formatted(cloudCount));
        }

        return new DrawStudentIntoEntranceMessage(cloudIndex);
    }
}
