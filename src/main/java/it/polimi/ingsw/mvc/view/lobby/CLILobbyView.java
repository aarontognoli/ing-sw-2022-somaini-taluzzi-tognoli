package it.polimi.ingsw.mvc.view.lobby;

import it.polimi.ingsw.messages.lobby.server.ServerLobbyMessage;
import it.polimi.ingsw.notifier.Notifier;

/**
 * CLI client View for the lobby
 */
public class CLILobbyView extends LobbyView {
    CLILobbyView(Notifier<ServerLobbyMessage> modelNotifier) {
        super(modelNotifier);
    }

    @Override
    public void subscribeNotification(ServerLobbyMessage newMessage) {
        // TODO: Handle message and generate the view
    }
}
