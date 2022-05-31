package it.polimi.ingsw.cards.characters.BardCharacter;

import it.polimi.ingsw.cards.characters.AllCharacterTest;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.InsufficientCoinException;
import it.polimi.ingsw.mvc.model.PrivateModel;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.exceptions.DiningRoomFullException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BardCharacterTest extends AllCharacterTest {

    BardCharacterTest() {
        super(BardCharacter.class);
    }

    @Test
    void emptyList() {
        try {
            playCard(new BardCharacterArgument(
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
            playCard(new BardCharacterArgument(
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
            playCard(new BardCharacterArgument(
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
            playCard(new BardCharacterArgument(
                            Arrays.asList(entranceStud1.getColor(), entranceStud2.getColor()),
                            Arrays.asList(diningStud1.getColor(), diningStud2.getColor())
                    )
            );
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

    @Test
    void diningRoomFull() {
        Board board = model.publicModel.getCurrentPlayer().getBoard();
        List<Student> entrance = model.publicModel.getCurrentPlayer().getBoard().getEntrance();
        Student studentEntrance = new Student(Color.RED_DRAGONS, 54321);
        entrance.set(0, studentEntrance);

        for (int i = 0; i < Board.DINING_ROOM_MAX_STUDENT_COUNT; i++) {
            try {
                board.addStudentsToDiningRoom(new Student(Color.RED_DRAGONS, 12345 + i));
            } catch (DiningRoomFullException e) {
                assert false;
            }
        }

        Student studentDining = new Student(Color.YELLOW_GNOMES, 56789);
        assertDoesNotThrow(() -> board.addStudentsToDiningRoom(studentDining));

        try {
            playCard(new BardCharacterArgument(
                    List.of(studentEntrance.getColor()),
                    List.of(studentDining.getColor())
            ));
            assert false;
        } catch (InsufficientCoinException e) {
            assert false;
        } catch (CCArgumentException e) {
            assertEquals(BardCharacter.DINING_ROOM_FULL, e.getMessage());
        }
    }

    @Test
    void notInEntrance() {
        Board board = model.publicModel.getCurrentPlayer().getBoard();
        List<Student> entrance = board.getEntrance();
        entrance.clear();
        Student studInDining = new Student(Color.RED_DRAGONS, 12345);
        assertDoesNotThrow(() -> board.addStudentsToDiningRoom(studInDining));

        try {
            playCard(new BardCharacterArgument(
                    List.of(Color.RED_DRAGONS),
                    List.of(Color.RED_DRAGONS)
            ));
            assert false;
        } catch (InsufficientCoinException e) {
            assert false;
        } catch (CCArgumentException e) {
            assertEquals(BardCharacter.STUDENT_NOT_IN_ENTRANCE, e.getMessage());

            assertEquals(0, entrance.size());
            List<Student> diningRed = board.getDiningRoom().get(Color.RED_DRAGONS.ordinal());
            assertEquals(1, diningRed.size());
            assertEquals(studInDining, diningRed.get(0));
        }
    }

    @Test
    void notInDining() {
        Board board = model.publicModel.getCurrentPlayer().getBoard();
        List<Student> entrance = board.getEntrance();
        Student studInEntrance = new Student(Color.RED_DRAGONS, 12345);
        entrance.set(0, studInEntrance);

        try {
            playCard(new BardCharacterArgument(
                    List.of(Color.RED_DRAGONS),
                    List.of(Color.RED_DRAGONS)
            ));
            assert false;
        } catch (InsufficientCoinException e) {
            assert false;
        } catch (CCArgumentException e) {
            assertEquals(BardCharacter.STUDENT_NOT_IN_DINING, e.getMessage());
            assertEquals(PrivateModel.INITIAL_STUDENT_ENTRANCE_2_4, entrance.size());
            assertEquals(studInEntrance, entrance.get(0));

            for (List<Student> diningByColor : board.getDiningRoom()) {
                assertEquals(0, diningByColor.size());
            }
        }
    }
}