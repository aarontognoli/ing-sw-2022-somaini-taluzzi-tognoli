package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.lobby.server.ServerLobbyMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LobbyClientController extends ClientControllerBase {

    public LobbyClientController(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        super(objectInputStream, objectOutputStream);
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

        // TODO: Handle message
    }

    @Override
    public void subscribeNotification(Message newValue) {

    }
}
