package it.polimi.ingsw.cards.characters.MushroomCharacter;

import it.polimi.ingsw.cards.characters.AllCharacterTest;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.exceptions.InsufficientCoinException;
import it.polimi.ingsw.exceptions.NoTowerException;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.mvc.model.PublicModelTest;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.exceptions.DiningRoomFullException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MushroomCharacterTest extends AllCharacterTest {
    public MushroomCharacterTest() {
        super(MushroomCharacter.class);
    }

    /**
     * Normal Behaviour tested by other methods.
     * 1. Make a Draw ( 2 and 2 )
     * 2. use card on not present color and check draw
     * 3. Use card on player 0 student color
     * 4. Check Player 1 Victory
     */
    @Test
    void mushroomTest() {
        //place two Player 0 students YELLOW

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

        // Place two students from the other player RED
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

        //use card on color blue
        PublicModelTest.incrementCurrentPlayer(model);
        try {
            p0Board.rewardCoin();
            playCard(Color.BLUE_UNICORNS);
            assertEquals(0, p0Board.getCoinCount());
        } catch (InsufficientCoinException | CCArgumentException e) {
            assert false;
        }

        model.publicModel.updateIslandOwner(targetIsland);
        assertThrows(NoTowerException.class, targetIsland::getTowerColor);//draw
        assertEquals(0, targetIsland.getTowers().size());

        //use card on color yellow
        try {
            p0Board.rewardCoin();
            p0Board.rewardCoin();
            p0Board.rewardCoin();
            playCard(Color.YELLOW_GNOMES);
            assertEquals(0, p0Board.getCoinCount());
        } catch (InsufficientCoinException | CCArgumentException e) {
            assert false;
        }
        //check player 1 victory
        model.publicModel.updateIslandOwner(targetIsland);

        try {
            assertEquals(TowerColor.values()[1], targetIsland.getTowerColor());
            assertEquals(1, targetIsland.getTowers().size());
        } catch (NoTowerException e) {
            assert false;
        }
    }
}
