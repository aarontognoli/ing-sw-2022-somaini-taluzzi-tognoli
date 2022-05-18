package it.polimi.ingsw.messages.game;

import it.polimi.ingsw.mvc.controller.ServerController;

public class MoveMotherNatureMessage extends ClientGameMessage {
    private final int steps;

    public MoveMotherNatureMessage(int steps) {
        this.steps = steps;
    }

    @Override
    public void controllerCallback(ServerController controller) throws Exception {
        controller.moveMotherNature(steps);
    }
}
