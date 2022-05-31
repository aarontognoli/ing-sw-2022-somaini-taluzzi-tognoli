package it.polimi.ingsw.cards.characters.PipeCharacterTest;

import it.polimi.ingsw.cards.characters.AllCharacterTest;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.PipeCharacter.PipeCharacter;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.InsufficientCoinException;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.mvc.model.PublicModelTest;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.exceptions.DiningRoomFullException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PipeCharacterTest extends AllCharacterTest {
    public PipeCharacterTest() {
        super(PipeCharacter.class);
    }

    /**
     * 1. Add 2 red students and 1 blue student to player 0 dining rooms
     * 2. Add 4 red students to player 1 dining room
     * 3. Play Card on red color
     * 4. Check 1 blue remaining on player 0 and one red remaining on player 1 in dining rooms
     */
    @Test
    void pipeTest() {
        //two red and one blue in p0
        Board p0Board = model.publicModel.getCurrentPlayer().getBoard();

        List<Student> entrance0 = p0Board.getEntrance();
        entrance0.set(0, new Student(Color.RED_DRAGONS, 1));
        entrance0.set(1, new Student(Color.RED_DRAGONS, 2));
        entrance0.set(2, new Student(Color.BLUE_UNICORNS, 3));

        try {
            model.publicModel.moveStudentToDiningRoom(Color.RED_DRAGONS);
            model.publicModel.moveStudentToDiningRoom(Color.RED_DRAGONS);
            model.publicModel.moveStudentToDiningRoom(Color.BLUE_UNICORNS);

        } catch (NotFoundException | DiningRoomFullException e) {
            assert false;
        }
        // Place 4 red students on p1
        PublicModelTest.incrementCurrentPlayer(model);
        Board p1Board = model.publicModel.getCurrentPlayer().getBoard();
        List<Student> entrance1 = p1Board.getEntrance();
        entrance1.set(0, new Student(Color.RED_DRAGONS, 4));
        entrance1.set(1, new Student(Color.RED_DRAGONS, 5));
        entrance1.set(2, new Student(Color.RED_DRAGONS, 6));
        entrance1.set(3, new Student(Color.RED_DRAGONS, 7));

        try {
            model.publicModel.moveStudentToDiningRoom(Color.RED_DRAGONS);
            model.publicModel.moveStudentToDiningRoom(Color.RED_DRAGONS);
            assertEquals(1, p1Board.getCoinCount());
            model.publicModel.moveStudentToDiningRoom(Color.RED_DRAGONS);//coin  reward
            assertEquals(2, p1Board.getCoinCount());
            model.publicModel.moveStudentToDiningRoom(Color.RED_DRAGONS);

        } catch (NotFoundException | DiningRoomFullException e) {
            assert false;
        }
        //doesn't care who uses the card
        PublicModelTest.incrementCurrentPlayer(model);
        try {
            p0Board.rewardCoin();
            p0Board.rewardCoin();
            playCard(Color.RED_DRAGONS);
            assertEquals(0, p0Board.getCoinCount());
        } catch (InsufficientCoinException | CCArgumentException e) {
            assert false;
        }

        for (Color c : Color.values()) {

            if (c.equals(Color.BLUE_UNICORNS))
                assertEquals(1, p0Board.getDiningRoom().get(c.ordinal()).size());
            else
                assertEquals(0, p0Board.getDiningRoom().get(c.ordinal()).size());

            if (c.equals(Color.RED_DRAGONS))
                assertEquals(1, p1Board.getDiningRoom().get(c.ordinal()).size());
            else
                assertEquals(0, p1Board.getDiningRoom().get(c.ordinal()).size());
        }
    }
}
