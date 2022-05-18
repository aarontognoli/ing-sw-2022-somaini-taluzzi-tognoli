package it.polimi.ingsw.messages.game;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.mvc.controller.ServerController;

public class MoveStudentToIslandMessage extends ClientGameMessage {
    private final Color studentColor;
    private final int islandIndex;

    public MoveStudentToIslandMessage(Color studentColor, int islandIndex) {
        this.studentColor = studentColor;
        this.islandIndex = islandIndex;
    }

    @Override
    public void controllerCallback(ServerController controller) throws Exception {
        controller.moveStudentToIsland(studentColor, islandIndex);
    }
}
