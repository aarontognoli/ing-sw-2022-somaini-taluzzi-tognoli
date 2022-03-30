package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.DeckName;
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

        Model model = regoleBase2Player();
        Board testBoard = new Board();
        List<Student> tempEntrance = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            tempEntrance.add(new Student(Color.YELLOW_GNOMES, i));
        }
        try {
            model.privateModel.removeStudentFromEntrance(new Student(Color.GREEN_FROGS, 7), testBoard);

        } catch (NotFoundException e) {
            assert true;
        }
        testBoard.addStudentsToEntrance(tempEntrance);
        try {
            model.privateModel.removeStudentFromEntrance(new Student(Color.GREEN_FROGS, 7), testBoard);

        } catch (NotFoundException e) {
            assert true;
        }
        try {
            Student s = model.privateModel.removeStudentFromEntrance(new Student(Color.YELLOW_GNOMES, 7), testBoard);
            assertEquals(s.getColor(), Color.YELLOW_GNOMES);
            assertEquals(testBoard.getEntrance().size(), 6);
            assert true;
        } catch (NotFoundException e) {

        }
    }

    @Test
    void addStudentToIsland() {
        Model model = regoleBase2Player();
        Island island = new Island();
        model.privateModel.addStudentToIsland(new Student(Color.YELLOW_GNOMES, 0), island);
        assertEquals(island.getStudents().size(), 1);
        assertEquals(island.getStudents().get(0).getID(), 0);
        assertEquals(island.getStudents().get(0).getColor(), Color.YELLOW_GNOMES);


    }

    @Test
    void addStudentToDiningRoom() {
        Model model = regoleBase2Player();
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

        }
        try {       //10 = DINING_ROOM_MAX_STUDENT_COUNT
            for (int i = 0; i < 10; i++) {
                model.privateModel.addStudentToDiningRoom(new Student(Color.YELLOW_GNOMES, i + 1), testBoard);
            }
            assert true;
        } catch (DiningRoomFullException e) {

        }
        try {
            model.privateModel.addStudentToDiningRoom(new Student(Color.YELLOW_GNOMES, 12), testBoard);
        } catch (DiningRoomFullException e) {
            assert true;
        }


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

    Model regoleBase2Player()
    {
        Model model=null;
        Map<String, DeckName> temp = new HashMap<>();
        temp.put("Player1",DeckName.DESERT_KING);
        temp.put("Player2",DeckName.CLOUD_WITCH);
        try {
             model = new Model(0, temp);
        }
        catch (Exception e)
        {}
        return  model;
    }
}