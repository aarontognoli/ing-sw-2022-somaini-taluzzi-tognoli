package it.polimi.ingsw.cloud;

import it.polimi.ingsw.pawn.Student;

// Package-only accessible class (only the Factory can instantiate it)
class Cloud {
    private Student[] students;

    public Cloud(int studentsCount) {
        students = new Student[studentsCount];
    }

    public Student[] getStudents() {
        // remove the students from the cloud and return them
        Student[] returnValue = students;
        students = null;
        return returnValue;
    }

    public void putStudents(Student[] newStudents) {
        students = newStudents;
    }
}
