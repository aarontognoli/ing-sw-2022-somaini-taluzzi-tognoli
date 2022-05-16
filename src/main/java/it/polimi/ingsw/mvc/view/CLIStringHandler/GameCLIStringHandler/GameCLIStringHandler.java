package it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler;

import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.game.GameMessage;
import it.polimi.ingsw.mvc.view.CLIStringHandler.BaseCLIStringHandler;
import it.polimi.ingsw.mvc.view.game.CLI.CLIGameView;

public interface GameCLIStringHandler extends BaseCLIStringHandler {
    GameMessage generateMessageFromInput(CLIGameView cliGameView, String input) throws ClientSideCheckException;
}
