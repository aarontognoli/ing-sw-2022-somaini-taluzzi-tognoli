package it.polimi.ingsw.cards.characters.CheeseCharacter;

import it.polimi.ingsw.cards.characters.AllCharacterTest;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.InsufficientCoinException;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.mvc.model.PublicModelTest;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.DiningRoomFullException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CheeseCharacterTest extends AllCharacterTest {

    CheeseCharacterTest() {
        super(CheeseCharacter.class);
    }

    /**
     * 1. Player 1 adds a RED student to their dining room, gaining RED professor
     * 2. Player 2 adds a RED student to their dining room, they do not gain the professor
     * 3. Player 1 adds a YELLOW student to their dining room, gaining YELLOW professor
     * 4. Player 2 activates CheeseCharacter
     * 5. Player 2 adds a YELLOW student to their dining room, gaining YELLOW professor
     */
    @Test
    void normalEffect() {

        Board player1Board = model.publicModel.getCurrentPlayer().getBoard();

        List<Student> currentPlayerEntrance = player1Board.getEntrance();

        currentPlayerEntrance.set(0, new Student(Color.RED_DRAGONS, 12345));
        currentPlayerEntrance.set(1, new Student(Color.YELLOW_GNOMES, 22345));

        try {
            // 1
            model.publicModel.moveStudentToDiningRoom(Color.RED_DRAGONS);
        } catch (DiningRoomFullException | NotFoundException e) {
            assert false;
        }

        assertEquals(PublicModelTest.getProfessorOwner(model, Color.RED_DRAGONS), player1Board);

        PublicModelTest.incrementCurrentPlayer(model);

        Board player2Board = model.publicModel.getCurrentPlayer().getBoard();
        currentPlayerEntrance = player2Board.getEntrance();

        currentPlayerEntrance.set(0, new Student(Color.RED_DRAGONS, 32345));
        currentPlayerEntrance.set(1, new Student(Color.YELLOW_GNOMES, 42345));

        try {
            // 2
            model.publicModel.moveStudentToDiningRoom(Color.RED_DRAGONS);
        } catch (DiningRoomFullException | NotFoundException e) {
            assert false;
        }

        assertEquals(PublicModelTest.getProfessorOwner(model, Color.RED_DRAGONS), player1Board);

        PublicModelTest.incrementCurrentPlayer(model);
        try {
            // 3
            model.publicModel.moveStudentToDiningRoom(Color.YELLOW_GNOMES);
        } catch (DiningRoomFullException | NotFoundException e) {
            assert false;
        }

        assertEquals(PublicModelTest.getProfessorOwner(model, Color.YELLOW_GNOMES), player1Board);

        PublicModelTest.incrementCurrentPlayer(model);

        try {
            // 4
            model.publicModel.getCurrentPlayer().getBoard().rewardCoin();
            playCard(model.publicModel.getCurrentPlayer().getNickname());
        } catch (InsufficientCoinException | CCArgumentException e) {
            assert false;
        }

        try {
            // 5
            model.publicModel.moveStudentToDiningRoom(Color.YELLOW_GNOMES);
        } catch (DiningRoomFullException | NotFoundException e) {
            assert false;
        }

        assertEquals(PublicModelTest.getProfessorOwner(model, Color.YELLOW_GNOMES), player2Board);
    }

}