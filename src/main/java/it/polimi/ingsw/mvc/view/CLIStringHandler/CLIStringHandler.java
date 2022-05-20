package it.polimi.ingsw.mvc.view.CLIStringHandler;

import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.mvc.view.CLI.CLIView;

public interface CLIStringHandler {
    ClientMessage generateMessageFromInput(CLIView cliView, String input) throws ClientSideCheckException;
}
