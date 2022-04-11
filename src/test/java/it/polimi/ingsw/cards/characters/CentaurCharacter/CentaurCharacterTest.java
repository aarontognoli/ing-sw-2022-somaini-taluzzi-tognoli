package it.polimi.ingsw.cards.characters.CentaurCharacter;

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
import it.polimi.ingsw.player.DiningRoomFullException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CentaurCharacterTest extends AllCharacterTest {

    CentaurCharacterTest() {
        super(CentaurCharacter.class);
    }

    /**
     * 1. Place player 1 tower in island 0
     * 2. Place an amount of students so that player 1 and 2 have a draw
     * 3. Activate card so that the influence of player 1 decrease by 1 (no influence of tower)
     * 4. Calculate influence, now player 2 should be the owner
     */
    @Test
    void normalTest() {
        Board currentBoard = model.publicModel.getCurrentPlayer().getBoard();

        List<Student> entrance = currentBoard.getEntrance();

        entrance.set(0, new Student(Color.RED_DRAGONS, 12345));
        entrance.set(1, new Student(Color.RED_DRAGONS, 22345));

        try {
            model.publicModel.moveStudentToDiningRoom(Color.RED_DRAGONS);
            model.publicModel.moveStudentToIsland(Color.RED_DRAGONS, 0);
        } catch (NotFoundException | DiningRoomFullException e) {
            assert false;
        }

        model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());
        try {
            Island targetIsland = model.publicModel.getMotherNatureIsland();
            assertEquals(TowerColor.values()[0], targetIsland.getTowerColor());
            assertEquals(1, targetIsland.getTowers().size());
        } catch (NoTowerException e) {
            assert false;
        }

        // Now add the students from the other player
        PublicModelTest.incrementCurrentPlayer(model);
        currentBoard = model.publicModel.getCurrentPlayer().getBoard();
        entrance = currentBoard.getEntrance();
        entrance.set(0, new Student(Color.YELLOW_GNOMES, 32345));
        entrance.set(1, new Student(Color.YELLOW_GNOMES, 42345));
        entrance.set(2, new Student(Color.YELLOW_GNOMES, 52345));

        try {
            model.publicModel.moveStudentToDiningRoom(Color.YELLOW_GNOMES);
            model.publicModel.moveStudentToIsland(Color.YELLOW_GNOMES, 0);
            model.publicModel.moveStudentToIsland(Color.YELLOW_GNOMES, 0);
        } catch (NotFoundException | DiningRoomFullException e) {
            assert false;
        }

        // Nothing have changed
        model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());
        try {
            Island targetIsland = model.publicModel.getMotherNatureIsland();
            assertEquals(TowerColor.values()[0], targetIsland.getTowerColor());
            assertEquals(1, targetIsland.getTowers().size());
        } catch (NoTowerException e) {
            assert false;
        }

        // Now play card and check that the owner changed
        try {
            playCard(null);
        } catch (InsufficientCoinException | CCArgumentException e) {
            assert false;
        }

        model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());
        try {
            Island targetIsland = model.publicModel.getMotherNatureIsland();
            assertEquals(TowerColor.values()[1], targetIsland.getTowerColor());
            assertEquals(1, targetIsland.getTowers().size());
        } catch (NoTowerException e) {
            assert false;
        }
    }
}