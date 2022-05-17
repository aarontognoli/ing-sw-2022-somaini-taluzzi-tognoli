package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.BoardNotInGameException;
import it.polimi.ingsw.exceptions.EntranceFullException;
import it.polimi.ingsw.exceptions.NoTowerException;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.DiningRoomFullException;
import it.polimi.ingsw.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrivateModelTest {

    @Test
    void getProfessorOwner() {
        Model model = PublicModelTest.twoPlayersBasicSetup();
        model.currentPlayer = model.players.get(0);
        Board testBoard;
        for (Color c : Color.values())
            assertNull(model.privateModel.getProfessorOwner(c));

        Color studentColor = model.currentPlayer.getBoard().getEntrance().get(0).getColor();
        try {
            model.publicModel.moveStudentToDiningRoom(studentColor);
        } catch (DiningRoomFullException | NotFoundException e) {
            assert false;
        }
        for (Color c : Color.values()) {
            if (c.equals(studentColor)) {
                testBoard = model.currentPlayer.getBoard();
            } else {
                testBoard = null;
            }
            assertEquals(testBoard, model.privateModel.getProfessorOwner(c));
        }
    }

    @Test
    void removeStudentFromEntrance() {

        Model model = PublicModelTest.twoPlayersBasicSetup();
        Board testBoard = new Board();
        List<Student> tempEntrance = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            tempEntrance.add(new Student(Color.YELLOW_GNOMES, i));
        }

        tempEntrance.add(new Student(Color.RED_DRAGONS, 7));

        // Entrance is empty. Exception expected.
        assertThrows(NotFoundException.class,
                () -> model.privateModel.removeStudentFromEntrance(Color.GREEN_FROGS, testBoard));


        // Entrance contains only Yellows, exception expected
        try {
            testBoard.addStudentsToEntrance(tempEntrance);
        } catch (EntranceFullException e1) {
            assert false;
        }
        assertThrows(NotFoundException.class,
                () -> model.privateModel.removeStudentFromEntrance(Color.GREEN_FROGS, testBoard));

        // Trying to remove a yellow, check entrance size
        try {
            Student s = model.privateModel.removeStudentFromEntrance(Color.YELLOW_GNOMES, testBoard);
            assertEquals(s.getColor(), Color.YELLOW_GNOMES);
            assertEquals(testBoard.getEntrance().size(), 7);

            boolean redFound = false;
            for (Student student : testBoard.getEntrance()) {
                if (student.getColor().equals(Color.RED_DRAGONS)) {
                    assertEquals(7, student.getID());
                    redFound = true;
                }
            }
            assertTrue(redFound);
        } catch (NotFoundException e) {
            assert false;
        }
    }

    @Test
    void addStudentToIsland() {
        Model model = PublicModelTest.twoPlayersBasicSetup();
        Island island = new Island();
        model.privateModel.addStudentToIsland(new Student(Color.YELLOW_GNOMES, 0), island);
        assertEquals(island.getStudents().size(), 1);
        assertEquals(island.getStudents().get(0).getID(), 0);
        assertEquals(island.getStudents().get(0).getColor(), Color.YELLOW_GNOMES);

    }

    @Test
    void addStudentToDiningRoom() {
        Model model = PublicModelTest.twoPlayersBasicSetup();
        Board testBoard = new Board();
        try {
            model.privateModel.addStudentToDiningRoom(new Student(Color.GREEN_FROGS, 0), testBoard);
            for (Color c : Color.values()) {
                int test;
                if (c.equals(Color.GREEN_FROGS))
                    test = 1;
                else
                    test = 0;
                assertEquals(testBoard.getDiningRoom().get(c.ordinal()).size(), test);
            }

        } catch (DiningRoomFullException e) {
            assert false;
        }
        try { // 10 = DINING_ROOM_MAX_STUDENT_COUNT
            for (int i = 0; i < 10; i++) {
                model.privateModel.addStudentToDiningRoom(new Student(Color.YELLOW_GNOMES, i + 1), testBoard);
            }

        } catch (DiningRoomFullException e) {
            assert false;
        }

        assertThrows(DiningRoomFullException.class,
                () -> model.privateModel.addStudentToDiningRoom(new Student(Color.YELLOW_GNOMES, 12), testBoard));
    }

    @Test
    void getInfluenceTwoPlayers() {
        // TODO: Check 3 player, 4 players, and tie in both cases
        Model model = PublicModelTest.twoPlayersBasicSetup();

        Island targetIsland = model.islands.get(0);

        Player firstPlayer = model.players.get(0);

        model.currentPlayer = firstPlayer;
        Color firstPlayerFirstStudentColor = firstPlayer.getBoard().getEntrance().get(0).getColor();
        try {
            model.publicModel.moveStudentToDiningRoom(firstPlayerFirstStudentColor);
        } catch (DiningRoomFullException | NotFoundException e) {
            assert false;
        }

        assertEquals(firstPlayer.getBoard(),
                model.professors.get(firstPlayerFirstStudentColor.ordinal()).getPosition());

        targetIsland.addStudent(new Student(firstPlayerFirstStudentColor, 3456));

        assertEquals(firstPlayer.getBoard(), model.privateModel.getInfluence(targetIsland));
    }

    @Test
    void removeAllTowers() {
        Model model = PublicModelTest.twoPlayersBasicSetup();
        Board board0 = model.players.get(0).getBoard();

        Island targetIsland = model.publicModel.getMotherNatureIsland();
        assertEquals(0, targetIsland.getTowers().size());

        model.privateModel.removeAllTowers(targetIsland);

        assertEquals(0, targetIsland.getTowers().size());

        try {
            for (int i = 0; i < 8; i++) {
                targetIsland.addTower(board0.removeTower());
            }
        } catch (Exception e) {
            assert false;
        }

        assertEquals(0, board0.getTowers().size());
        assertEquals(8, targetIsland.getTowers().size());

        model.privateModel.removeAllTowers(targetIsland);

        assertEquals(8, board0.getTowers().size());
        assertEquals(0, targetIsland.getTowers().size());
        //Four Players
        model = PublicModelTest.fourPlayersBasicSetup();
        Board board3 = model.players.get(0).getBoard();

        targetIsland = model.publicModel.getMotherNatureIsland();
        assertEquals(0, targetIsland.getTowers().size());

        model.privateModel.removeAllTowers(targetIsland);

        assertEquals(0, targetIsland.getTowers().size());

        try {
            for (int i = 0; i < 8; i++) {
                targetIsland.addTower(board3.removeTower());
            }
        } catch (Exception e) {
            assert false;
        }

        assertEquals(0, board3.getTowers().size());
        assertEquals(8, targetIsland.getTowers().size());

        model.privateModel.removeAllTowers(targetIsland);

        assertEquals(8, board3.getTowers().size());
        assertEquals(0, targetIsland.getTowers().size());
    }

    @Test
    void mergeIslands() {
        Model model = PublicModelTest.twoPlayersBasicSetup();
        Board board0 = model.players.get(0).getBoard();
        Island motherNatureIsland = model.publicModel.getMotherNatureIsland();
        try {
            model.privateModel.mergeIslands(motherNatureIsland);
            assert false;
        } catch (Exception e) {
            assertEquals(12, model.islands.size());
        }
        try {
            model.islands.get(0).addTower(board0.removeTower());
        } catch (Exception e) {
            assert false;
        }
        try {
            model.privateModel.mergeIslands(motherNatureIsland);
            assertEquals(12, model.islands.size());
        } catch (Exception e) {
            assert false;
        }
        try {
            model.islands.get(1).addTower(board0.removeTower());
            motherNatureIsland = model.publicModel.getMotherNatureIsland();
        } catch (Exception e) {
            // Ok
        }
        try {
            model.privateModel.mergeIslands(motherNatureIsland);
            assertEquals(11, model.islands.size());
            motherNatureIsland = model.publicModel.getMotherNatureIsland();
            assertEquals(2, motherNatureIsland.getTowers().size());
        } catch (Exception e) {
            assert false;
        }
        model = PublicModelTest.twoPlayersBasicSetup();
        board0 = model.players.get(0).getBoard();
        try {
            model.islands.get(0).addTower(board0.removeTower());
            model.islands.get(1).addTower(board0.removeTower());
            model.islands.get(1).addTower(board0.removeTower());
            model.islands.get(2).addTower(board0.removeTower());
            model.islands.get(2).addTower(board0.removeTower());
            model.islands.get(2).addTower(board0.removeTower());
            model.privateModel.mergeIslands(model.islands.get(1));
            assertEquals(10, model.islands.size());
            assertEquals(6, model.publicModel.getMotherNatureIsland().getTowers().size());
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    void checkVictoryConditions() {
        // 2 players Test
        Model model = PublicModelTest.twoPlayersBasicSetup();
        Player p = model.privateModel.checkVictoryConditions();
        assertNull(p);
        model.players.get(0).getBoard().getTowers().clear();
        p = model.privateModel.checkVictoryConditions();
        assertEquals(model.players.get(0), p);
        // <=3 islands
        model = PublicModelTest.twoPlayersBasicSetup();
        model.islands.clear();
        model.islands.add(new Island());
        model.islands.add(new Island());
        model.islands.add(new Island());

        // TowerVictory
        Player winningPlayer = model.players.get(0);
        try {
            winningPlayer.getBoard().removeTower();
        } catch (NoTowerException e1) {
            assert false;
        }

        p = model.privateModel.checkVictoryConditions();
        assertEquals(p, winningPlayer);
        // assistant cards empty
        model = PublicModelTest.twoPlayersBasicSetup();
        for (AssistantCard ac : AssistantCard.values()) {
            try {
                model.players.get(0).getDeck().playAssistantCard(ac);
            } catch (NotFoundException e) {
                assert false;
            }
        }
        model.players.get(0).getDeck().getHand().clear();
        model.professors.get(0).move(model.players.get(0).getBoard());
        // Professors Victory
        p = model.privateModel.checkVictoryConditions();
        assertEquals(model.players.get(0), p);
        // empty bag
        model = PublicModelTest.twoPlayersBasicSetup();
        model.bag = new Bag(0);
        model.players.get(0).getDeck().getHand().clear();
        model.professors.get(0).move(model.players.get(0).getBoard());

    }

    @Test
    void checkTowersForVictory() {
        // checked in checkVictoryConditions()
    }

    @Test
    void checkProfessorsForVictory() {
        // checked in checkVictoryConditions()
    }

    @Test
    void getPlayerFromBoard() {
        Model model = PublicModelTest.twoPlayersBasicSetup();

        for (Player p : model.players) {
            try {
                assertEquals(p, model.privateModel.getPlayerFromBoard(p.getBoard()));
            } catch (BoardNotInGameException e) {
                assert false;
            }
        }

    }

    @Test
    void sameAssistantCardOrder() {
        Model model = PublicModelTest.twoPlayersBasicSetup();
        Player firstPlayer = model.players.get(0);
        Player secondPlayer = model.players.get(1);
        for (Player p : model.players) {
            for (int i = 0; i < AssistantCard.values().length - 1; i++) {
                try {
                    p.getDeck().playAssistantCard(AssistantCard.values()[i]);
                } catch (NotFoundException e) {
                    assert false;
                }
            }
        }
        model.firstPlayer = firstPlayer;
        assertDoesNotThrow(() -> model.publicModel.playAssistant(AssistantCard.CARD_10));
        model.publicModel.endTurn();
        assertDoesNotThrow(() -> model.publicModel.playAssistant(AssistantCard.CARD_10));
        model.publicModel.endTurn();
        assertEquals(firstPlayer, model.publicModel.getCurrentPlayer());
    }

    @Test
    void winByProfessorFourPlayers() {
        Model model = PublicModelTest.fourPlayersBasicSetup();
        while (!model.bag.isEmpty()) {

            assertDoesNotThrow(() -> model.bag.draw());

        }

        model.currentPlayer = model.players.get(3);
        model.players.get(3).getBoard().getEntrance().set(1, new Student(Color.RED_DRAGONS, 1));
        assertDoesNotThrow(() -> model.publicModel.moveStudentToDiningRoom(Color.RED_DRAGONS));
        assertEquals(model.players.get(2), model.privateModel.checkVictoryConditions());

    }

    @Test
    void tieInfluence4Players() {
        Model model = PublicModelTest.fourPlayersBasicSetup();
        model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());
        assertEquals(0, model.publicModel.getMotherNatureIsland().getTowers().size());
    }
}