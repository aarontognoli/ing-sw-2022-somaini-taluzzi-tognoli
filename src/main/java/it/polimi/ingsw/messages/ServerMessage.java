package it.polimi.ingsw.messages;

import it.polimi.ingsw.mvc.view.CLI.CLIView;

/**
 * Messages sent from the server to the client
 */
public abstract class ServerMessage extends Message {
    public abstract void updateCLI(CLIView cliLobbyView);

    // TODO: Do the same for the GUI
    // public abstract GUILobbyViewUpdate getUpdateForGUI();
}
