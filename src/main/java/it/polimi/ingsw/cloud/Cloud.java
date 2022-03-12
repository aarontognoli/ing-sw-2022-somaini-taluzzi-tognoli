package it.polimi.ingsw.cloud;

class FakeStudent {}

// Package-only accessible class (only the Factory can instantiate it)
class Cloud {
    // TODO: Use Student instead of FakeStudent
    private FakeStudent[] students;

    public Cloud(int studentsCount) {
        students = new FakeStudent[studentsCount];
    }

    public FakeStudent[] getStudents() {
        // remove the students from the cloud and return them
        FakeStudent[] returnValue = students;
        students = null;
        return returnValue;
    }

    public void putStudents(FakeStudent[] newStudents) {
        students = newStudents;
    }
}
