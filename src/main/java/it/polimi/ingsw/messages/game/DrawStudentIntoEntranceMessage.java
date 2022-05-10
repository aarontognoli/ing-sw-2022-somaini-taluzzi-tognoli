package it.polimi.ingsw.messages.game;

import it.polimi.ingsw.mvc.controller.ServerController;

public class DrawStudentIntoEntranceMessage extends GameMessage {
    private final int cloudIndex;

    public DrawStudentIntoEntranceMessage(int cloudIndex) {
        this.cloudIndex = cloudIndex;
    }


    @Override
    public void controllerCallback(ServerController controller) throws Exception {
        controller.drawStudentsIntoEntrance(cloudIndex);
    }
}
