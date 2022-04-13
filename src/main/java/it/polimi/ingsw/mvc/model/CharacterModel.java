package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.bag.BagEmptyException;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.player.Player;

import java.util.List;

public class CharacterModel {
    final Model fatherModel;

    public CharacterModel(Model fatherModel) {
        this.fatherModel = fatherModel;
    }

    public Student drawStudentFromBag() throws BagEmptyException {
        return fatherModel.privateModel.drawStudentFromBag();
    }

    public void updateProfessorMoverRule(ProfessorMoverRuleDefault newProfessorMoverRule) {
        fatherModel.professorMoverRule = newProfessorMoverRule;
    }

    public void updateInfluenceCalculator(InfluenceCalculatorRules newInfluenceCalculatorRules) {
        fatherModel.influenceCalculator.setInfluenceCalculatorRules(newInfluenceCalculatorRules);
    }

    public void removeStudentsFromAllBoards(int howMany, Color whichColor) throws IllegalArgumentException {
        for (Player p : fatherModel.players) {
            List<Student> diningRoom = p.getBoard().getDiningRoom().get(whichColor.ordinal());
            for (int i = 0; i < howMany; i++) {
                if (!diningRoom.isEmpty()) {
                    fatherModel.bag.reinsert(diningRoom.remove(diningRoom.size() - 1));
                }
            }
        }
    }

}
