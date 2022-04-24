package it.polimi.ingsw.mvc;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Player;

public interface ModelActions {

    void playAssistant(AssistantCard assistantCard) throws Exception;

    void drawStudentsIntoEntrance(int cloudIndex) throws Exception;

    void endTurn();

    void endRound();

    void moveMotherNature(int steps) throws Exception;

    void moveStudentToIsland(Color studentColor, int islandIndex) throws Exception;

    void moveStudentToDiningRoom(Color studentColor) throws Exception;

    void playCharacterCard(int cardIndex, Object effectArgument) throws Exception;

    void updateIslandOwner(Island island);

    Player checkFinishedTowers();
}
