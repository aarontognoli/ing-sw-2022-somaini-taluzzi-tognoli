package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.GamePhase;
import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.mvc.PlayerActions;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.server.gameMessage;

/**
 * The server controller receives message from the remote-view,
 * which comes from its client, and updates the model accordingly
 */
public class ServerController extends Controller implements PlayerActions {

    private final Model model;
    //Turn actions checks
    //Move in dedicated class(?)
    Boolean characterCardPlayed;
    Boolean motherNatureMoved;
    int studentsPlaced;

    public ServerController(Model model) {
        this.model = model;
        resetChecks();
    }

    void resetChecks() {
        characterCardPlayed = false;
        studentsPlaced = 0;
        motherNatureMoved = false;
    }

    Boolean enoughStudentsPlaced() {
        int maxStudentsToMove = 3;
        if (model.publicModel.getTotalPlayerCount() == 3) {
            maxStudentsToMove = 4;
        }
        return studentsPlaced >= maxStudentsToMove;
    }

    @Override
    public void subscribeNotification(Message message) {
        if (model.publicModel.getWinner() != null) {
            message.getRemoteView().sendErrorMessage(gameMessage.playerAlreadyWonMessage + model.publicModel.getWinner().getNickname());
            return;
        }
        if (!message.getUsername().equals(model.publicModel.getCurrentPlayer().getNickname())) {
            message.getRemoteView().sendErrorMessage(gameMessage.wrongTurnMessage);
            return;
        }
        try {
            synchronized (this) {
                message.controllerCallback(this);
            }
        } catch (Exception e) {
            message.getRemoteView().sendErrorMessage(e.getMessage());
        }

        // here the model notifies the remote views with its new state
        model.notifySubscribers(model);
    }

    // TODO: Check for exceptions and turn order
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
        if (!enoughStudentsPlaced()) {
            throw new WrongActionException(gameMessage.notEnoughStudentsAlreadyPlaced);
        }
        if (!motherNatureMoved) {
            throw new WrongActionException(gameMessage.motherNatureNotMovedMessage);
        }
        model.publicModel.drawStudentsIntoEntrance(cloudIndex);
        model.publicModel.endTurn();
        resetChecks();
    }

    @Override
    public void moveMotherNature(int steps) throws Exception {
        if (model.publicModel.getGamePhase() != GamePhase.ACTION) {
            throw new WrongActionException(gameMessage.wrongGamePhaseMessage);
        }
        if (!enoughStudentsPlaced()) {
            throw new WrongActionException(gameMessage.notEnoughStudentsAlreadyPlaced);
        }
        model.publicModel.moveMotherNature(steps);
        model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());
        motherNatureMoved = true;
    }

    @Override
    public void moveStudentToIsland(Color studentColor, int islandIndex) throws Exception {
        if (model.publicModel.getGamePhase() != GamePhase.ACTION) {
            throw new WrongActionException(gameMessage.wrongGamePhaseMessage);
        }
        if (enoughStudentsPlaced()) {
            throw new WrongActionException(gameMessage.maxStudentsAlreadyPlacedMessage);
        }
        model.publicModel.moveStudentToIsland(studentColor, islandIndex);
        studentsPlaced++;
    }

    @Override
    public void moveStudentToDiningRoom(Color studentColor) throws Exception {
        if (model.publicModel.getGamePhase() != GamePhase.ACTION) {
            throw new WrongActionException(gameMessage.wrongGamePhaseMessage);
        }
        if (enoughStudentsPlaced()) {
            throw new WrongActionException(gameMessage.maxStudentsAlreadyPlacedMessage);
        }
        model.publicModel.moveStudentToDiningRoom(studentColor);
        studentsPlaced++;
    }

    @Override
    public void playCharacterCard(int cardIndex, Object effectArgument) throws Exception {
        if (model.publicModel.getGamePhase() != GamePhase.ACTION) {
            throw new WrongActionException(gameMessage.wrongGamePhaseMessage);
        }
        if (characterCardPlayed) {
            throw new WrongActionException(gameMessage.characterCardAlreadyPlayedMessage);
        }
        model.publicModel.playCharacterCard(cardIndex, effectArgument);
        characterCardPlayed = true;
    }
}
