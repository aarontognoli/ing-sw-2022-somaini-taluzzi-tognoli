package it.polimi.ingsw.cards.characters.HerbalistCharacter;

import it.polimi.ingsw.cards.characters.AllCharacterTest;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.exceptions.InsufficientCoinException;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.mvc.model.PublicModelTest;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.DiningRoomFullException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HerbalistCharacterTest extends AllCharacterTest {
    HerbalistCharacterTest() {
        super(HerbalistCharacter.class);
    }

    private void checkOwner(Island islandZero) {
        assertEquals(1, islandZero.getTowers().size());
        assertEquals(TowerColor.values()[0], islandZero.getTowers().get(0).getColor());
    }

    @Test
    void restoringWhileNoActiveEntryTile() {
        HerbalistCharacter card;
        try {
            card = PublicModelTest.findHerbalist(model);
        } catch (NotFoundException e) {
            assert false;
            return;
        }

        assertThrows(RuntimeException.class, card::moveEntryTileBackToCard);
    }

    @Test
    void tooMuchNoEntryTileActivations() {
        List<Island> islands = PublicModelTest.getIslands(model);

        Board currentPlayerBoard = model.publicModel.getCurrentPlayer().getBoard();

        for (int i = 0; i < 19; i++)
            currentPlayerBoard.rewardCoin();

        for (int i = 0; i < 4; i++) {
            int finalI = i;
            assertDoesNotThrow(() -> playCard(islands.get(finalI)));
        }

        assertThrows(CCArgumentException.class, () -> playCard(islands.get(4)));
        assertEquals(6, currentPlayerBoard.getCoinCount());
    }

    @Test
    void twoEffectsOnSameIsland() {
        Island targetIsland = model.publicModel.getMotherNatureIsland();
        Board currentPlayerBoard = model.publicModel.getCurrentPlayer().getBoard();
        for (int i = 0; i < 4; i++) {
            currentPlayerBoard.rewardCoin();
        }

        assertDoesNotThrow(() -> playCard(targetIsland));
        assertTrue(targetIsland.hasNoEntryTile());

        assertThrows(CCArgumentException.class, () -> playCard(targetIsland));
        assertTrue(targetIsland.hasNoEntryTile());
        assertEquals(3, currentPlayerBoard.getCoinCount());
    }

    /**
     * 1. Player 1 adds a RED Student to their dining room and to island 0
     * 2. Artificially update island 0 owner
     * 3. Player 1 is the owner of island 0
     * 4. Player 2 adds a YELLOW Student to their dining room and 3 to island 0
     * 5. Play HerbalistCharacter on island 0
     * 6. Artificially update island 0 owner
     * 7. The owner of that island does not change, no entry tile is removed from that island
     */
    @Test
    void normalEffect() {
        HerbalistCharacter herbalistCharacter;
        try {
            herbalistCharacter = PublicModelTest.findHerbalist(model);
        } catch (NotFoundException e) {
            assert false;
            return;
        }

        assertEquals(0, herbalistCharacter.entryTilesInIslandCount);

        Island islandZero = model.publicModel.getMotherNatureIsland();

        Board player1Board = model.publicModel.getCurrentPlayer().getBoard();

        List<Student> entrance = player1Board.getEntrance();

        entrance.set(0, new Student(Color.RED_DRAGONS, 12345));
        entrance.set(1, new Student(Color.RED_DRAGONS, 22345));

        try {
            // 1
            model.publicModel.moveStudentToDiningRoom(Color.RED_DRAGONS);
            model.publicModel.moveStudentToIsland(Color.RED_DRAGONS, 0);
        } catch (DiningRoomFullException | NotFoundException e) {
            assert false;
        }

        // 2
        model.publicModel.updateIslandOwner(islandZero);

        // 3
        checkOwner(islandZero);
        assertEquals(1, islandZero.getTowers().size());
        assertEquals(TowerColor.values()[0], islandZero.getTowers().get(0).getColor());

        PublicModelTest.incrementCurrentPlayer(model);

        Board secondPlayerBoard = model.publicModel.getCurrentPlayer().getBoard();
        entrance = secondPlayerBoard.getEntrance();

        entrance.set(0, new Student(Color.YELLOW_GNOMES, 32345));
        entrance.set(1, new Student(Color.YELLOW_GNOMES, 32345));
        entrance.set(2, new Student(Color.YELLOW_GNOMES, 32345));
        entrance.set(3, new Student(Color.YELLOW_GNOMES, 32345));

        try {
            // 4
            model.publicModel.moveStudentToDiningRoom(Color.YELLOW_GNOMES);
            model.publicModel.moveStudentToIsland(Color.YELLOW_GNOMES, 0);
            model.publicModel.moveStudentToIsland(Color.YELLOW_GNOMES, 0);
            model.publicModel.moveStudentToIsland(Color.YELLOW_GNOMES, 0);
        } catch (DiningRoomFullException | NotFoundException e) {
            assert false;
        }

        try {
            // 5
            secondPlayerBoard.rewardCoin();
            playCard(islandZero);
        } catch (InsufficientCoinException | CCArgumentException e) {
            assert false;
        }
        assertEquals(1, herbalistCharacter.entryTilesInIslandCount);

        assertTrue(islandZero.hasNoEntryTile());

        // 6
        model.publicModel.updateIslandOwner(islandZero);
        assertEquals(0, herbalistCharacter.entryTilesInIslandCount);
        assertFalse(islandZero.hasNoEntryTile());

        // 7
        checkOwner(islandZero);
    }
}