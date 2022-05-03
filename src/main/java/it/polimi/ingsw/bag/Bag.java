package it.polimi.ingsw.bag;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.pawn.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Bag {
    static private int incremental_id = 0;

    static final private int COLORS_COUNT = Color.values().length;
    static final private Color[] COLORS_BY_INDEX = Color.values();

    final private List<Student> students;

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
     * to maintain random draw of students
     */
    public void reinsert(List<Student> studentList) {
        students.addAll(studentList);
        Collections.shuffle(students);
    }

    public boolean isEmpty() {
        return students.size() == 0;
    }

    //only for testing
    public Bag BagForTestingAllRed(int howMany) {
        students.clear();
        for (int j = 0; j < howMany; j++) {
            students.add(new Student(Color.RED_DRAGONS, incremental_id));
            incremental_id++;
        }
        return this;
    }
}
