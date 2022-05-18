package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.lobby.client.ClientLobbyMessage;
import it.polimi.ingsw.messages.lobby.server.GameStartMessage;
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
        if (obj instanceof ErrorMessage) {
            //TODO
        }
        if (!(obj instanceof ServerLobbyMessage message)) {
            throw new RuntimeException("Invalid message during Lobby, got " + obj.getClass().getName());
        }

        // When the GameStartMessage arrives, stop reading new objects from the network
        if (obj instanceof GameStartMessage) {
            this.stopObjectRead();
        }

        lobbyMessageNotifier.notifySubscribers(message);
    }

    @Override
    public void subscribeNotification(Message newValue) {
        if (!(newValue instanceof ClientLobbyMessage)) {
            throw new RuntimeException("Invalid message received by LobbyClientController. Why did he subscribe to this?");
        }

        asyncSendObject(newValue);
    }

    public Notifier<ServerLobbyMessage> getLobbyMessageNotifier() {
        return lobbyMessageNotifier;
    }
}
