package it.polimi.ingsw.mvc.model;

import java.io.Serializable;

public class ProfessorMoverRuleDefault implements Serializable {
    public boolean isMaxStudentsCount(int newStudentCount, int previousMax, String playerName) {
        return newStudentCount > previousMax;
    }
}
