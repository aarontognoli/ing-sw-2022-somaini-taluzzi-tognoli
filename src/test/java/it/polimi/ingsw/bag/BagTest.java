package it.polimi.ingsw.bag;

import it.polimi.ingsw.pawn.Student;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

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
}