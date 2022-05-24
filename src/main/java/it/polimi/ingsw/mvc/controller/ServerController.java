package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.GamePhase;
import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.messages.game.ClientGameMessage;
import it.polimi.ingsw.mvc.PlayerActions;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.notifier.Notifier;
import it.polimi.ingsw.server.GameMessageConstants;

/**
 * The server controller receives message from the remote-view,
 * which comes from its client, and updates the model accordingly
 */
public class ServerController extends Controller implements PlayerActions {

    private final Model model;
    private final Notifier<Model> modelNotifier;

    public ServerController(Model model, Notifier<Model> modelNotifier) {
        this.model = model;
        this.modelNotifier = modelNotifier;
    }

    Boolean enoughStudentsPlaced() {
        int maxStudentsToMove = 3;
        // if entrance is empty the player must keep playing
        if (model.publicModel.getCurrentPlayer().getBoard().getEntrance().isEmpty())
            return true;

        if (model.publicModel.getTotalPlayerCount() == 3) {
            maxStudentsToMove = 4;
        }
        return model.publicModel.getStudentsPlaced() >= maxStudentsToMove;
    }

    @Override
    public void subscribeNotification(ClientMessage message) {

        if (!(message instanceof ClientGameMessage gameMsg)) {
            throw new RuntimeException("How did a wrong message get to the controller?");
        }

        if (model.publicModel.getWinner() != null) {
            gameMsg.getRemoteView().sendErrorMessage(
                    GameMessageConstants.playerAlreadyWonMessage + model.publicModel.getWinner().getNickname());
            return;
        }
        if (!gameMsg.getUsername().equals(model.publicModel.getCurrentPlayer().getNickname())) {
            gameMsg.getRemoteView().sendErrorMessage(GameMessageConstants.wrongTurnMessage);
            return;
        }

        synchronized (this) {
            try {
                gameMsg.controllerCallback(this);

                // we notify the remote views with the new model
                modelNotifier.notifySubscribers(model);
            } catch (Exception e) {
                gameMsg.getRemoteView().sendErrorMessage(e.getMessage());
            }
        }

    }

    @Override
    public void playAssistant(AssistantCard assistantCard) throws Exception {
        if (model.publicModel.getGamePhase() != GamePhase.PIANIFICATION) {
            throw new WrongActionException(GameMessageConstants.wrongGamePhaseMessage);
        }
        model.publicModel.playAssistant(assistantCard);
        model.publicModel.endTurn();
    }

    @Override
    public void drawStudentsIntoEntrance(int cloudIndex) throws Exception {
        if (model.publicModel.getGamePhase() != GamePhase.ACTION) {
            throw new WrongActionException(GameMessageConstants.wrongGamePhaseMessage);
        }
        if (!enoughStudentsPlaced()) {
            throw new WrongActionException(GameMessageConstants.notEnoughStudentsAlreadyPlaced);
        }
        if (!model.publicModel.isMotherNatureMoved()) {
            throw new WrongActionException(GameMessageConstants.motherNatureNotMovedMessage);
        }
        model.publicModel.drawStudentsIntoEntrance(cloudIndex);
        model.publicModel.endTurn();
        model.publicModel.resetChecks();
    }

    @Override
    public void moveMotherNature(int steps) throws Exception {
        if (model.publicModel.getGamePhase() != GamePhase.ACTION) {
            throw new WrongActionException(GameMessageConstants.wrongGamePhaseMessage);
        }
        if (!enoughStudentsPlaced()) {
            throw new WrongActionException(GameMessageConstants.notEnoughStudentsAlreadyPlaced);
        }
        model.publicModel.moveMotherNature(steps);
        model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());
        model.publicModel.setMotherNatureMoved(true);
    }

    @Override
    public void moveStudentToIsland(Color studentColor, int islandIndex) throws Exception {
        if (model.publicModel.getGamePhase() != GamePhase.ACTION) {
            throw new WrongActionException(GameMessageConstants.wrongGamePhaseMessage);
        }
        if (enoughStudentsPlaced()) {
            throw new WrongActionException(GameMessageConstants.maxStudentsAlreadyPlacedMessage);
        }
        model.publicModel.moveStudentToIsland(studentColor, islandIndex);
        model.publicModel.setStudentsPlaced(model.publicModel.getStudentsPlaced() + 1);
    }

    @Override
    public void moveStudentToDiningRoom(Color studentColor) throws Exception {
        if (model.publicModel.getGamePhase() != GamePhase.ACTION) {
            throw new WrongActionException(GameMessageConstants.wrongGamePhaseMessage);
        }
        if (enoughStudentsPlaced()) {
            throw new WrongActionException(GameMessageConstants.maxStudentsAlreadyPlacedMessage);
        }
        model.publicModel.moveStudentToDiningRoom(studentColor);
        model.publicModel.setStudentsPlaced(model.publicModel.getStudentsPlaced() + 1);
    }

    @Override
    public void playCharacterCard(int cardIndex, Object effectArgument) throws Exception {
        if (model.publicModel.getGamePhase() != GamePhase.ACTION) {
            throw new WrongActionException(GameMessageConstants.wrongGamePhaseMessage);
        }
        if (model.publicModel.isCharacterCardPlayed()) {
            throw new WrongActionException(GameMessageConstants.characterCardAlreadyPlayedMessage);
        }
        model.publicModel.playCharacterCard(cardIndex, effectArgument);
        model.publicModel.setCharacterCardPlayed(true);
    }

}
