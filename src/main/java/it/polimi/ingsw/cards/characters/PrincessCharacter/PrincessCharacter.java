package it.polimi.ingsw.cards.characters.PrincessCharacter;

import it.polimi.ingsw.bag.BagEmptyException;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.DiningRoomFullException;

import java.util.ArrayList;
import java.util.List;

public class PrincessCharacter extends CharacterCard {
    public static final int INITIAL_STUDENT_SIZE = 4;
    public static final String STUDENT_NOT_FOUND = "Student not found in Princess card";
    public static final String DINING_ROOM_FULL = "Dining room full for this color";

    final List<Student> students;

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public PrincessCharacter(Model model) {
        super(model, 2);

        students = new ArrayList<>(INITIAL_STUDENT_SIZE);
        for (int j = 0; j < INITIAL_STUDENT_SIZE; j++) {
            try {
                students.add(model.characterModel.drawStudentFromBag());
            } catch (BagEmptyException e) {
                throw new RuntimeException("This should never happen");
            }

        }
    }

    @Override
    protected void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (!(arguments instanceof Color targetColor)) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }

        int studentIndex = -1;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getColor() == targetColor) {
                studentIndex = i;
                break;
            }
        }

        if (studentIndex == -1) {
            throw new CCArgumentException(STUDENT_NOT_FOUND);
        }

        Board currentPlayerBoard = model.publicModel.getCurrentPlayer().getBoard();

        try {
            currentPlayerBoard.addStudentsToDiningRoom(students.get(studentIndex));
        } catch (DiningRoomFullException e) {
            throw new CCArgumentException(DINING_ROOM_FULL);
        }

        try {
            Student studentFromBag = model.characterModel.drawStudentFromBag();
            students.set(studentIndex, studentFromBag);
        } catch (BagEmptyException e) {
            // Bag empty, just remove the student from the card
            students.remove(studentIndex);

            // TODO: At the end of the round check for winning player
        }
    }
}
