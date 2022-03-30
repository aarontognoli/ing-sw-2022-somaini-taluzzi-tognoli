package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrivateModelTest {
    Model model = new Model();

    @Test
    void getProfessorOwner() {

    }

    @Test
    void removeStudentFromEntrance() {


        Board testBoard = new Board();
        List<Student> tempEntrance = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            tempEntrance.add(new Student(Color.YELLOW_GNOMES, i));
        }
        try {
            model.privateModel.removeStudentFromEntrance(new Student(Color.GREEN_FROGS, 7), testBoard);
            assert false;
        } catch (NotFoundException e) {
            assert true;
        }
        testBoard.addStudentsToEntrance(tempEntrance);
        try {
            model.privateModel.removeStudentFromEntrance(new Student(Color.GREEN_FROGS, 7), testBoard);
            assert false;
        } catch (NotFoundException e) {
            assert true;
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
        Island island = new Island();
        model.privateModel.addStudentToIsland(new Student(Color.YELLOW_GNOMES, 0), island);
        assertEquals(island.getStudents().size(), 1);
        assertEquals(island.getStudents().get(0).getID(), 0);
        assertEquals(island.getStudents().get(0).getColor(), Color.YELLOW_GNOMES);


    }

    @Test
    void addStudentToDiningRoom() {
    }

    @Test
    void getInfluence() {
    }

    @Test
    void placeTower() {
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
}