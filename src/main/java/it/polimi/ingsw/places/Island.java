package it.polimi.ingsw.places;

import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.pawn.Tower;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TowerDifferentColorException extends Exception {
    TowerDifferentColorException(TowerColor color1, TowerColor color2) {
        super("Tower operation between different colors." +
                "\nFirst Color: " + color1.toString() +
                "; Second color: " + color2.toString());
    }
}

public class Island {

    static private final int INITIAL_TOWER_CAPACITY = 8;
    static private final int INITIAL_STUDENT_CAPACITY = 8;

    private List<Tower> towers;
    private final List<Student> students;

    public Island(Student initialStudent) {
        students = new ArrayList<>(INITIAL_STUDENT_CAPACITY);
        towers = new ArrayList<>(INITIAL_TOWER_CAPACITY);

        if (initialStudent != null) {
            // This island _does_ contain a student
            // Some Island are initialized without any student
            students.add(initialStudent);
        }
    }

    public Island(Island i1, Island i2) throws TowerDifferentColorException {
        // Check the 2 island have same color Towers

        if (i1.getTowers().size() != 0 && i2.getTowers().size() != 0) {
            TowerColor towerColor1, towerColor2;
            try {
                towerColor1 = i1.getTowerColor();
                towerColor2 = i2.getTowerColor();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("This should never happen since we checked that both islands have non-zero number of towers.");
            }
            if (!towerColor1.equals(towerColor2)) {
                throw new TowerDifferentColorException(towerColor1, towerColor2);
            }
        }

        // Merge students and towers into a single list
        students = Stream.concat(
                        i1.students.stream(),
                        i2.students.stream()
                )
                .collect(Collectors.toList());

        towers = Stream.concat(
                        i1.towers.stream(),
                        i2.towers.stream()
                )
                .collect(Collectors.toList());
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public List<Student> getStudents() {
        return students;
    }

    public TowerColor getTowerColor() throws Exception {
        if (towers.size() == 0) {
            throw new Exception("Cannot get tower color of island because there is no tower");
        }

        return this.towers.get(0).getColor();
    }

    public void addTower(Tower tower) throws Exception {
        // Make sure all the towers are of the same color
        if (towers.size() != 0) {
            if (!tower.getColor().equals(towers.get(0).getColor())) {
                throw new TowerDifferentColorException(
                        towers.get(0).getColor(),
                        tower.getColor()
                );
            }
        }

        towers.add(tower);
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Tower> removeAllTowers() {
        List<Tower> returnValue = towers;

        towers = new ArrayList<>(INITIAL_TOWER_CAPACITY);

        return returnValue;
    }

    public Student removeStudent(Student student) throws Exception {
        int indexRemove = students.indexOf(student);

        if (indexRemove == -1) throw new Exception("Student not found.");

        return students.remove(indexRemove);
    }
}
