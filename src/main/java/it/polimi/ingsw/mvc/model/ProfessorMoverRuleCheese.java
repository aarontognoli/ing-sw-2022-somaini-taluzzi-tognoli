package it.polimi.ingsw.mvc.model;

public class ProfessorMoverRuleCheese extends ProfessorMoverRuleDefault {

    private final String targetPlayerName;

    public ProfessorMoverRuleCheese(String playerName) {
        super();
        targetPlayerName = playerName;
    }

    @Override
    public boolean isMaxStudentsCount(int newStudentCount, int previousMax, String playerName) {
        if (playerName.equals(targetPlayerName)) {
            return newStudentCount >= previousMax;
        }
        return super.isMaxStudentsCount(newStudentCount, previousMax, playerName);
    }
}
