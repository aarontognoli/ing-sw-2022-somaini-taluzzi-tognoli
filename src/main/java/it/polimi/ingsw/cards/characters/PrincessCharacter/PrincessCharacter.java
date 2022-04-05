package it.polimi.ingsw.cards.characters.PrincessCharacter;

import it.polimi.ingsw.bag.BagEmptyException;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.player.DiningRoomFullException;
import it.polimi.ingsw.player.Player;

import java.util.ArrayList;
import java.util.List;

public class PrincessCharacter extends CharacterCard {
    public static final int INITIAL_STUDENT_SIZE = 4;

    private List<Student> students;

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }


    public PrincessCharacter(Model model, List<Student> initialStudents) {
        super(model, 2);

        if (initialStudents.size() != INITIAL_STUDENT_SIZE) {
            // Should never happen
            throw new RuntimeException("Invalid count of students in JokerCharacter constructor");
        }

        students = initialStudents;
    }

    private int indexOfStudentId(int studentId, List<Student> students) {

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getID() == studentId) {
                return i;
            }
        }

        return -1;
    }


    @Override
    protected void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (!(arguments instanceof Integer)) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }

        int studentIdToGet = (int) arguments;
        int studentIndex = indexOfStudentId(studentIdToGet, students);

        if (studentIndex == -1) {
            throw new CCArgumentException("Student ID Not Valid");
        }

        Player caller = model.publicModel.getCurrentPlayer();

        try {
            caller.getBoard().addStudentsToDiningRoom(students.get(studentIndex));
        } catch (DiningRoomFullException e) {
            throw new CCArgumentException("Dining room Full");
        }

        students.remove(studentIndex);

        try {
            students.add(model.characterModel.drawStudentFromBag());
        } catch (BagEmptyException e) {
            System.out.println("Empty Bag, finish round.");
        }
    }
}
