package it.polimi.ingsw.bag;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.BagEmptyException;
import it.polimi.ingsw.pawn.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bag implements Serializable {
    static int incremental_id = 0;

    static final private int COLORS_COUNT = Color.values().length;
    static final private Color[] COLORS_BY_INDEX = Color.values();

    final List<Student> students;

    public Bag(int studentsCountPerColor) {
        students = new ArrayList<>(COLORS_COUNT * studentsCountPerColor);

        // Generate students array and shuffle
        for (int i = 0; i < COLORS_COUNT; i++) {
            Color targetColor = COLORS_BY_INDEX[i];

            for (int j = 0; j < studentsCountPerColor; j++) {
                students.add(new Student(targetColor, incremental_id));
                incremental_id++;
            }
        }

        Collections.shuffle(students);
    }

    /**
     * @return student drawn from the bag
     * @throws BagEmptyException bag is empty
     */
    public Student draw() throws BagEmptyException {
        if (isEmpty())
            throw new BagEmptyException();

        Student returnValue = students.get(students.size() - 1);

        students.remove(students.size() - 1);

        return returnValue;
    }

    /**
     * @param studentList students list to reinsert in the bag
     * @implNote the students in the bug are shuffled again
     *           to maintain random draw of students
     */
    public void reinsert(List<Student> studentList) {
        students.addAll(studentList);
        Collections.shuffle(students);
    }

    /**
     * @return true if the bag is empty, otherwise false
     */
    public boolean isEmpty() {
        return students.size() == 0;
    }
}
