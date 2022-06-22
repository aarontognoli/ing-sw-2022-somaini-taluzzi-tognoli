package it.polimi.ingsw.cloud;

import it.polimi.ingsw.pawn.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Package-only accessible class (only the Factory can instantiate it)
public class Cloud implements Serializable {
    private List<Student> students;

    public Cloud(int studentsCount) {
        students = new ArrayList<>(studentsCount);
    }

    /**
     * @return list of students on the cloud
     * It also removes them from the cloud
     */
    public List<Student> getStudentsAndRemove() {
        List<Student> returnValue = students;
        students = null;
        return returnValue;
    }

    /**
     * @return list of students on the cloud
     * It does not remove them from the cloud
     */
    public List<Student> getStudentsWithoutEmptying() {
        return students;
    }

    /**
     * @param newStudents list of students to put on (empty) cloud
     */
    public void putStudents(List<Student> newStudents) {
        students = newStudents;
    }
}
