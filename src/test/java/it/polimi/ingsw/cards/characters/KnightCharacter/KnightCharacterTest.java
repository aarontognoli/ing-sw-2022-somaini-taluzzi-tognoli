package it.polimi.ingsw.cards.characters.KnightCharacter;

import it.polimi.ingsw.cards.characters.AllCharacterTest;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.exceptions.InsufficientCoinException;
import it.polimi.ingsw.exceptions.NoTowerException;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.mvc.model.DefaultInfluenceCalculatorRules;
import it.polimi.ingsw.mvc.model.InfluenceCalculator_2_3;
import it.polimi.ingsw.mvc.model.InfluenceCalculator_4;
import it.polimi.ingsw.mvc.model.PublicModelTest;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.DiningRoomFullException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KnightCharacterTest extends AllCharacterTest {
    public KnightCharacterTest() {
        super(KnightCharacter.class);
    }

    /**
     * Normal Behaviour tested by other methods.
     * 1. Make a Draw ( 2 and 2 )
     * 2. Use card on player 0 ( +2 influence ) and check that he's winning
     * 3. Increase player 1 influence, check that player 0 is still winning
     * 4. Increase player 1 influence, check Draw
     */
    @Test
    void knightTest() {
        //place two Player 0 students

        Board p0Board = model.publicModel.getCurrentPlayer().getBoard();

        List<Student> entrance0 = p0Board.getEntrance();
        entrance0.set(0, new Student(Color.YELLOW_GNOMES, 1));
        entrance0.set(1, new Student(Color.YELLOW_GNOMES, 2));
        entrance0.set(2, new Student(Color.YELLOW_GNOMES, 3));

        try {
            model.publicModel.moveStudentToDiningRoom(Color.YELLOW_GNOMES);
            model.publicModel.moveStudentToIsland(Color.YELLOW_GNOMES, 0);
            model.publicModel.moveStudentToIsland(Color.YELLOW_GNOMES, 0);
        } catch (NotFoundException | DiningRoomFullException e) {
            assert false;
        }

        // Place two students from the other player
        PublicModelTest.incrementCurrentPlayer(model);
        Board p1Board = model.publicModel.getCurrentPlayer().getBoard();
        List<Student> entrance1 = p1Board.getEntrance();
        entrance1.set(0, new Student(Color.RED_DRAGONS, 4));
        entrance1.set(1, new Student(Color.RED_DRAGONS, 5));
        entrance1.set(2, new Student(Color.RED_DRAGONS, 6));

        try {
            model.publicModel.moveStudentToDiningRoom(Color.RED_DRAGONS);
            model.publicModel.moveStudentToIsland(Color.RED_DRAGONS, 0);
            model.publicModel.moveStudentToIsland(Color.RED_DRAGONS, 0);
        } catch (NotFoundException | DiningRoomFullException e) {
            assert false;
        }
        Island targetIsland = model.publicModel.getMotherNatureIsland();
        model.publicModel.updateIslandOwner(targetIsland);
        assertThrows(NoTowerException.class, targetIsland::getTowerColor);//draw
        assertEquals(0, targetIsland.getTowers().size());

        // Use knightCharacter on player 0
        PublicModelTest.incrementCurrentPlayer(model);
        try {
            p0Board.rewardCoin();
            p0Board.rewardCoin();
            playCard((int) 0);//redundant cast but it's to test int and Integer istanceof
            assertEquals(1, p0Board.getCoinCount());
        } catch (InsufficientCoinException | CCArgumentException e) {
            assert false;
        }

        model.publicModel.updateIslandOwner(targetIsland);

        try {
            assertEquals(TowerColor.values()[0], targetIsland.getTowerColor());
            assertEquals(1, targetIsland.getTowers().size());
        } catch (NoTowerException e) {
            assert false;
        }
        assertEquals(InfluenceCalculator_2_3.class, model.characterModel.getInfluenceCalculator().getClass());
        model.characterModel.resetInfluenceCalculatorRules();
        //increase player 1 influence by two
        PublicModelTest.incrementCurrentPlayer(model);
        entrance1.set(0, new Student(Color.RED_DRAGONS, 7));
        entrance1.set(1, new Student(Color.RED_DRAGONS, 8));
        entrance1.set(2, new Student(Color.RED_DRAGONS, 9));

        try {
            model.publicModel.moveStudentToIsland(Color.RED_DRAGONS, 0);
            model.publicModel.moveStudentToIsland(Color.RED_DRAGONS, 0);
        } catch (NotFoundException e) {
            assert false;
        }
        //check player 1 victory then play card and check draw

        model.publicModel.updateIslandOwner(targetIsland);
        try {
            assertEquals(TowerColor.values()[1], targetIsland.getTowerColor());
            assertEquals(1, targetIsland.getTowers().size());
        } catch (NoTowerException e) {
            assert false;
        }
        PublicModelTest.incrementCurrentPlayer(model);
        try {
            p0Board.rewardCoin();
            p0Board.rewardCoin();
            assertEquals(3, p0Board.getCoinCount());
            playCard((Integer) 0);//redundant cast but it's to test int and Integer istanceof
            assertEquals(0, p0Board.getCoinCount());
        } catch (InsufficientCoinException | CCArgumentException e) {
            assert false;
        }
        model.publicModel.updateIslandOwner(targetIsland);
        try {
            assertEquals(TowerColor.values()[1], targetIsland.getTowerColor());
            assertEquals(1, targetIsland.getTowers().size());
        } catch (NoTowerException e) {
            assert false;
        }


    }
}
