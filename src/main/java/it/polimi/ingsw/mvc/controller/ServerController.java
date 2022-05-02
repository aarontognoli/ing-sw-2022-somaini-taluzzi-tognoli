package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.Color;
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

    public ServerController(Model model) {
        this.model = model;
    }

    @Override
    public void subscribeNotification(Message message) {
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

    @Override
    public void playAssistant(AssistantCard assistantCard) throws Exception {
        // TODO: Check for exceptions and turn order
        model.publicModel.playAssistant(assistantCard);
        model.publicModel.endTurn();
    }

    @Override
    public void drawStudentsIntoEntrance(int cloudIndex) throws Exception {
        model.publicModel.drawStudentsIntoEntrance(cloudIndex);
        model.publicModel.endTurn();
    }

    @Override
    public void moveMotherNature(int steps) throws Exception {
        model.publicModel.moveMotherNature(steps);
        model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());
    }

    @Override
    public void moveStudentToIsland(Color studentColor, int islandIndex) throws Exception {
        model.publicModel.moveStudentToIsland(studentColor, islandIndex);
    }

    @Override
    public void moveStudentToDiningRoom(Color studentColor) throws Exception {
        model.publicModel.moveStudentToDiningRoom(studentColor);
    }

    @Override
    public void playCharacterCard(int cardIndex, Object effectArgument) throws Exception {
        model.publicModel.playCharacterCard(cardIndex, effectArgument);
    }
}
