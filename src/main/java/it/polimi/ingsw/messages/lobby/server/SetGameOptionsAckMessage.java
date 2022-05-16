package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler.CLIUsernameHandler;

public class SetGameOptionsAckMessage extends ServerLobbyMessage {
    final private boolean isValidOptions;

    public SetGameOptionsAckMessage(boolean isValidOptions) {
        this.isValidOptions = isValidOptions;
    }

    public boolean getIsValidOptions() {
        return isValidOptions;
    }

    @Override
    public void updateCLI(CLILobbyView cliLobbyView) {
        if(getIsValidOptions()) {
            cliLobbyView.setFrontEnd("The setting of the game was successful.");
            cliLobbyView.setCurrentQueryMessage("Enter Username: ");
            cliLobbyView.setCliStringHandler(new CLIUsernameHandler());
        } else {
            cliLobbyView.setFrontEnd("There was an error in the setting of the game.");
        }
    }
}
