package it.polimi.ingsw.mvc.view.lobby;

import it.polimi.ingsw.messages.lobby.server.ServerLobbyMessage;
import it.polimi.ingsw.mvc.view.View;
import it.polimi.ingsw.notifier.Notifier;
import it.polimi.ingsw.notifier.Subscriber;

/**
 * Base View for the Lobby views, sends message to the ClientLobbyController
 * and receives from the network ServerLobbyMessages
 */
public abstract class LobbyView extends View implements Subscriber<ServerLobbyMessage> {

    public LobbyView(Notifier<ServerLobbyMessage> modelNotifier) {
        modelNotifier.addSubscriber(this);
    }

    public abstract void run();

    /**
     * Notification sent by the model every time it updates
     *
     * @param newMessage newValue of the model
     */
    @Override
    public abstract void subscribeNotification(ServerLobbyMessage newMessage);
}
