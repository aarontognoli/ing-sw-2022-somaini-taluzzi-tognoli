package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler.CLIGameOptionsHandler;
import it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler.CLIUsernameHandler;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

public class LobbyNameAckMessage extends ServerLobbyMessage {
    final private boolean isValid;

    public LobbyNameAckMessage(boolean isValid) {
        this.isValid = isValid;
    }

    @Override
    public void updateCLI(CLILobbyView cliLobbyView) {
        if (isValid) {
            if (cliLobbyView.isFirstPlayer()) {
                cliLobbyView.setFrontEnd("Lobby created successfully.\n");
                cliLobbyView.setCurrentQueryMessage("""
                        Enter Game Options:
                        Choose a number of players between 2 and 4.
                        Choose a game mode: 'easy' or 'expert'.
                        Choose a number between 1 and 12 which is going to be the island where Mother Nature is positioned.
                        Write: <number_of_players> <game_mode> <mother_nature_island>""");
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
                cliLobbyView.setCurrentQueryMessage("Cannot join that lobby. Please try again: ");
            }
        }
    }
}
