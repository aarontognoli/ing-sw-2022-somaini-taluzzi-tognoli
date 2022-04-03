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

import java.time.Year;
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
        Model model = twoPlayersBasicSetup();
        model.currentPlayer = model.players.get(0);
        model.currentPlayer.getBoard().getEntrance().add(new Student(Color.YELLOW_GNOMES, 11));
        try {
            model.publicModel.moveStudentToIsland(Color.YELLOW_GNOMES, 2);
        }catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(model.currentPlayer.getBoard().getEntrance().isEmpty());
        for (int i = 0; i < 12 && i != 0 && i != 6 && i != 2; i++) {
            assertEquals(1, model.islands.get(i).getStudents().size());
        }
        assertEquals(0, model.islands.get(0).getStudents().size());
        assertEquals(0, model.islands.get(6).getStudents().size());
        assertEquals(2, model.islands.get(2).getStudents().size());
        assertEquals(Color.YELLOW_GNOMES, model.islands.get(2).getStudents().get(1).getColor());
        assertEquals(11, model.islands.get(2).getStudents().get(1).getID());
    }

    @Test
    void moveStudentToDiningRoom() {
        Model model = twoPlayersBasicSetup();
        model.currentPlayer = model.players.get(0);
        model.currentPlayer.getBoard().getEntrance().add(new Student(Color.YELLOW_GNOMES, 11));
        try{
            model.publicModel.moveStudentToDiningRoom(Color.YELLOW_GNOMES);
        }catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(0, model.currentPlayer.getBoard().getEntrance().size());
        assertEquals(0, model.currentPlayer.getBoard().getDiningRoom().get(Color.GREEN_FROGS.ordinal()).size());
        assertEquals(0, model.currentPlayer.getBoard().getDiningRoom().get(Color.BLUE_UNICORNS.ordinal()).size());
        assertEquals(0, model.currentPlayer.getBoard().getDiningRoom().get(Color.RED_DRAGONS.ordinal()).size());
        assertEquals(0, model.currentPlayer.getBoard().getDiningRoom().get(Color.PINK_FAIRIES.ordinal()).size());
        assertEquals(1, model.currentPlayer.getBoard().getDiningRoom().get(Color.YELLOW_GNOMES.ordinal()).size());
        assertEquals(Color.YELLOW_GNOMES, model.currentPlayer.getBoard().getDiningRoom().
                get(Color.YELLOW_GNOMES.ordinal()).get(0).getColor());
        assertEquals(11, model.currentPlayer.getBoard().getDiningRoom().
                get(Color.YELLOW_GNOMES.ordinal()).get(0).getID());
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