package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.bag.BagEmptyException;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Player;

import java.util.ArrayList;
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

    /**
     * @param whichColor which color of students to remove from each dining room
     * @param howMany    how many students to remove, if a player has less than this amount,
     *                   remove all the student they have of this color
     * @implNote putting all the students removed from the dining rooms into a list and then doing
     * a single `bag.reinsert` call so that we don't have to shuffle the bag many times
     */
    public void removeStudentsFromAllBoards(Color whichColor, int howMany) {
        List<Student> studToReinsert = new ArrayList<>();
        for (Player p : fatherModel.players) {
            List<Student> diningRoomThisColor = p.getBoard().getDiningRoom().get(whichColor.ordinal());
            if (diningRoomThisColor.size() <= howMany) {
                studToReinsert.addAll(diningRoomThisColor);
                diningRoomThisColor.clear();
            } else {
                for (int i = 0; i < howMany; i++) {
                    studToReinsert.add(diningRoomThisColor.remove(diningRoomThisColor.size() - 1));
                }
            }
        }
        fatherModel.bag.reinsert(studToReinsert);
    }

    public Island getIsland(int index) throws IndexOutOfBoundsException {
        return fatherModel.islands.get(index);
    }
}
