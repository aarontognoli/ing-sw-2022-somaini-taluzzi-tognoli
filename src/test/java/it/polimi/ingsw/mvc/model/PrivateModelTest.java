package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.DiningRoomFullException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PrivateModelTest {


    @Test
    void getProfessorOwner() {

    }

    @Test
    void removeStudentFromEntrance() {

        Model model = TwoPlayersBasicSetup();
        Board testBoard = new Board();
        List<Student> tempEntrance = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            tempEntrance.add(new Student(Color.YELLOW_GNOMES, i));
        }
        try {
            model.privateModel.removeStudentFromEntrance(new Student(Color.GREEN_FROGS, 7), testBoard);
            assert false;
        } catch (NotFoundException e) {

        }
        testBoard.addStudentsToEntrance(tempEntrance);
        try {
            model.privateModel.removeStudentFromEntrance(new Student(Color.GREEN_FROGS, 7), testBoard);
            assert false;
        } catch (NotFoundException e) {

        }
        try {
            Student s = model.privateModel.removeStudentFromEntrance(new Student(Color.YELLOW_GNOMES, 7), testBoard);
            assertEquals(s.getColor(), Color.YELLOW_GNOMES);
            assertEquals(testBoard.getEntrance().size(), 6);

        } catch (NotFoundException e) {
            assert false;
        }
    }

    @Test
    void addStudentToIsland() {
        Model model = TwoPlayersBasicSetup();
        Island island = new Island();
        model.privateModel.addStudentToIsland(new Student(Color.YELLOW_GNOMES, 0), island);
        assertEquals(island.getStudents().size(), 1);
        assertEquals(island.getStudents().get(0).getID(), 0);
        assertEquals(island.getStudents().get(0).getColor(), Color.YELLOW_GNOMES);


    }

    @Test
    void addStudentToDiningRoom() {
        Model model = TwoPlayersBasicSetup();
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
        try {       //10 = DINING_ROOM_MAX_STUDENT_COUNT
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
    }

    @Test
    void placeTower() {
        Model model = TwoPlayersBasicSetup();
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
        } catch (Exception e) {
            assertEquals(board1.getTowers().size(), 8);
            try {
                assertEquals(model.publicModel.getMotherNatureIsland().getTowerColor(), board0.getTowers().get(0).getColor());
            } catch (Exception ein) {
            }
        }
        for (int i = 0; i < 7; i++) {
            try {
                //Todo try emptying a board
            } catch (Exception e) {

            }
        }
    }

    @Test
    void removeAllTowers() {
    }

    @Test
    void mergeIslands() {
    }

    @Test
    void placeProfessorInBoard() {
    }

    @Test
    void checkVictoryConditions() {
    }

    @Test
    void checkTowersForVictory() {
    }

    @Test
    void checkProfessorsForVictory() {
    }

    @Test
    void getPlayerFromBoard() {
    }

    @Test
    void getStudentInEntrance() {
    }

    Model TwoPlayersBasicSetup() {
        Model model = null;
        Map<String, DeckName> temp = new HashMap<>();
        temp.put("Player1", DeckName.DESERT_KING);
        temp.put("Player2", DeckName.CLOUD_WITCH);
        try {
            model = new Model(0, temp);
        } catch (Exception e) {
        }
        return model;
    }
}