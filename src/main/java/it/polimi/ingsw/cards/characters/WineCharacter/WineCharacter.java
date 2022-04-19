package it.polimi.ingsw.cards.characters.WineCharacter;

import it.polimi.ingsw.bag.BagEmptyException;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCardWithStudents;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;

import java.util.List;

public class WineCharacter extends CharacterCardWithStudents {
    public static final String STUDENT_NOT_FOUND = "Student ID not found in wine character card";

    public static final int INITIAL_STUDENT_SIZE = 4;

    public WineCharacter(Model model) {
        super(model, 1, INITIAL_STUDENT_SIZE);
    }

    @Override
    public void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (arguments.getClass() != WineCharacterArgument.class) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }

        WineCharacterArgument classArgument = (WineCharacterArgument) arguments;

        int targetIslandIndex = classArgument.getTargetIslandIndex();
        Color targetStudentColor = classArgument.getTargetStudentColor();

        Island targetIsland;

        try {
            targetIsland = model.characterModel.getIsland(targetIslandIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new CCArgumentException(e.getMessage());
        }

        int targetStudentIndex = -1;

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getColor() == targetStudentColor) {
                targetStudentIndex = i;
                break;
            }
        }

        if (targetStudentIndex == -1) {
            throw new CCArgumentException(STUDENT_NOT_FOUND);
        }

        targetIsland.addStudent(students.get(targetStudentIndex));

        try {
            // Draw a new student from the bag and replace the one we put on the island
            Student studentFromBag = model.characterModel.drawStudentFromBag();
            students.set(targetStudentIndex, studentFromBag);
        } catch (BagEmptyException e) {
            // Bag empty, only remove the student without drawing a new one
            students.remove(targetIslandIndex);

            // TODO: At the end of the round check for winner
        }
    }


    /**
     * @return pointer to the students list
     * @apiNote should only be used by tests, in fact is package private
     */
    List<Student> getStudentsListReference() {
        return students;
    }
}
