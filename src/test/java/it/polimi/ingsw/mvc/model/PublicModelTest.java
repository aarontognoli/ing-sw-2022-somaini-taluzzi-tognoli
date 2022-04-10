package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.bag.BagEmptyException;
import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.WineCharacter.WineCharacter;
import it.polimi.ingsw.cards.characters.WineCharacter.WineCharacterArgument;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.exceptions.EntranceFullException;
import it.polimi.ingsw.exceptions.InsufficientCoinException;
import it.polimi.ingsw.pawn.Student;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        assertFalse(model.currentPlayer.getBoard().getEntrance().isEmpty());
        try {
            model.privateModel.fillClouds();
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.currentPlayer.getBoard().getEntrance().clear();
        try {
            model.publicModel.drawStudentsIntoEntrance(1);
        } catch (EntranceFullException e) {
            assert false;
        }
        assertEquals(3, model.currentPlayer.getBoard().getEntrance().size());
    }

    @Test
    void endTurn() {
        // TODO once the method is complete
    }

    @Test
    void getMotherNatureIsland() {
        Model model = twoPlayersBasicSetup();
        assertEquals(model.islands.get(0), model.publicModel.getMotherNatureIsland());
    }

    @Test
    void moveMotherNature() {
        Model model = twoPlayersBasicSetup();
        model.currentPlayer = model.players.get(0);
        model.currentPlayer.setCurrentAssistantCard(AssistantCard.CARD_3);
        try {
            model.publicModel.moveMotherNature(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(model.islands.get(2), model.motherNature.getPosition());
    }

    @Test
    void moveStudentToIsland() {
        Model model = twoPlayersBasicSetup();
        model.currentPlayer = model.players.get(0);
        model.currentPlayer.getBoard().getEntrance().clear();
        model.currentPlayer.getBoard().getEntrance().add(new Student(Color.YELLOW_GNOMES, 11));
        try {
            model.publicModel.moveStudentToIsland(Color.YELLOW_GNOMES, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(model.currentPlayer.getBoard().getEntrance().isEmpty());
        for (int i = 0; i < 12; i++) {
            if (i != 0 && i != 6 && i != 2)
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
        model.currentPlayer.getBoard().getEntrance().clear();
        model.currentPlayer.getBoard().getEntrance().add(new Student(Color.YELLOW_GNOMES, 11));
        try {
            model.publicModel.moveStudentToDiningRoom(Color.YELLOW_GNOMES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(0, model.currentPlayer.getBoard().getEntrance().size());
        assertEquals(0, model.currentPlayer.getBoard().getDiningRoom().get(Color.GREEN_FROGS.ordinal()).size());
        assertEquals(0, model.currentPlayer.getBoard().getDiningRoom().get(Color.BLUE_UNICORNS.ordinal()).size());
        assertEquals(0, model.currentPlayer.getBoard().getDiningRoom().get(Color.RED_DRAGONS.ordinal()).size());
        assertEquals(0, model.currentPlayer.getBoard().getDiningRoom().get(Color.PINK_FAIRIES.ordinal()).size());
        assertEquals(1, model.currentPlayer.getBoard().getDiningRoom().get(Color.YELLOW_GNOMES.ordinal()).size());
        assertEquals(Color.YELLOW_GNOMES,
                model.currentPlayer.getBoard().getDiningRoom().get(Color.YELLOW_GNOMES.ordinal()).get(0).getColor());
        assertEquals(11,
                model.currentPlayer.getBoard().getDiningRoom().get(Color.YELLOW_GNOMES.ordinal()).get(0).getID());
    }

    @Test
    void playCharacterCard() {
        Model model = twoPlayersExpertMode();
        model.currentPlayer = model.players.get(0);

        List<Student> wineCharacterStudents = new ArrayList<>(WineCharacter.INITIAL_STUDENT_SIZE);

        for (int i = 0; i < WineCharacter.INITIAL_STUDENT_SIZE; i++) {
            try {
                wineCharacterStudents.add(model.privateModel.drawStudentFromBag());
            } catch (BagEmptyException e) {
                assert false;
            }
        }

        model.currentGameCards.set(0, new WineCharacter(model, wineCharacterStudents));

        int studentToMoveId = wineCharacterStudents.get(0).getID();
        try {
            model.publicModel.playCharacterCard(0,
                    new WineCharacterArgument(model.islands.get(0), studentToMoveId));
        } catch (InsufficientCoinException | CCArgumentException e) {
            assert false;
        }

        assertEquals(1, model.islands.get(0).getStudents().size());
        assertEquals(studentToMoveId, model.islands.get(0).getStudents().get(0).getID());
    }

    @Test
    void updateIslandOwner() {
        // TODO once the method is complete
    }

    public static Model twoPlayersBasicSetup() {
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

    public static Model twoPlayersExpertMode() {
        Model model = null;
        Map<String, DeckName> temp = new HashMap<>();
        temp.put("Player1", DeckName.DESERT_KING);
        temp.put("Player2", DeckName.CLOUD_WITCH);
        try {
            model = new Model(0, temp, GameMode.EXPERT_MODE);
        } catch (Exception e) {
        }
        return model;
    }
}