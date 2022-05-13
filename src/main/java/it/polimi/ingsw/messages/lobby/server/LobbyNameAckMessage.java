package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLIStringHandler.CLIGameOptionsHandler;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLIStringHandler.CLIUsernameHandler;

public class LobbyNameAckMessage extends ServerLobbyMessage {
    final private boolean isValid;

    public LobbyNameAckMessage(boolean isValid) {
        this.isValid = isValid;
    }

    @Override
    public void updateCLI(CLILobbyView cliLobbyView) {
        if (isValid) {
            if (cliLobbyView.isFirstPlayer()) {
                cliLobbyView.setFrontEnd("Lobby created successfully.");
                cliLobbyView.setCurrentQueryMessage("Enter Game Options: ");
                cliLobbyView.setCliStringHandler(new CLIGameOptionsHandler());
            } else {
                cliLobbyView.setFrontEnd("Lobby joined successfully.");
                cliLobbyView.setCurrentQueryMessage("Enter Username: ");
                cliLobbyView.setCliStringHandler(new CLIUsernameHandler());
            }
        } else {
            if (cliLobbyView.isFirstPlayer()) {
                cliLobbyView.setCurrentQueryMessage("Cannot create lobby with that name. Please try again: ");
            } else {
                cliLobbyView.setCurrentQueryMessage("Cannot to join that lobby. Please try again: ");
            }
        }
    }
}
