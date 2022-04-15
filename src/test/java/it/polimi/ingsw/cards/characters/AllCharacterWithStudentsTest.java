package it.polimi.ingsw.cards.characters;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.model.PublicModelTest;
import it.polimi.ingsw.pawn.Student;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AllCharacterWithStudentsTest extends AllCharacterTest {

    protected AllCharacterWithStudentsTest(Class<? extends CharacterCard> cardClass) {
        super(cardClass);
    }

    @Test
    void constructorWithEmptyBag() {
        PublicModelTest.setBag(model, new Bag(0));

        try {
            characterCardClass.getDeclaredConstructor(Model.class).newInstance(model);
            assert false;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | NoSuchMethodException | SecurityException e) {
            assert false;
        } catch (InvocationTargetException e) {
            assertInstanceOf(RuntimeException.class, e.getTargetException());

            RuntimeException runtimeException = (RuntimeException) e.getTargetException();
            assertEquals(CharacterCardWithStudents.CANNOT_DRAW_FROM_BAG, runtimeException.getMessage());
        }
    }

    /**
     * Check that `getStudents()` returns a deep copy
     */
    @Test
    void getStudents() {
        CharacterCardWithStudents princessCharacter = (CharacterCardWithStudents) PublicModelTest.getCharCard(model);

        List<Student> studentsCopy0 = princessCharacter.getStudents();
        List<Student> studentsCopy1 = princessCharacter.getStudents();

        assertEquals(studentsCopy0, studentsCopy1);

        studentsCopy1.set(0, new Student(Color.GREEN_FROGS, 9876));

        assertNotEquals(studentsCopy0, studentsCopy1);
        assertEquals(studentsCopy0, princessCharacter.getStudents());
    }
}
