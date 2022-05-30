package it.polimi.ingsw.mvc.view.CLIStringHandler;

import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;

public class CLIEmptyHandler implements CLIStringHandler {
    private String message;

    public CLIEmptyHandler(String message) {
        this.message = message;
    }

    public CLIEmptyHandler() {
        this.message = "Not your turn!";
    }

    @Override
    public ClientMessage generateMessageFromInput(CLIView cliView, String input) throws ClientSideCheckException {
        throw new ClientSideCheckException(message);
    }
}
