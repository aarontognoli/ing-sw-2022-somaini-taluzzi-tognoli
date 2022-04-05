package it.polimi.ingsw.mvc.model;

public class ProfessorMoverRuleDefault {
    public boolean isMaxStudentsCount(int newStudentCount, int previousMax, String playerName) {
        return newStudentCount > previousMax;
    }
}
