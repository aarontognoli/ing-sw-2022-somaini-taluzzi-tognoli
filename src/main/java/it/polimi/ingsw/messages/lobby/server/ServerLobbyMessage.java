package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.messages.ServerMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;

/**
 * Messages sent from the server to the client during the Lobby stage
 */
public abstract class ServerLobbyMessage extends ServerMessage {

    public abstract void updateCLI(CLIView cliLobbyView);

    // TODO: Do the same for the GUI
    // public abstract GUILobbyViewUpdate getUpdateForGUI();
}
