package it.polimi.ingsw.messages;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.mvc.controller.ServerController;

public class MoveStudentToDiningRoomMessage extends Message{
    private final Color color;

    public MoveStudentToDiningRoomMessage(Color color) {
        this.color = color;
    }

    @Override
    public void controllerCallback(ServerController controller) throws Exception {
        controller.moveStudentToDiningRoom(color);
    }
}
