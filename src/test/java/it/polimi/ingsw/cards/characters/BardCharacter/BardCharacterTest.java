package it.polimi.ingsw.cards.characters.BardCharacter;

import it.polimi.ingsw.pawn.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.InsufficientCoinException;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.model.PublicModelTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class BardCharacterTest {

    Model model;

    @BeforeEach
    void setUp() {
        model = PublicModelTest.twoPlayersExpertMode();
        PublicModelTest.setupPlayCharacterCard(model, new BardCharacter(model));
    }

    @Test
    void emptyList() {
        try {
            model.publicModel.playCharacterCard(0, new BardCharacterArgument(
                    List.of(),
                    List.of()
            ));
        } catch (InsufficientCoinException | CCArgumentException e) {
            assert false;
        }
    }

    @Test
    void notMatchingSize() {
        try {
            model.publicModel.playCharacterCard(0, new BardCharacterArgument(
                    List.of(Color.BLUE_UNICORNS),
                    List.of()
            ));
            assert false;
        } catch (InsufficientCoinException e) {
            // Player should have the initial coin, which is enough
            assert false;
        } catch (CCArgumentException e) {
            assertEquals(BardCharacter.LIST_SIZE_NOT_MATCH, e.getMessage());
        }
    }

    @Test
    void tooBigSize() {
        try {
            model.publicModel.playCharacterCard(0, new BardCharacterArgument(
                    Arrays.asList(Color.BLUE_UNICORNS, Color.BLUE_UNICORNS, Color.BLUE_UNICORNS),
                    Arrays.asList(Color.BLUE_UNICORNS, Color.BLUE_UNICORNS, Color.BLUE_UNICORNS)
            ));
            assert false;
        } catch (InsufficientCoinException e) {
            // Player should have the initial coin, which is enough
            assert false;
        } catch (CCArgumentException e) {
            assertEquals(BardCharacter.LIST_SIZE_TOO_BIG, e.getMessage());
        }
    }

    @Test
    void normalExchange() {
        List<Student> entrance = model.publicModel.getCurrentPlayer().getBoard().getEntrance();
        List<List<Student>> diningRoom = model.publicModel.getCurrentPlayer().getBoard().getDiningRoom();

        Student entranceStud1 = entrance.get(0);
        Student entranceStud2 = entrance.get(1);

        Student diningStud1 = new Student(Color.GREEN_FROGS, 12345);
        Student diningStud2 = new Student(Color.RED_DRAGONS, 54321);

        diningRoom.get(diningStud1.getColor().ordinal()).add(diningStud1);
        diningRoom.get(diningStud2.getColor().ordinal()).add(diningStud2);

        int oldEntranceSize = entrance.size();
        try {
            model.publicModel.playCharacterCard(0, new BardCharacterArgument(
                    Arrays.asList(entranceStud1.getColor(), entranceStud2.getColor()),
                    Arrays.asList(diningStud1.getColor(), diningStud2.getColor())
            ));
        } catch (InsufficientCoinException | CCArgumentException e) {
            assert false;
        }

        assertEquals(oldEntranceSize, entrance.size());

        // Look for diningStud*, which where moved in entrance
        assertNotEquals(-1, entrance.indexOf(diningStud1));
        assertNotEquals(-1, entrance.indexOf(diningStud2));

        // Look for entranceStud*, which where moved in diningRoom
        assertNotEquals(-1, diningRoom.get(entranceStud1.getColor().ordinal()).indexOf(entranceStud1));
        assertNotEquals(-1, diningRoom.get(entranceStud2.getColor().ordinal()).indexOf(entranceStud2));
    }
}