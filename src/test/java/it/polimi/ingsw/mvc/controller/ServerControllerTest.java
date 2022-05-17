package it.polimi.ingsw.mvc.controller;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.WrongActionException;
import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.game.*;
import it.polimi.ingsw.messages.lobby.client.SetNicknameMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.model.PublicModelTest;
import it.polimi.ingsw.mvc.view.game.RemoteView;
import it.polimi.ingsw.notifier.Notifier;
import it.polimi.ingsw.server.GameMessageConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerControllerTest {
    Model model;
    Controller controller;
    ErrorMessage errorMessage;
    GameMessage message;
    RemoteView player0View;
    RemoteView player1View;

    private class RemoteViewStub extends RemoteView {
        Model modelView;

        public RemoteViewStub(Notifier<Model> modelNotifier, String username) {
            super(modelNotifier, username, null);
        }

        @Override
        public void sendErrorMessage(String error) {
            errorMessage = new ErrorMessage(error);
        }

        @Override
        public void subscribeNotification(Model newValue) {
            this.modelView = newValue;
        }
    }

    private void initialiseTwoPlayersExpertMode() {
        model = PublicModelTest.twoPlayersExpertMode();
        Notifier<Model> modelNotifier = new Notifier<>();
        controller = new ServerController(model);
        player0View = new RemoteViewStub(modelNotifier, "Player0");
        player1View = new RemoteViewStub(modelNotifier, "Player1");
    }

    private void sendMessagePlayer0(GameMessage message) {
        message.setUsername("Player0");
        message.setRemoteView(player0View);
        controller.subscribeNotification(message);
    }

    private void sendMessagePlayer1(GameMessage message) {
        message.setUsername("Player1");
        message.setRemoteView(player1View);
        controller.subscribeNotification(message);
    }

    @Test
    void subscribeNotification() {
        initialiseTwoPlayersExpertMode();

        assertThrows(RuntimeException.class,
                () -> controller.subscribeNotification(new SetNicknameMessage("username1")));

        message = new DrawStudentIntoEntranceMessage(0);
        sendMessagePlayer0(message);
        assertEquals(WrongActionException.MESSAGE_PREFIX + GameMessageConstants.wrongGamePhaseMessage,
                errorMessage.getErrorMessageString());

        errorMessage = null;
        message = new PlayAssistantMessage(AssistantCard.CARD_1);
        sendMessagePlayer1(message);
        assertEquals(GameMessageConstants.wrongTurnMessage, errorMessage.getErrorMessageString());

        errorMessage = null;
        sendMessagePlayer0(message);
        assertNull(errorMessage);

        message = new PlayAssistantMessage(AssistantCard.CARD_3);
        sendMessagePlayer1(message);
        assertNull(errorMessage);

        message = new PlayAssistantMessage(AssistantCard.CARD_3);
        sendMessagePlayer0(message);
        assertEquals(WrongActionException.MESSAGE_PREFIX + GameMessageConstants.wrongGamePhaseMessage,
                errorMessage.getErrorMessageString());

        message = new DrawStudentIntoEntranceMessage(0);
        sendMessagePlayer0(message);
        assertEquals(WrongActionException.MESSAGE_PREFIX + GameMessageConstants.notEnoughStudentsAlreadyPlaced,
                errorMessage.getErrorMessageString());

        message = new MoveMotherNatureMessage(2);
        sendMessagePlayer0(message);
        assertEquals(WrongActionException.MESSAGE_PREFIX + GameMessageConstants.notEnoughStudentsAlreadyPlaced,
                errorMessage.getErrorMessageString());

        errorMessage = null;
        Color color0 = model.publicModel.getCurrentPlayer().getBoard().getEntrance().get(0).getColor();
        Color color1 = model.publicModel.getCurrentPlayer().getBoard().getEntrance().get(1).getColor();
        Color color2 = model.publicModel.getCurrentPlayer().getBoard().getEntrance().get(2).getColor();
        Color color3 = model.publicModel.getCurrentPlayer().getBoard().getEntrance().get(3).getColor();
        message = new MoveStudentToDiningRoomMessage(color0);
        sendMessagePlayer0(message);
        message = new MoveStudentToDiningRoomMessage(color1);
        sendMessagePlayer0(message);
        message = new MoveStudentToDiningRoomMessage(color2);
        sendMessagePlayer0(message);
        assertNull(errorMessage);

        message = new MoveStudentToDiningRoomMessage(color3);
        sendMessagePlayer0(message);
        assertEquals(WrongActionException.MESSAGE_PREFIX + GameMessageConstants.maxStudentsAlreadyPlacedMessage,
                errorMessage.getErrorMessageString());

        message = new MoveMotherNatureMessage(3);
        sendMessagePlayer0(message);
        assertEquals("Trying to move mother nature for 3 steps, maximum is 1",
                errorMessage.getErrorMessageString());

        errorMessage = null;
        message = new MoveMotherNatureMessage(1);
        sendMessagePlayer0(message);
        assertNull(errorMessage);

        message = new DrawStudentIntoEntranceMessage(0);
        sendMessagePlayer0(message);
        assertNull(errorMessage);

        color0 = model.publicModel.getCurrentPlayer().getBoard().getEntrance().get(0).getColor();
        message = new MoveStudentToIslandMessage(color0, 10);
        sendMessagePlayer1(message);
        assertNull(errorMessage);

        message = new DrawStudentIntoEntranceMessage(0);
        sendMessagePlayer0(message);
        assertEquals(GameMessageConstants.wrongTurnMessage, errorMessage.getErrorMessageString());
    }

    @Test
    void moveMotherNature() {
        initialiseTwoPlayersExpertMode();
        message = new MoveMotherNatureMessage(1);
        sendMessagePlayer0(message);
        assertEquals(WrongActionException.MESSAGE_PREFIX + GameMessageConstants.wrongGamePhaseMessage,
                errorMessage.getErrorMessageString());
    }
}