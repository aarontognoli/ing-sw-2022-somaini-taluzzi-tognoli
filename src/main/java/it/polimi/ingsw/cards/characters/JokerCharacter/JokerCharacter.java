package it.polimi.ingsw.cards.characters.JokerCharacter;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.pawn.Student;

public class JokerCharacter extends CharacterCard {

    private static final int INITIAL_STUDENT_SIZE = 6;

    private List<Student> students;

    public JokerCharacter(Model model, List<Student> initialStudents) {
        super(model);

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
    public void activateEffect(Object arguments) throws CCArgumentException {
        if (arguments.getClass() != JokerCharacterArgument.class) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }

        JokerCharacterArgument classArgument = (JokerCharacterArgument) arguments;

        List<Integer> stuIdToRemFromJoker = classArgument.getStudentsIDToRemoveFromJoker();
        List<Integer> stuIdToRemFromPlayer = classArgument.getStudentsIDToRemoveFromPlayerBoard();

        if (stuIdToRemFromJoker.size() != stuIdToRemFromPlayer.size()) {
            throw new CCArgumentException("Must remove same amount of players from player board and from Joker card");
        }

        int studentsToMoveCount = stuIdToRemFromJoker.size();

        List<Student> currentPlayerEntrance = model.publicModel.getCurrentPlayer().getBoard().getEntrance();

        List<Integer> stuIndexToRemFromJoker = new ArrayList<>(studentsToMoveCount);
        List<Integer> stuIndexToRemFromPlayer = new ArrayList<>(studentsToMoveCount);

        // Check that all the IDs exist in the corresponding array. Cannot do the
        // exchange directly because we do not know, until we checked them
        // all, that the argument is valid (There could be an ID that does not exist)
        for (int i = 0; i < studentsToMoveCount; i++) {
            int indexJoker = indexOfStudentId(stuIdToRemFromJoker.get(i), students);

            if (indexJoker == -1) {
                throw new CCArgumentException("Student ID not found in Joker Character card");
            }

            int indexPlayer = indexOfStudentId(stuIdToRemFromJoker.get(i), currentPlayerEntrance);

            if (indexPlayer == -1) {
                throw new CCArgumentException("Student ID not found in current player entrance");
            }

            stuIndexToRemFromJoker.add(indexJoker);
            stuIndexToRemFromPlayer.add(indexPlayer);
        }

        // All the IDs exist in the corresponding array, swap them
        for (int i = 0; i < studentsToMoveCount; i++) {
            int indexJoker = stuIndexToRemFromJoker.get(i);
            int indexPlayer = stuIndexToRemFromPlayer.get(i);

            Student temp = students.get(indexJoker);
            students.set(indexJoker, currentPlayerEntrance.get(indexPlayer));
            currentPlayerEntrance.set(indexPlayer, temp);
        }
    }
}
