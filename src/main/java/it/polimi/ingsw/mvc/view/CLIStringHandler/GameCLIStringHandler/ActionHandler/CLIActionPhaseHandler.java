package it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.ActionHandler;

import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CLICharacterCardHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.GameCLIStringHandler;

public abstract class CLIActionPhaseHandler implements GameCLIStringHandler {
    protected void handleCharacterCardMessage(CLIView cliView, String input) throws ClientSideCheckException {
        if (cliView.getGameMode().equals(GameMode.EXPERT_MODE) && input.equals("character")) {
            cliView.setCliStringHandler(new CLICharacterCardHandler());
            cliView.setCurrentQueryMessage("TODO: Choose a character card message");
            throw new ClientSideCheckException("");
        }
    }
}
