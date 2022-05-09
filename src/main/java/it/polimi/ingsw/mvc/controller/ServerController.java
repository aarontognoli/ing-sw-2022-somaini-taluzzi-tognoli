package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.GamePhase;
import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.game.*;
import it.polimi.ingsw.mvc.PlayerActions;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.player.Player;
import it.polimi.ingsw.server.gameMessage;

/**
 * The server controller receives message from the remote-view,
 * which comes from its client, and updates the model accordingly
 */
public class ServerController extends Controller implements PlayerActions {

    private final Model model;
    private int studentsMoved = 0;

    public ServerController(Model model) {
        this.model = model;
    }

    private void setStudentsMoved() {
        if (studentsMoved == 3) {
            studentsMoved = 0;
        } else {
            studentsMoved++;
        }
    }

    private int getStudentsMoved() {
        return studentsMoved;
    }

    @Override
    public void subscribeNotification(Message message) {

        if (!(message instanceof GameMessage gameMsg)) {
            throw new RuntimeException("How did a wrong message get to the controller?");
        }

        if (model.publicModel.getWinner() != null) {
            gameMsg.getRemoteView().sendErrorMessage(gameMessage.playerAlreadyWonMessage + model.publicModel.getWinner().getNickname());
            return;
        }
        if (!gameMsg.getUsername().equals(model.publicModel.getCurrentPlayer().getNickname())) {
            gameMsg.getRemoteView().sendErrorMessage(gameMessage.wrongTurnMessage);
            return;
        }

        synchronized (this) {
            try {
                Player player = model.publicModel.getCurrentPlayer();

                gameMsg.controllerCallback(this);

                player.setPreviousMove(gameMsg);

                // here the model notifies the remote views with its new state
                model.notifySubscribers(model);

            } catch (Exception e) {
                gameMsg.getRemoteView().sendErrorMessage(e.getMessage());
            }
        }


    }

    @Override
    public void playAssistant(AssistantCard assistantCard) throws Exception {
        if (model.publicModel.getGamePhase() != GamePhase.PIANIFICATION) {
            throw new WrongActionException(gameMessage.wrongGamePhaseMessage);
        }
        model.publicModel.playAssistant(assistantCard);
        model.publicModel.endTurn();
    }

    @Override
    public void drawStudentsIntoEntrance(int cloudIndex) throws Exception {
        if (model.publicModel.getGamePhase() != GamePhase.ACTION) {
            throw new WrongActionException(gameMessage.wrongGamePhaseMessage);
        }
        if (!model.publicModel.getCurrentPlayer().getPreviousMove().getClass()
                .equals(MoveMotherNatureMessage.class)) {
            throw new WrongActionException(gameMessage.wrongMoveMessage);
        }
        model.publicModel.drawStudentsIntoEntrance(cloudIndex);
        setStudentsMoved();
        model.publicModel.endTurn();
    }

    @Override
    public void moveMotherNature(int steps) throws Exception {
        if (model.publicModel.getGamePhase() != GamePhase.ACTION) {
            throw new WrongActionException(gameMessage.wrongGamePhaseMessage);
        }
        if (!model.publicModel.getCurrentPlayer().getPreviousMove().getClass()
                    .equals(MoveStudentToDiningRoomMessage.class) &&
                !model.publicModel.getCurrentPlayer().getPreviousMove().getClass()
                    .equals(MoveStudentToIslandMessage.class)) {
            throw new WrongActionException(gameMessage.wrongMoveMessage);
        }
        model.publicModel.moveMotherNature(steps);
        model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());
    }

    @Override
    public void moveStudentToIsland(Color studentColor, int islandIndex) throws Exception {
        if (model.publicModel.getGamePhase() != GamePhase.ACTION) {
            throw new WrongActionException(gameMessage.wrongGamePhaseMessage);
        }
        if (isMoveStudentWrongAction() || getStudentsMoved() == 3) {
            throw new WrongActionException(gameMessage.wrongMoveMessage);
        }
        model.publicModel.moveStudentToIsland(studentColor, islandIndex);
        setStudentsMoved();
    }

    @Override
    public void moveStudentToDiningRoom(Color studentColor) throws Exception {
        if (model.publicModel.getGamePhase() != GamePhase.ACTION) {
            throw new WrongActionException(gameMessage.wrongGamePhaseMessage);
        }
        if (isMoveStudentWrongAction() || getStudentsMoved() == 3) {
            throw new WrongActionException(gameMessage.wrongMoveMessage);
        }
        model.publicModel.moveStudentToDiningRoom(studentColor);
        setStudentsMoved();
    }

    @Override
    public void playCharacterCard(int cardIndex, Object effectArgument) throws Exception {
        if (model.publicModel.getGamePhase() != GamePhase.ACTION) {
            throw new WrongActionException(gameMessage.wrongGamePhaseMessage);
        }
        model.publicModel.playCharacterCard(cardIndex, effectArgument);
    }

    private boolean isMoveStudentWrongAction() {
        return !model.publicModel.getCurrentPlayer().getPreviousMove().getClass()
                        .equals(MoveStudentToDiningRoomMessage.class) &&
                !model.publicModel.getCurrentPlayer().getPreviousMove().getClass()
                        .equals(MoveStudentToIslandMessage.class) &&
                !model.publicModel.getCurrentPlayer().getPreviousMove().getClass()
                        .equals(PlayAssistantMessage.class);
    }
}
