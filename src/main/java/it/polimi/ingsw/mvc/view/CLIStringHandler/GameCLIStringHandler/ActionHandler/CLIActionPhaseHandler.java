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
            cliView.setCurrentQueryMessage("""
                    Choose a character card by typing:
                    
                    '1' if you want to play the first displayed card;
                    '2' if you want to play the second one;
                    '3' if you want to play the third one.""");
            throw new ClientSideCheckException("");
        }
    }
}
