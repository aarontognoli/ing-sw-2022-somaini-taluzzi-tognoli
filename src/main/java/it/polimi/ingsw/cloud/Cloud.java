package it.polimi.ingsw.cloud;

import it.polimi.ingsw.pawn.Student;

import java.util.ArrayList;
import java.util.List;

// Package-only accessible class (only the Factory can instantiate it)
public class Cloud {
    private List<Student> students;

    public Cloud() {
        students = new ArrayList<Student>();
    }

    public List<Student> getStudents() {
        // remove the students from the cloud and return them
        List<Student> returnValue = students;
        students = null;
        return returnValue;
    }

    public void putStudents(List<Student> newStudents) {
        students = newStudents;
    }
}
