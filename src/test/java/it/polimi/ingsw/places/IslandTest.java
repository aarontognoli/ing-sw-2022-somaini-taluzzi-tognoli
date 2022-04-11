package it.polimi.ingsw.places;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.exceptions.TowerDifferentColorException;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.pawn.Tower;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {

    Island island;

    @Test
    public void singleStudentIsland() {
        island = new Island();
        island.addStudent(new Student(Color.BLUE_UNICORNS, 1234));

        List<Student> students = island.getStudents();

        assertEquals(1, students.size());
        assertEquals(Color.BLUE_UNICORNS, students.get(0).getColor());
        assertEquals(1234, students.get(0).getID().intValue());
    }

    @Test
    public void noStudentIsland() {
        island = new Island();

        List<Student> students = island.getStudents();

        assertEquals(0, students.size());
    }

    @Test
    public void mergeIslandsSameTowerColor() throws Exception {
        Student stud1 = new Student(Color.GREEN_FROGS, 1234);
        Student stud2 = new Student(Color.PINK_FAIRIES, 2345);
        Island i1 = new Island();
        i1.addStudent(stud1);
        Island i2 = new Island();
        i2.addStudent(stud2);

        try {
            i1.addTower(new Tower(TowerColor.BLACK));
            i2.addTower(new Tower(TowerColor.BLACK));
        } catch (Exception e) {
            assert false;
        }

        try {
            island = new Island(i1, i2);
        } catch (TowerDifferentColorException e) {
            assert false;
        }

        assertNotEquals(-1, island.getStudents().indexOf(stud1));
        assertNotEquals(-1, island.getStudents().indexOf(stud2));

        assertEquals(2, island.getTowers().size());
        assertEquals(TowerColor.BLACK, island.getTowerColor());
    }

    @Test
    public void mergeIslandsDifferentTowerColor() {
        Island i1 = new Island();
        i1.addStudent(new Student(Color.GREEN_FROGS, 1234));
        Island i2 = new Island();
        i2.addStudent(new Student(Color.PINK_FAIRIES, 2345));

        try {
            i1.addTower(new Tower(TowerColor.WHITE));
            i2.addTower(new Tower(TowerColor.BLACK));
        } catch (Exception e) {
            assert false;
        }

        try {
            island = new Island(i1, i2);
            assert false;
        } catch (TowerDifferentColorException e) {
        }
    }

    static private void addBlackTowers(Island i) {
        try {
            i.addTower(new Tower(TowerColor.BLACK));
            i.addTower(new Tower(TowerColor.BLACK));
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    public void addDifferentColorTower() {
        island = new Island();

        addBlackTowers(island);

        try {
            island.addTower(new Tower(TowerColor.WHITE));
            assert false;
        } catch (Exception e) {
        }
    }

    @Test
    public void addSameColorTower() throws Exception {
        island = new Island();

        addBlackTowers(island);

        assertEquals(2, island.getTowers().size());
        assertEquals(TowerColor.BLACK, island.getTowerColor());
    }

    @Test
    public void addStudent() {
        island = new Island();

        island.addStudent(new Student(Color.GREEN_FROGS, 1234));
        island.addStudent(new Student(Color.RED_DRAGONS, 2345));

        List<Student> students = island.getStudents();

        assertEquals(2, students.size());
        assertEquals(Color.GREEN_FROGS, students.get(0).getColor());
        assertEquals(1234, students.get(0).getID().intValue());

        assertEquals(Color.RED_DRAGONS, students.get(1).getColor());
        assertEquals(2345, students.get(1).getID().intValue());
    }

    @Test
    public void removeAllTowers() {
        island = new Island();

        addBlackTowers(island);

        List<Tower> beforeRemoveTowers = island.removeAllTowers();

        island.removeAllTowers();

        assertEquals(2, beforeRemoveTowers.size());
        assertEquals(0, island.getTowers().size());
    }

    @Test
    public void getTowerColorWithNoTower() {
        island = new Island();

        try {
            island.getTowerColor();
            assert false;
        } catch (Exception e) {
        }
    }

    @Test
    public void getTowerColorStandard() throws Exception {
        island = new Island();

        addBlackTowers(island);

        assertEquals(TowerColor.BLACK, island.getTowerColor());
    }

    @Test
    public void removeStudent() {
        Student student = new Student(Color.YELLOW_GNOMES, 123);

        island = new Island();
        island.addStudent(student);

        try {
            island.removeStudent(student);
            assertEquals(0, island.getStudents().size());
        } catch (Exception e) {
            assert false;
        }

        try {
            island.removeStudent(student);
            assert false;
        } catch (Exception e) {
        }
    }
}