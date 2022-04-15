package it.polimi.ingsw.cards.characters.PrincessCharacter;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.cards.characters.AllCharacterTest;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.mvc.model.PublicModelTest;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.player.Board;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrincessCharacterTest extends AllCharacterTest {

    PrincessCharacterTest() {
        super(PrincessCharacter.class);
    }

    @Test
    void constructorWithEmptyBag() {
        PublicModelTest.setBag(model, new Bag(0));

        assertThrows(RuntimeException.class, () -> new PrincessCharacter(model));
    }

    @Test
    void getStudents() {
        PrincessCharacter princessCharacter = (PrincessCharacter) PublicModelTest.getCharCard(model);

        List<Student> students = princessCharacter.students;
        List<Student> studentsCopy = princessCharacter.getStudents();

        assertEquals(students, studentsCopy);

        studentsCopy.set(0, new Student(Color.GREEN_FROGS, 9876));

        assertNotEquals(students, studentsCopy);
    }

    /**
     * Play the card with a student color that was not into the card list
     */
    @Test
    void studentNotFound() {
        PrincessCharacter princessCharacter = (PrincessCharacter) PublicModelTest.getCharCard(model);

        List<Student> students = princessCharacter.students;
        students.clear();

        model.publicModel.getCurrentPlayer().getBoard().rewardCoin();
        assertThrows(CCArgumentException.class, () -> playCard(Color.RED_DRAGONS));
    }

    /**
     * 1. Fill red dining room
     * 2. Play card with red target color
     */
    @Test
    void diningRoomFull() {
        Board currentPlayerBoard = model.publicModel.getCurrentPlayer().getBoard();

        List<Student> redDiningRoom = currentPlayerBoard.getDiningRoom().get(Color.RED_DRAGONS.ordinal());

        assertEquals(0, redDiningRoom.size());

        // 1
        for (int i = 0; i < Board.DINING_ROOM_MAX_STUDENT_COUNT; i++) {
            redDiningRoom.add(new Student(Color.RED_DRAGONS, i + 12345));
        }

        PrincessCharacter princessCharacter = (PrincessCharacter) PublicModelTest.getCharCard(model);
        List<Student> princessStudents = princessCharacter.students;

        princessStudents.set(0, new Student(Color.RED_DRAGONS, 54321));

        List<Student> princessStudentsCopy = new ArrayList<>(princessStudents);
        List<Student> redDiningRoomCopy = new ArrayList<>(redDiningRoom);

        // 2
        model.publicModel.getCurrentPlayer().getBoard().rewardCoin();
        assertThrows(CCArgumentException.class, () -> playCard(Color.RED_DRAGONS));

        assertEquals(princessStudentsCopy, princessStudents);
        assertEquals(redDiningRoomCopy, redDiningRoom);
    }

    /**
     * @param expectedPrincessStudSizeAfter expected count of students in the princess card
     *                                      after activating the effect of the princess card
     *                                      1. Try to play card while the bag is empty
     *                                      2. Student was moved, check sizes
     */
    private void checkStudentMoved(int expectedPrincessStudSizeAfter) {
        PrincessCharacter princessCharacter = (PrincessCharacter) PublicModelTest.getCharCard(model);
        List<Student> princessStudents = princessCharacter.students;

        assertEquals(PrincessCharacter.INITIAL_STUDENT_SIZE, princessStudents.size());

        Color firstStudentColor = princessStudents.get(0).getColor();

        List<Student> diningRoomTargetColor = model.publicModel.getCurrentPlayer()
                .getBoard()
                .getDiningRoom()
                .get(firstStudentColor.ordinal());

        assertEquals(0, diningRoomTargetColor.size());
        // 1
        model.publicModel.getCurrentPlayer().getBoard().rewardCoin();
        assertDoesNotThrow(() -> playCard(firstStudentColor));

        // 2
        assertEquals(1, diningRoomTargetColor.size());
        assertEquals(expectedPrincessStudSizeAfter, princessStudents.size());
    }


    @Test
    void bagEmpty() {
        PublicModelTest.setBag(model, new Bag(0));
        checkStudentMoved(PrincessCharacter.INITIAL_STUDENT_SIZE - 1);
    }

    /**
     * 1. Play princess card and check that a student was moved
     */
    @Test
    void normalEffect() {
        checkStudentMoved(PrincessCharacter.INITIAL_STUDENT_SIZE);
    }
}