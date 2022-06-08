package it.polimi.ingsw.cards.characters;

import it.polimi.ingsw.bag.BagEmptyException;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.CardInfoController;
import it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.CardInfoHasStudents;
import it.polimi.ingsw.pawn.Student;

import java.util.ArrayList;
import java.util.List;

public abstract class CharacterCardWithStudents extends CharacterCard {
    public final static String CANNOT_DRAW_FROM_BAG =
            "Cannot draw students from bag during Character card initialization (?)";

    final protected List<Student> students;

    public CharacterCardWithStudents(Model model, int coinCost, int initialStudentsCount) {
        super(model, coinCost);

        students = new ArrayList<>(initialStudentsCount);
        for (int j = 0; j < initialStudentsCount; j++) {
            try {
                students.add(model.characterModel.drawStudentFromBag());
            } catch (BagEmptyException e) {
                throw new RuntimeException(CANNOT_DRAW_FROM_BAG);
            }
        }
    }

    @Override
    public CardInfoController getCharacterCardInfoController() {
        return new CardInfoHasStudents();
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

}
