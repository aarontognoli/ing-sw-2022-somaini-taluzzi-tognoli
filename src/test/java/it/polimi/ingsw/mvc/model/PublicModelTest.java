package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.cloud.Cloud;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PublicModelTest {

    @Test
    void playAssistant() {
        Model model = twoPlayersBasicSetup();
        model.currentPlayer = model.players.get(0);
        assertNull(model.currentPlayer.getCurrentAssistantCard());
        model.publicModel.playAssistant(AssistantCard.CARD_6);
        assertEquals(AssistantCard.CARD_6, model.currentPlayer.getCurrentAssistantCard());
        model.publicModel.playAssistant(AssistantCard.CARD_3);
        assertEquals(AssistantCard.CARD_3, model.currentPlayer.getCurrentAssistantCard());
    }

    @Test
    void drawStudentsIntoEntrance() {
        Model model = twoPlayersBasicSetup();
        model.currentPlayer = model.players.get(0);
        assertTrue(model.currentPlayer.getBoard().getEntrance().isEmpty());
        try {
            model.privateModel.fillClouds();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.publicModel.drawStudentsIntoEntrance(1);
        assertEquals(3, model.currentPlayer.getBoard().getEntrance().size());
    }

    @Test
    void endTurn() {
        //TODO
    }

    @Test
    void getMotherNatureIsland() {
        Model model = twoPlayersBasicSetup();
        assertEquals(model.islands.get(0), model.publicModel.getMotherNatureIsland());
    }

    /*
    @Test
    void setPlayersCount() {
    }

    @Test
    void setGameMode() {
    }

    @Test
    void chooseDeck() {
    }
    */

    @Test
    void moveMotherNature() {
        Model model = twoPlayersBasicSetup();
        model.currentPlayer = model.players.get(0);
        model.currentPlayer.setCurrentAssistantCard(AssistantCard.CARD_3);
        try {
            model.publicModel.moveMotherNature(2);
        }catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(model.islands.get(2), model.motherNature.getPosition());
    }

    @Test
    void moveStudentToIsland() {
    }

    @Test
    void moveStudentToDiningRoom() {
    }

    @Test
    void playCharacterCard() {
    }

    @Test
    void updateIslandOwner() {
        //TODO
    }

    Model twoPlayersBasicSetup() {
        Model model = null;
        Map<String, DeckName> temp = new HashMap<>();
        temp.put("Player1", DeckName.DESERT_KING);
        temp.put("Player2", DeckName.CLOUD_WITCH);
        try {
            model = new Model(0, temp, GameMode.EASY_MODE);
        } catch (Exception e) {

        }
        return model;
    }
}