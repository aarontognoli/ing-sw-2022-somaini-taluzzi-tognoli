package it.polimi.ingsw.messages.game;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.mvc.controller.ServerController;
import it.polimi.ingsw.mvc.view.game.RemoteView;

public abstract class GameMessage extends Message {
    private String username;
    private RemoteView remoteView;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RemoteView getRemoteView() {
        return remoteView;
    }

    public void setRemoteView(RemoteView remoteView) {
        this.remoteView = remoteView;
    }

    public abstract void controllerCallback(ServerController controller) throws Exception;

}
