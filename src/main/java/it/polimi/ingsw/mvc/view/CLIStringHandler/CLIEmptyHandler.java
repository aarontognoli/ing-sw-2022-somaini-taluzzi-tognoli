package it.polimi.ingsw.mvc.view.CLIStringHandler;

import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;

public class CLIEmptyHandler implements CLIStringHandler {
    @Override
    public ClientMessage generateMessageFromInput(CLIView cliView, String input) throws ClientSideCheckException {
        throw new ClientSideCheckException("Not your turn");
    }
}
