package it.polimi.ingsw.cards.characters.JokerCharacter;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.cards.characters.AllCharacterTest;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.mvc.model.PublicModelTest;
import it.polimi.ingsw.pawn.Student;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class JokerCharacterTest extends AllCharacterTest {
    JokerCharacterTest() {
        super(JokerCharacter.class);
    }

    @Test
    void getJokerStudents() {
        JokerCharacter joker = (JokerCharacter) PublicModelTest.getCharCard(model);

        List<Student> internal = joker.jokerStudents;
        List<Student> copy = joker.getJokerStudents();

        assertEquals(copy, internal);

        copy.set(0, null);

        assertNotEquals(internal.get(0), copy.get(0));
    }

    @Test
    void notMatchingSize() {
        assertThrows(CCArgumentException.class, () -> playCard(new JokerCharacterArgument(
                List.of(),
                List.of(Color.YELLOW_GNOMES)
        )));
    }

    @Test
    void sizeTooBig() {
        assertThrows(CCArgumentException.class, () -> playCard(new JokerCharacterArgument(
                List.of(Color.YELLOW_GNOMES, Color.YELLOW_GNOMES, Color.YELLOW_GNOMES, Color.YELLOW_GNOMES),
                List.of(Color.YELLOW_GNOMES, Color.YELLOW_GNOMES, Color.YELLOW_GNOMES, Color.YELLOW_GNOMES)
        )));
    }

    @Test
    void emptyBag() {
        assertThrows(RuntimeException.class, () -> {
            PublicModelTest.setBag(model, new Bag(0));
            new JokerCharacter(model);
        });
    }

    @Test
    void notFoundInEntrance() {
        List<Student> entrance = model.publicModel.getCurrentPlayer().getBoard().getEntrance();
        entrance.clear();

        assertThrows(CCArgumentException.class, () -> playCard(new JokerCharacterArgument(
                List.of(Color.GREEN_FROGS),
                List.of(Color.GREEN_FROGS)
        )));
    }

    @Test
    void notFoundInJoker() {
        List<Student> entrance = model.publicModel.getCurrentPlayer().getBoard().getEntrance();
        entrance.set(0, new Student(Color.GREEN_FROGS, 12345));

        JokerCharacter joker = (JokerCharacter) PublicModelTest.getCharCard(model);
        joker.jokerStudents.clear();

        assertThrows(CCArgumentException.class, () -> playCard(new JokerCharacterArgument(
                List.of(Color.GREEN_FROGS),
                List.of(Color.GREEN_FROGS)
        )));
    }

    @Test
    void normalEffect() {
        List<Student> entrance = model.publicModel.getCurrentPlayer().getBoard().getEntrance();

        Student entranceStu1 = new Student(Color.RED_DRAGONS, 12345);
        Student entranceStu2 = new Student(Color.YELLOW_GNOMES, 22345);
        entrance.set(0, entranceStu1);
        entrance.set(1, entranceStu2);

        JokerCharacter joker = (JokerCharacter) PublicModelTest.getCharCard(model);

        Student jokerStu1 = new Student(Color.GREEN_FROGS, 32345);
        Student jokerStu2 = new Student(Color.BLUE_UNICORNS, 42345);
        joker.jokerStudents.set(0, jokerStu1);
        joker.jokerStudents.set(1, jokerStu2);

        assertDoesNotThrow(() -> playCard(new JokerCharacterArgument(
                List.of(jokerStu1.getColor(), jokerStu2.getColor()),
                List.of(entranceStu1.getColor(), entranceStu2.getColor())
        )));

        assertEquals(entranceStu1, joker.jokerStudents.get(0));
        assertEquals(entranceStu2, joker.jokerStudents.get(1));
        assertEquals(jokerStu1, entrance.get(0));
        assertEquals(jokerStu2, entrance.get(1));
    }
}