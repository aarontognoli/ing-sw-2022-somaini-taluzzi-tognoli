package it.polimi.ingsw.messages;

import it.polimi.ingsw.mvc.controller.ServerController;

public class MoveMotherNatureMessage extends Message {
    private final int steps;

    public MoveMotherNatureMessage(int steps) {
        this.steps = steps;
    }

    @Override
    public void controllerCallback(ServerController controller) throws Exception {
        controller.moveMotherNature(steps);
    }
}
