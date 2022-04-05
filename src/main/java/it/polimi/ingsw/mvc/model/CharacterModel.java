package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.bag.BagEmptyException;
import it.polimi.ingsw.pawn.Student;

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

}
