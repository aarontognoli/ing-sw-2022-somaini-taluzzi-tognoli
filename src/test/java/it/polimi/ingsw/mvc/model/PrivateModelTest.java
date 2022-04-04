package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.exceptions.BoardNotInGameException;
import it.polimi.ingsw.exceptions.NoTowerException;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.exceptions.TowerDifferentColorException;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.pawn.Tower;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.DiningRoomFullException;
import it.polimi.ingsw.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PrivateModelTest {

    @Test
    void getProfessorOwner() {
        Model model = twoPlayersBasicSetup();
        model.currentPlayer = model.players.get(0);
        Board testBoard;
        for (Color c : Color.values())
            assertEquals(null, model.privateModel.getProfessorOwner(c));

        model.privateModel.placeProfessorInBoard(model.professors.get(Color.RED_DRAGONS.ordinal()));
        for (Color c : Color.values()) {
            if (c.equals(Color.RED_DRAGONS)) {
                testBoard = model.currentPlayer.getBoard();
            } else {
                testBoard = null;
            }
            assertEquals(testBoard, model.privateModel.getProfessorOwner(c));
        }

    }

    @Test
    void removeStudentFromEntrance() {

        Model model = twoPlayersBasicSetup();
        Board testBoard = new Board();
        List<Student> tempEntrance = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            tempEntrance.add(new Student(Color.YELLOW_GNOMES, i));
        }

        tempEntrance.add(new Student(Color.RED_DRAGONS, 7));

        // Entrance is empty. Exception expected.
        try {
            model.privateModel.removeStudentFromEntrance(Color.GREEN_FROGS, testBoard);
            assert false;
        } catch (NotFoundException e) {

        }

        // Entrance contains only Yellows, exception expected
        testBoard.addStudentsToEntrance(tempEntrance);
        try {
            model.privateModel.removeStudentFromEntrance(Color.GREEN_FROGS, testBoard);
            assert false;
        } catch (NotFoundException e) {
        }

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
        Model model = twoPlayersBasicSetup();
        Island island = new Island();
        model.privateModel.addStudentToIsland(new Student(Color.YELLOW_GNOMES, 0), island);
        assertEquals(island.getStudents().size(), 1);
        assertEquals(island.getStudents().get(0).getID(), 0);
        assertEquals(island.getStudents().get(0).getColor(), Color.YELLOW_GNOMES);

    }

    @Test
    void addStudentToDiningRoom() {
        Model model = twoPlayersBasicSetup();
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
        try {
            model.privateModel.addStudentToDiningRoom(new Student(Color.YELLOW_GNOMES, 12), testBoard);
            assert false;
        } catch (DiningRoomFullException e) {

        }

    }

    @Test
    void getInfluence() {
        // TODO
    }

    @Test
    void placeTower() {
        Model model = twoPlayersBasicSetup();
        Board board0 = model.players.get(0).getBoard();
        Board board1 = model.players.get(1).getBoard();
        try {
            model.privateModel.placeTower(board0);
        } catch (Exception e) {
            assert false;
        }

        assertEquals(model.publicModel.getMotherNatureIsland().getTowers().size(), 1);
        assertEquals(board0.getTowers().size(), 7);
        try {
            model.privateModel.placeTower(board1);
            assert false;
        } catch (TowerDifferentColorException e) {
            assertEquals(board1.getTowers().size(), 8);
            try {
                assertEquals(model.publicModel.getMotherNatureIsland().getTowerColor(),
                        board0.getTowers().get(0).getColor());
            } catch (Exception ein) {
                assert false;
            }
        } catch (Exception e) {
            assert false;
        }
        for (int i = 0; i < 7; i++) {
            try {
                model.privateModel.placeTower(board0);

            } catch (Exception e) {
                assert false;
            }
        }
        assertEquals(0, board0.getTowers().size());
        try {
            model.privateModel.placeTower(board0);
            assert false;
        } catch (Exception e) {
            assertEquals(0, board0.getTowers().size());
            assertEquals(8, model.publicModel.getMotherNatureIsland().getTowers().size());
        }
    }

    @Test
    void removeAllTowers() {
        Model model = twoPlayersBasicSetup();
        Board board0 = model.players.get(0).getBoard();
        Island motherNatureIsland = model.publicModel.getMotherNatureIsland();
        assertEquals(0, motherNatureIsland.getTowers().size());
        model.privateModel.removeAllTowers(motherNatureIsland);
        assertEquals(0, motherNatureIsland.getTowers().size());
        try {
            for (int i = 0; i < 8; i++) {
                model.privateModel.placeTower(board0);
            }
        } catch (Exception e) {
        }
        assertEquals(0, board0.getTowers().size());
        model.privateModel.removeAllTowers(motherNatureIsland);
        assertEquals(0, motherNatureIsland.getTowers().size());
        assertEquals(8, board0.getTowers().size());
    }

    @Test
    void mergeIslands() {
        Model model = twoPlayersBasicSetup();
        Board board0 = model.players.get(0).getBoard();
        Island motherNatureIsland = model.publicModel.getMotherNatureIsland();
        try {
            model.privateModel.mergeIslands(motherNatureIsland);
            assert false;
        } catch (Exception e) {
            assertEquals(12, model.islands.size());
        }
        try {
            model.privateModel.placeTower(board0);
        } catch (Exception e) {
            assert false;
        }
        try {
            model.privateModel.mergeIslands(motherNatureIsland);
            assert false;
        } catch (Exception e) {
        }
        try {
            // model.publicModel.moveMotherNature(1);
            model.motherNature.move(model.islands.get(1));
            model.privateModel.placeTower(board0);
            motherNatureIsland = model.publicModel.getMotherNatureIsland();
        } catch (Exception e) {

        }
        try {
            model.privateModel.mergeIslands(motherNatureIsland);
            assertEquals(11, model.islands.size());
            motherNatureIsland = model.publicModel.getMotherNatureIsland();
            assertEquals(2, motherNatureIsland.getTowers().size());
        } catch (Exception e) {
            assert false;
        }
        model = twoPlayersBasicSetup();
        board0 = model.players.get(0).getBoard();
        try {
            // model.publicModel.moveMotherNature(1);
            model.privateModel.placeTower(board0);
            model.motherNature.move(model.islands.get(1));
            model.privateModel.placeTower(board0);
            model.privateModel.placeTower(board0);
            model.motherNature.move(model.islands.get(2));
            model.privateModel.placeTower(board0);
            model.privateModel.placeTower(board0);
            model.privateModel.placeTower(board0);
            model.privateModel.mergeIslands(model.islands.get(1));
            assertEquals(10, model.islands.size());
            assertEquals(6, model.publicModel.getMotherNatureIsland().getTowers().size());
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    void placeProfessorInBoard() {
        Model model = twoPlayersBasicSetup();
        Board board0 = model.players.get(0).getBoard();
        model.currentPlayer = model.players.get(0);
        model.privateModel.placeProfessorInBoard(model.professors.get(Color.BLUE_UNICORNS.ordinal()));
        Board testBoard;
        for (Color c : Color.values()) {
            if (c.equals(Color.BLUE_UNICORNS)) {
                testBoard = board0;
            } else {
                testBoard = null;
            }
            assertEquals(testBoard, model.professors.get(c.ordinal()).getPosition());
        }

    }

    @Test
    void checkVictoryConditions() {
        // 2 players Test
        Model model = twoPlayersBasicSetup();
        Player p = model.privateModel.checkVictoryConditions();
        assertEquals(null, p);
        model.players.get(0).getBoard().getTowers().clear();
        p = model.privateModel.checkVictoryConditions();
        assertEquals(model.players.get(0), p);
        // <=3 islands
        model = twoPlayersBasicSetup();
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
        model = twoPlayersBasicSetup();
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
        model = twoPlayersBasicSetup();
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
        Model model = twoPlayersBasicSetup();

        for (Player p : model.players) {
            try {
                assertEquals(p, model.privateModel.getPlayerFromBoard(p.getBoard()));
            } catch (BoardNotInGameException e) {
                assert false;
            }
        }

    }

    Model twoPlayersBasicSetup() {
        Model model = null;
        Map<String, DeckName> temp = new HashMap<>();
        temp.put("Player1", DeckName.DESERT_KING);
        temp.put("Player2", DeckName.CLOUD_WITCH);
        try {
            model = new Model(0, temp, GameMode.EASY_MODE);
        } catch (Exception e) {

        }
        return model;
    }
}