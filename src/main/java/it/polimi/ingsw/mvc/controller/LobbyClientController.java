package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.client.SocketClient;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.messages.lobby.client.ClientLobbyMessage;
import it.polimi.ingsw.messages.lobby.server.GameStartMessage;
import it.polimi.ingsw.messages.lobby.server.ServerLobbyMessage;
import it.polimi.ingsw.notifier.Notifier;

public class LobbyClientController extends ClientControllerBase {

    public LobbyClientController(SocketClient socketClient) {
        super(socketClient);
    }

    /**
     * @param obj object received from the network
     * @implNote obj must be a ServerLobbyMessage
     */
    @Override
    public void handleObjectFromNetwork(Object obj) {
        if (obj instanceof ErrorMessage errorMessage) {
            serverMessageNotifier.notifySubscribers(errorMessage);
            return;
        }
        if (!(obj instanceof ServerLobbyMessage message)) {
            throw new RuntimeException("Invalid message during Lobby, got " + obj.getClass().getName());
        }
        // When the GameStartMessage arrives, stop reading new objects from the network
        if (obj instanceof GameStartMessage) {
            socketClient.stopToChangePhase();
        }
        serverMessageNotifier.notifySubscribers(message);
    }

    @Override
    public void subscribeNotification(ClientMessage newValue) {
        //If it's not a client lobby message it means that it has to be managed
        //by the game client controller
        if (!(newValue instanceof ClientLobbyMessage)) {
            return;
        }
        socketClient.asyncSendObject(newValue);
    }

    public Notifier<ServerMessage> getServerMessageNotifier() {
        return serverMessageNotifier;
    }
}
