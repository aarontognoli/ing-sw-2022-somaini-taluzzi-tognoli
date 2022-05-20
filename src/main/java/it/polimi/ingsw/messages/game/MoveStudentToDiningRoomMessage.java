package it.polimi.ingsw.messages.game;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.mvc.controller.ServerController;

public class MoveStudentToDiningRoomMessage extends ClientGameMessage {
    private final Color color;

    public MoveStudentToDiningRoomMessage(Color color) {
        this.color = color;
    }

    @Override
    public void controllerCallback(ServerController controller) throws Exception {
        controller.moveStudentToDiningRoom(color);
    }
}
