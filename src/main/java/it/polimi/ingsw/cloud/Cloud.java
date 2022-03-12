package it.polimi.ingsw.cloud;

import java.util.List;

// Package-only accessible class (only the Factory can instantiate it)
class Cloud {
    private List<Student> students;

    public Cloud(int studentsCount) {
        students = new List<Student>(studentsCount);
    }
}

public class FactoryCloud {
    public static Cloud createTwoFourPlayersCloud() {
        // Three students per cloud when there are 2 or 4 players
        return new Cloud(3);
    }

    public static Cloud createThreePlayersCloud() {
        // Four students per cloud when there are 3 players
        return new Cloud(4);
    }
}
