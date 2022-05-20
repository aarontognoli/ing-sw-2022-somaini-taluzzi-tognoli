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

    public List<Student> getStudents() {
        // remove the students from the cloud and return them
        List<Student> returnValue = students;
        students = null;
        return returnValue;
    }

    public List<Student> getStudentsWithoutEmptying() {
        return students;
    }

    public void putStudents(List<Student> newStudents) {
        students = newStudents;
    }
}
