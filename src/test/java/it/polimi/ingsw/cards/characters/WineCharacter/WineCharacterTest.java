package it.polimi.ingsw.cards.characters.WineCharacter;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.cards.characters.AllCharacterWithStudentsTest;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.model.PublicModelTest;
import it.polimi.ingsw.pawn.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WineCharacterTest extends AllCharacterWithStudentsTest {

    WineCharacterTest() {
        super(WineCharacter.class);
    }


    @Test
    void invalidIslandIndex() {
        assertThrows(CCArgumentException.class, () -> playCard(new WineCharacterArgument(
                -1,
                null
        )));

        assertThrows(CCArgumentException.class, () -> playCard(new WineCharacterArgument(
                Model.TOTAL_ISLANDS_NUMBER,
                null
        )));
    }

    @Test
    void studentNotFound() {
        WineCharacter wineCharacter = (WineCharacter) PublicModelTest.getCharCard(model);
        wineCharacter.getStudentsListReference().clear();

        assertThrows(CCArgumentException.class, () -> playCard(new WineCharacterArgument(
                0,
                Color.RED_DRAGONS
        )));
    }

    /**
     * 1. Move a student to island 0
     * 2. Check there is that student on island 0
     * 3. Check size of students on wine card
     *
     * @param expectedSizeOnWine expected count of students after the wine effect has been activated
     */
    private void checkStudentMoved(int expectedSizeOnWine) {
        WineCharacter wineCharacter = (WineCharacter) PublicModelTest.getCharCard(model);

        assertEquals(0, model.publicModel.getMotherNatureIsland().getStudents().size());

        Student firstStudent = wineCharacter.getStudentsListReference().get(0);
        assertDoesNotThrow(() -> playCard(new WineCharacterArgument(
                0,
                firstStudent.getColor()
        )));

        assertEquals(1, model.publicModel.getMotherNatureIsland().getStudents().size());
        assertEquals(firstStudent, model.publicModel.getMotherNatureIsland().getStudents().get(0));

        assertEquals(expectedSizeOnWine, wineCharacter.getStudentsListReference().size());
    }

    @Test
    void bagEmptyDuringEffect() {
        PublicModelTest.setBag(model, new Bag(0));

        checkStudentMoved(WineCharacter.INITIAL_STUDENT_SIZE - 1);
    }

    @Test
    void normalEffect() {
        checkStudentMoved(WineCharacter.INITIAL_STUDENT_SIZE);
    }
}