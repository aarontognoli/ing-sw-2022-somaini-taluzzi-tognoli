package it.polimi.ingsw.messages;

import it.polimi.ingsw.mvc.controller.ServerController;

public class DrawStudentIntoEntranceMessage extends Message{
    private final int cloudIndex;

    public DrawStudentIntoEntranceMessage (int cloudIndex) {
        this.cloudIndex = cloudIndex;
    }


    @Override
    public void controllerCallback(ServerController controller) throws Exception {
        controller.drawStudentsIntoEntrance(cloudIndex);
    }
}
