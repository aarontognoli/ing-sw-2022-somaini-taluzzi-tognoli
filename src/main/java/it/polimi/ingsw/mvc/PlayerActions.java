package it.polimi.ingsw.mvc;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.Color;

public interface PlayerActions {

    /**
     * @param assistantCard the assistant card we want to play
     */
    void playAssistant(AssistantCard assistantCard) throws Exception;

    /**
     * @param cloudIndex  index of the target cloud
     */
    void drawStudentsIntoEntrance(int cloudIndex) throws Exception;

    /**
     * @param steps number of steps we want mother nature to do
     */
    void moveMotherNature(int steps) throws Exception;

    /**
     * @param studentColor color of the student we need to move from entrance to
     *                     island
     * @param islandIndex  index of the target island
     */
    void moveStudentToIsland(Color studentColor, int islandIndex) throws Exception;

    /**
     * @param studentColor color of the student we need to mode from entrance to
     *                     dining room
     */
    void moveStudentToDiningRoom(Color studentColor) throws Exception;

    /**
     * @param cardIndex index of the character card we want to play
     * @param effectArgument the argument object of the character card we want to play
     */
    void playCharacterCard(int cardIndex, Object effectArgument) throws Exception;
}
