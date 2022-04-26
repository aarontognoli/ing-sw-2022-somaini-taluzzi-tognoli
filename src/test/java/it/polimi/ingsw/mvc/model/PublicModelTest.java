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
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PublicModelTest {

    @Test
    void playAssistant() {
        Model model = twoPlayersBasicSetup();
        assertNull(model.currentPlayer.getCurrentAssistantCard());
        assertDoesNotThrow(() -> model.publicModel.playAssistant(AssistantCard.CARD_6));
        assertEquals(AssistantCard.CARD_6, model.currentPlayer.getCurrentAssistantCard());
        assertThrows(AssistantCardAlreadyPlayedException.class, () -> model.publicModel.playAssistant(AssistantCard.CARD_3));
        model.publicModel.endTurn();
        assertDoesNotThrow(() -> model.publicModel.playAssistant(AssistantCard.CARD_3));
        assertEquals(AssistantCard.CARD_3, model.currentPlayer.getCurrentAssistantCard());
    }

    @Test
    void playAssistantNotInHand() {
        Model model = twoPlayersBasicSetup();
        assertDoesNotThrow(() -> model.publicModel.playAssistant(AssistantCard.CARD_1));//p0
        model.publicModel.endTurn();
        assertDoesNotThrow(() -> model.publicModel.playAssistant(AssistantCard.CARD_6));//p1
        model.publicModel.endTurn();
        model.publicModel.endRound();
        assertThrows(NotFoundException.class, () -> model.publicModel.playAssistant(AssistantCard.CARD_1));
    }

    public static List<Professor> getProfessors(Model fatherModel) {
        return new ArrayList<>(fatherModel.professors);
    }


    @Test
    void getMotherNatureIsland() {
        Model model = twoPlayersBasicSetup();
        assertEquals(model.islands.get(0), model.publicModel.getMotherNatureIsland());
    }

    @Test
    void moveMotherNature() {
        Model model = twoPlayersBasicSetup();
        AssistantCard card = AssistantCard.CARD_3;
        assertDoesNotThrow(() -> model.publicModel.playAssistant(card));
        assertDoesNotThrow(() -> model.publicModel.moveMotherNature(card.getMaxMotherNatureMovementValue()));
        assertEquals(model.islands.get(2), model.motherNature.getPosition());
    }

    @Test
    void invalidMoveMotherNature() {
        Model model = twoPlayersBasicSetup();
        AssistantCard card = AssistantCard.CARD_3;
        assertDoesNotThrow(() -> model.publicModel.playAssistant(card));
        assertThrows(TooMuchStepsException.class, () -> model.publicModel.moveMotherNature(card.getMaxMotherNatureMovementValue() + 1));

        assertEquals(model.islands.get(0), model.motherNature.getPosition());
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

        Color targetStudentColor = wineCharacter.getStudents().get(0).getColor();
        try {
            model.publicModel.playCharacterCard(0,
                    new WineCharacterArgument(0, targetStudentColor));
        } catch (InsufficientCoinException | CCArgumentException e) {
            assert false;
        }

        assertEquals(1, model.islands.get(0).getStudents().size());
        assertEquals(targetStudentColor, model.islands.get(0).getStudents().get(0).getColor());
    }

    @Test
    void updateIslandOwner() {
        // TODO once the method is complete
    }

    private static Model createModel(Map<String, DeckName> nicknamesAndDecks, GameMode gameMode) {
        try {
            Model model = new Model(0, nicknamesAndDecks, gameMode);
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
        temp.put("Player0", DeckName.CLOUD_WITCH);
        return createModel(temp, GameMode.EASY_MODE);
    }

    public static Model twoPlayersExpertMode() {
        Map<String, DeckName> temp = new HashMap<>();
        temp.put("Player1", DeckName.DESERT_KING);
        temp.put("Player0", DeckName.CLOUD_WITCH);
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

    //Only for test
    public static void resetInfluenceCalculatorRules(Model fatherModel) {
        fatherModel.influenceCalculator.setInfluenceCalculatorRules(new DefaultInfluenceCalculatorRules(fatherModel));
    }

    public static InfluenceCalculator getInfluenceCalculator(Model fatherModel) {
        return fatherModel.influenceCalculator;
    }

    public static Player getProfessorOwnerPlayer(Model fatherModel, Professor p) {
        try {
            return fatherModel.privateModel.getPlayerFromBoard(p.getPosition());
        } catch (BoardNotInGameException e) {
            return null;
        }
    }

    public static List<Player> getPlayers(Model fatherModel) {
        return fatherModel.players;
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
        } catch (EntranceFullException | CloudEmptyException e) {
            assert false;
        }
        assertEquals(3, model.currentPlayer.getBoard().getEntrance().size());
    }
}