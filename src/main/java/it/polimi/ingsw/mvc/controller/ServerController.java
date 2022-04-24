package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.mvc.ModelActions;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Player;

/**
 * The server controller receives message from the remote-view,
 * which comes from its client, and updates the model accordingly
 */
public class ServerController extends Controller implements ModelActions {

    private final Model model;

    public ServerController(Model model) {
        this.model = model;
    }

    @Override
    public void subscribeNotification(Message newValue) {
        // TODO: Decode message, and call the correct method of this class
    }

    @Override
    public void playAssistant(AssistantCard assistantCard) throws Exception {
        // TODO: Check for exceptions and turn order
        model.publicModel.playAssistant(assistantCard);
    }

    @Override
    public void drawStudentsIntoEntrance(int cloudIndex) throws Exception {

    }

    @Override
    public void endTurn() {

    }

    @Override
    public void endRound() {

    }

    @Override
    public void moveMotherNature(int steps) throws Exception {

    }

    @Override
    public void moveStudentToIsland(Color studentColor, int islandIndex) throws Exception {

    }

    @Override
    public void moveStudentToDiningRoom(Color studentColor) throws Exception {

    }

    @Override
    public void playCharacterCard(int cardIndex, Object effectArgument) throws Exception {

    }

    @Override
    public void updateIslandOwner(Island island) {

    }

    @Override
    public Player checkFinishedTowers() {
        return null;
    }
}
