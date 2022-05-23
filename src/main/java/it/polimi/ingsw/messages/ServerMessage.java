package it.polimi.ingsw.messages;

import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.GUI.GUIView;

/**
 * Messages sent from the server to the client
 */
public abstract class ServerMessage extends Message {
    public abstract void updateCLI(CLIView cliLobbyView);

    public abstract void updateGUI(GUIView guiLobbyView);

}
