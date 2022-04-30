package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.notifier.Notifier;

public interface ClientConnection {
    public void closeConnection();
    public void asyncSend(Object message);
}
