package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.cards.characters.WineCharacter.WineCharacter;
import it.polimi.ingsw.cards.characters.WineCharacter.WineCharacterArgument;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.exceptions.EntranceFullException;
import it.polimi.ingsw.exceptions.InsufficientCoinException;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PublicModelTest {

    @Test
    void playAssistant() {
        Model model = twoPlayersBasicSetup();
        assertNull(model.currentPlayer.getCurrentAssistantCard());
        model.publicModel.playAssistant(AssistantCard.CARD_6);
        assertEquals(AssistantCard.CARD_6, model.currentPlayer.getCurrentAssistantCard());
        model.publicModel.playAssistant(AssistantCard.CARD_3);
        assertEquals(AssistantCard.CARD_3, model.currentPlayer.getCurrentAssistantCard());
    }

    @Test
    void drawStudentsIntoEntrance() {
        Model model = twoPlayersBasicSetup();
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

    public static void setupPlayCharacterCard(Model model, CharacterCard characterCard) {
        model.currentGameCards.set(0, characterCard);
    }

    @Test
    void playCharacterCard() {
        Model model = twoPlayersExpertMode();

        WineCharacter wineCharacter = new WineCharacter(model);

        setupPlayCharacterCard(model, wineCharacter);

        int studentToMoveId = wineCharacter.getStudents().get(0).getID();
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

    private static Model createModel(Map<String, DeckName> nicknamesAndDecks, GameMode gameMode) {
        try {
            Model model = new Model(0, nicknamesAndDecks, gameMode);
            model.currentPlayer = model.players.get(0);
            return model;
        } catch (Exception e) {
            assert false;
        }

        assert false;
        return null;
    }

    public static Model twoPlayersBasicSetup() {
        Map<String, DeckName> temp = new HashMap<>();
        temp.put("Player1", DeckName.DESERT_KING);
        temp.put("Player2", DeckName.CLOUD_WITCH);
        return createModel(temp, GameMode.EASY_MODE);
    }

    public static Model twoPlayersExpertMode() {
        Map<String, DeckName> temp = new HashMap<>();
        temp.put("Player1", DeckName.DESERT_KING);
        temp.put("Player2", DeckName.CLOUD_WITCH);
        return createModel(temp, GameMode.EXPERT_MODE);
    }

    public static void incrementCurrentPlayer(Model model) {
        model.privateModel.incrementCurrentPlayer();
    }

    public static Board getProfessorOwner(Model model, Color color) {
        return model.privateModel.getProfessorOwner(color);
    }

    public static CharacterCard getCharCard(Model model) {
        return model.currentGameCards.get(0);
    }

    public static List<Island> getIslands(Model model) {
        return model.islands;
    }

    public static void setBag(Model model, Bag newBag) {
        model.bag = newBag;
    }
}