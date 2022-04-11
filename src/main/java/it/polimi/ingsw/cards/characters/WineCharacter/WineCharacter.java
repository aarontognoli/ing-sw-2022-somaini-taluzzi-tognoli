package it.polimi.ingsw.cards.characters.WineCharacter;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.bag.BagEmptyException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;

public class WineCharacter extends CharacterCard {

    public static final int INITIAL_STUDENT_SIZE = 4;

    List<Student> students;

    public WineCharacter(Model model, List<Student> studentsToPlace) {
        super(model, 1);

        if (studentsToPlace.size() != INITIAL_STUDENT_SIZE) {
            // Should never happen
            throw new RuntimeException("Invalid count of students in WineCharacter constructor.");
        }

        students = studentsToPlace;
    }

    public WineCharacter(Model model) {
        super(model, 1);
        List<Student> studentsForWine = new ArrayList<>(WineCharacter.INITIAL_STUDENT_SIZE);
        for (int j = 0; j < WineCharacter.INITIAL_STUDENT_SIZE; j++) {
            try {
                studentsForWine.add(model.characterModel.drawStudentFromBag());
            } catch (BagEmptyException e) {
                throw new RuntimeException("This should never happen");
            }

        }
        students = studentsForWine;
    }

    @Override
    public void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (arguments.getClass() != WineCharacterArgument.class) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }

        WineCharacterArgument classArgument = (WineCharacterArgument) arguments;

        Island targetIsland = classArgument.getTargetIsland();
        int targetStudentId = classArgument.getTargetStudentId();

        int targetStudentIndex = -1;

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getID() == targetStudentId) {
                targetStudentIndex = i;
                break;
            }
        }

        if (targetStudentIndex == -1) {
            throw new CCArgumentException("Student ID not found in wine character card.");
        }

        targetIsland.addStudent(students.get(targetStudentIndex));

        // Draw a new student from the bag and replace the one we put on the island
        try {
            students.set(targetStudentIndex, model.characterModel.drawStudentFromBag());
        } catch (BagEmptyException e) {
            throw new CCArgumentException(e.getMessage());
        }
    }
}
