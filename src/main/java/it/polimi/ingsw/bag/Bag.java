package it.polimi.ingsw.bag;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.pawn.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BagEmptyException extends Exception {
    public BagEmptyException() {
        super("Bag is empty");
    }
}

public class Bag {
    static private int incremental_id = 0;

    static final private int COLORS_COUNT = 5;
    static final private Color[] COLORS_BY_INDEX = {
            Color.YELLOW_GNOMES,
            Color.BLUE_UNICORNS,
            Color.GREEN_FROGS,
            Color.RED_DRAGONS,
            Color.PINK_FAIRIES
    };

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
        if (students.size() == 0)
            throw new BagEmptyException();

        Student returnValue = students.get(students.size() - 1);

        students.remove(students.size() - 1);

        return returnValue;
    }
}
