package it.polimi.ingsw.mvc;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.Color;

public interface PlayerActions {

    void playAssistant(AssistantCard assistantCard) throws Exception;

    void drawStudentsIntoEntrance(int cloudIndex) throws Exception;

    void moveMotherNature(int steps) throws Exception;

    void moveStudentToIsland(Color studentColor, int islandIndex) throws Exception;

    void moveStudentToDiningRoom(Color studentColor) throws Exception;

    void playCharacterCard(int cardIndex, Object effectArgument) throws Exception;
}
