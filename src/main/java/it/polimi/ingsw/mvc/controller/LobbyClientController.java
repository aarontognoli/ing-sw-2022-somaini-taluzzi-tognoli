package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.lobby.server.ServerLobbyMessage;
import it.polimi.ingsw.notifier.Notifier;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LobbyClientController extends ClientControllerBase {

    // Notifier needed for LobbyView
    private final Notifier<ServerLobbyMessage> lobbyMessageNotifier;

    public LobbyClientController(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        super(objectInputStream, objectOutputStream);

        this.lobbyMessageNotifier = new Notifier<>();
    }

    /**
     * @param obj object received from the network
     * @implNote obj must be a ServerLobbyMessage
     */
    @Override
    protected void handleObjectFromNetwork(Object obj) {
        if (!(obj instanceof ServerLobbyMessage message)) {
            throw new RuntimeException("Invalid message during Lobby");
        }

        lobbyMessageNotifier.notifySubscribers(message);
    }

    @Override
    public void subscribeNotification(Message newValue) {

    }

    public Notifier<ServerLobbyMessage> getLobbyMessageNotifier() {
        return lobbyMessageNotifier;
    }
}
