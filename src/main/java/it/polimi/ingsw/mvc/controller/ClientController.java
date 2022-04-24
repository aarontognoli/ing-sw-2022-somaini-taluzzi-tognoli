package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.messages.Message;

/**
 * The client controller receives message from the client View (Be it CLI or GUI)
 * and then forwards the message to the server
 */
public class ClientController extends Controller {

    public ClientController() {
        // TODO: Add client connection
    }

    @Override
    public void subscribeNotification(Message newValue) {
        // TODO: Forward the message to the server
    }
}
