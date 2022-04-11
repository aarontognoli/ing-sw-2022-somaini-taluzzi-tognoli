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

    public void reinsert(Student s) throws IllegalArgumentException {
        if (s == null)
            throw new IllegalArgumentException("Studente non valido");

        students.add(s);

        Collections.shuffle(students);

    }

    public boolean isEmpty() {
        return students.size() == 0;
    }
}
