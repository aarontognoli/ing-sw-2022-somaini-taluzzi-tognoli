package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.messages.lobby.LobbyMessage;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;

/**
 * Messages sent from the server to the client during the Lobby stage
 */
public abstract class ServerLobbyMessage extends LobbyMessage {

    public abstract void updateCLI(CLILobbyView cliLobbyView);

    // TODO: Do the same for the GUI
    // public abstract GUILobbyViewUpdate getUpdateForGUI();
}
