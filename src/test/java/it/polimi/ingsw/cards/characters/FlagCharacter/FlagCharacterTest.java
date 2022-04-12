package it.polimi.ingsw.cards.characters.FlagCharacter;

import it.polimi.ingsw.cards.characters.AllCharacterTest;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.exceptions.InsufficientCoinException;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.DiningRoomFullException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Since the card just calls updateIslandOwner, the in depth testing should be in PublicModelTest
 */
class FlagCharacterTest extends AllCharacterTest {

    FlagCharacterTest() {
        super(FlagCharacter.class);
    }

    /**
     * 1. Adding a RED Student to dining room
     * 2. Adding a RED student to island 0
     * 3. Island has still no towers
     * 4. Activate FlagCharacter on island 0
     * 5. Island has one first players' tower in it
     */
    @Test
    void normalEffect() {
        Board currentPlayerBoard = model.publicModel.getCurrentPlayer().getBoard();

        List<Student> entrance = currentPlayerBoard.getEntrance();

        entrance.set(0, new Student(Color.RED_DRAGONS, 12345));
        entrance.set(1, new Student(Color.RED_DRAGONS, 22345));

        try {
            // 1
            model.publicModel.moveStudentToDiningRoom(Color.RED_DRAGONS);
            // 2
            model.publicModel.moveStudentToIsland(Color.RED_DRAGONS, 0);
        } catch (DiningRoomFullException | NotFoundException e) {
            assert false;
        }

        // 3
        assertEquals(0, model.publicModel.getMotherNatureIsland().getTowers().size());

        try {
            // 4
            currentPlayerBoard.rewardCoin();
            currentPlayerBoard.rewardCoin();
            playCard(model.publicModel.getMotherNatureIsland());
        } catch (InsufficientCoinException | CCArgumentException e) {
            assert false;
        }

        // 5
        assertEquals(1, model.publicModel.getMotherNatureIsland().getTowers().size());
        assertEquals(TowerColor.values()[0], model.publicModel.getMotherNatureIsland().getTowers().get(0).getColor());
    }
}