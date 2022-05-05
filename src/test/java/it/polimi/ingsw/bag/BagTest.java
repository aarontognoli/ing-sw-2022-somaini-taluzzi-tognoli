package it.polimi.ingsw.bag;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.pawn.Student;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static it.polimi.ingsw.bag.Bag.incremental_id;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BagTest {

    @Test
    public void draw() {
        final int STUD_COLOR_COUNT = 100;

        Bag bag = new Bag(STUD_COLOR_COUNT);

        Set<Integer> studentsIdSet = new HashSet<>();

        for (int i = 0; i < 5 * STUD_COLOR_COUNT; i++) {
            try {
                Student drawedStudent = bag.draw();

                assertNotNull(drawedStudent);

                assert !studentsIdSet.contains(drawedStudent.getID());

                studentsIdSet.add(drawedStudent.getID());

            } catch (BagEmptyException e) {
                assert false;
            }
        }

        try {
            bag.draw();
            assert false;
        } catch (BagEmptyException e) {
            // OK, the bag was empty.
        }
    }

    public static void fillBagWithRedStudents(Bag bag, int studentsCount) {
        bag.students.clear();
        for (int j = 0; j < studentsCount; j++) {
            bag.students.add(new Student(Color.RED_DRAGONS, incremental_id));
            incremental_id++;
        }
    }
}