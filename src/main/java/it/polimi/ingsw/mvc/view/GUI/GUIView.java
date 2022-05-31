package it.polimi.ingsw.mvc.view.GUI;

import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.ServerMessage;
import it.polimi.ingsw.messages.lobby.client.SetDeckMessage;
import it.polimi.ingsw.messages.lobby.client.SetNicknameMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.CreateLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.JoinLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.RequestLobbyNamesListMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.ClientView;
import it.polimi.ingsw.notifier.Notifier;

public class GUIView extends ClientView {
    //lobby window components
    public static GUIView thisGUI;


    public GUIView(Notifier<ServerMessage> messageNotifier, Notifier<Model> modelNotifier) {
        super(messageNotifier, modelNotifier);

    }

    @Override
    public void show() {
        showModel();
    }

    @Override
    protected void showModel() {
        LobbyFrame.lobbyFrame.updateModel(model);
    }

    public void showError(ErrorMessage em) {
        LobbyFrame.lobbyFrame.showError(em);
    }

    public void showInfo(String s) {
        LobbyFrame.lobbyFrame.showInfo(s);
    }


    public LobbyFrame app;


    @Override
    public void run() throws InterruptedException {
        //show
        app = new LobbyFrame();
        thisGUI = this;
        app.open();


    }

    @Override
    public void subscribeNotification(ServerMessage newMessage) {
        newMessage.updateGUI(this);

    }




    public void startGame() {
        LobbyFrame.lobbyFrame.showGameView();
    }


    public void reloadLobbies() {
        notifySubscribers(new RequestLobbyNamesListMessage());
    }


    public void joinLobby(String lobbyName) {
        if (lobbyName == null)
            showError(new ErrorMessage("Invalid lobby"));
        else
            notifySubscribers(new JoinLobbyMessage(lobbyName));


    }

    public void createLobby(String lobbyName, int playersNumber, GameMode gamemode, int motherNatureIslandIndex) {
        if (lobbyName.isEmpty())
            showError(new ErrorMessage("Insert a lobby name."));
        else
            notifySubscribers(new CreateLobbyMessage(lobbyName, playersNumber, gamemode, motherNatureIslandIndex));
    }

    public void setUsername(String username) {
        if (username.isEmpty())
            showError(new ErrorMessage("You need to specify a username"));
        else {
            this.setMyUsername(username);
            notifySubscribers(new SetNicknameMessage(username));

        }
    }

    public void setDeckName(DeckName deckName) {
        notifySubscribers(new SetDeckMessage(deckName));
    }

    public void showSetUsernameAndDeckFrame() {
        LobbyFrame.lobbyFrame.loadUsernameAndDeckFrame();
    }

    public void enableSetDeckPane() {
        LobbyFrame.lobbyFrame.unlockDeckPane();
    }

    public void loadCreateFrame() {
        LobbyFrame.lobbyFrame.loadCreateFrame();
    }
}


