package it.polimi.ingsw.mvc.view.GUI;

import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.enums.GamePhase;
import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.ServerMessage;
import it.polimi.ingsw.messages.game.ClientGameMessage;
import it.polimi.ingsw.messages.lobby.client.SetDeckMessage;
import it.polimi.ingsw.messages.lobby.client.SetNicknameMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.CreateLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.JoinLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.RequestLobbyNamesListMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.ClientView;
import it.polimi.ingsw.notifier.Notifier;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.player.Player;

import java.util.List;

public class GUIView extends ClientView {
    //lobby window components
    public static GUIView thisGUI;


    public GUIView(Notifier<ServerMessage> messageNotifier, Notifier<Model> modelNotifier) {
        super(messageNotifier, modelNotifier);

    }

    @Override
    public void show() {
        if (model != null) {
            if (model.publicModel.getWinner() != null) {
                String winner = model.publicModel.getWinner().getNickname();
                if (winner.equals(myUsername)) {
                    LobbyFrame.lobbyFrame.win();
                } else {
                    LobbyFrame.lobbyFrame.showWinner(winner);
                }
            } else {
                showModel();
            }
        }
    }

    @Override
    protected void showModel() {
        LobbyFrame.lobbyFrame.updateModel(model);


        String currentPlayerNickname = model.publicModel.getCurrentPlayer().getNickname();
        if (!currentPlayerNickname.equals(this.myUsername)) {
            LobbyFrame.lobbyFrame.waitForTurn(currentPlayerNickname);
            return;
        }

        if (model.publicModel.getGamePhase().equals(GamePhase.PLANNING)) {
            LobbyFrame.lobbyFrame.planningPhase(model.publicModel.getCurrentPlayer().getDeck());
        } else {
            LobbyFrame.lobbyFrame.actionPhase(model.publicModel.getCurrentPlayer().getBoard(), model.publicModel.getGameMode(), model.publicModel.isCharacterCardPlayed(), model.publicModel.enoughStudentsPlaced(), model.publicModel.isMotherNatureMoved());
        }
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

    public void sendMessage(ClientGameMessage message) {
        try {
            notifySubscribers(message);
        } catch (Exception e) {
            showError(new ErrorMessage(e.getMessage()));

        }
    }

    public void closeApp(String s) {
        LobbyFrame.lobbyFrame.closedFromServer(s);
    }

    public int getMotherNatureMaxMovement() {
        return model.publicModel.getCurrentPlayer().getMaxMotherNatureMovementValue();
    }

    public int getMotherNatureIslandIndex() {
        return model.publicModel.getIslands().indexOf(model.publicModel.getMotherNatureIsland());
    }

    public List<Student> getThisPlayerEntrance() {
        for (Player p : model.publicModel.getPlayers()) {
            if (p.getNickname().equals(getMyUsername())) {
                return p.getBoard().getEntrance();
            }
        }
        throw new RuntimeException("Impossible state of game");
    }

}


