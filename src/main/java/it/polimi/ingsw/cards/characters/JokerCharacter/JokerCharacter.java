package it.polimi.ingsw.cards.characters.JokerCharacter;

import it.polimi.ingsw.bag.BagEmptyException;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.pawn.Student;

import java.util.ArrayList;
import java.util.List;

public class JokerCharacter extends CharacterCard {

    public static final
    String SIZE_DONT_MATCH = "Must move same amount of players from player board and from Joker card";
    public static final
    String SIZE_TOO_BIG = "Cannot move more than 3 players from player board and from Joker card";

    public static final
    String COLOR_NOT_FOUND_ENTRANCE = "A student with one of the chosen colors was not found in entrance";

    public static final
    String COLOR_NOT_FOUND_JOKER = "A student with one of the chosen colors was not found in joker";

    public static final int INITIAL_STUDENT_SIZE = 6;

    final List<Student> jokerStudents;

    public List<Student> getJokerStudents() {
        return new ArrayList<>(jokerStudents);
    }


    public JokerCharacter(Model model) {
        super(model, 1);

        jokerStudents = new ArrayList<>(JokerCharacter.INITIAL_STUDENT_SIZE);
        for (int i = 0; i < JokerCharacter.INITIAL_STUDENT_SIZE; i++) {
            try {
                jokerStudents.add(model.characterModel.drawStudentFromBag());
            } catch (BagEmptyException e) {
                throw new RuntimeException("Cannot draw students from bag in JokerCharacter. What? " + e.getMessage());
            }
        }
    }

    /**
     * @param list        List where I should look for the student
     * @param targetColor color of the student I should set as null and return
     * @return first student in list with targetColor as color
     */
    private Student findStudentByColorAndNull(List<Student> list, Color targetColor) throws NotFoundException {
        for (int i = 0; i < list.size(); i++) {
            Student stud = list.get(i);
            if (stud != null && stud.getColor() == targetColor) {
                list.set(i, null);
                return stud;
            }
        }

        throw new NotFoundException("Student not found with color " + targetColor.toString());
    }

    /**
     * @param listWithNull  list with some null values
     * @param studentsToAdd list with the students to add in the null places
     */
    private void swapNullsWith(List<Student> listWithNull, List<Student> studentsToAdd) {
        for (Student s : studentsToAdd) {
            int nullIndex = listWithNull.indexOf(null);

            if (nullIndex == -1) {
                throw new RuntimeException("We are just swapping, why can't we find a null value?!?");
            }

            listWithNull.set(nullIndex, s);
        }
    }

    @Override
    public void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (!(arguments instanceof JokerCharacterArgument jokerCharacterArgument)) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }

        List<Color> colorRemoveBoard = jokerCharacterArgument.getColorRemoveBoard();
        List<Color> colorRemoveJoker = jokerCharacterArgument.getColorRemoveJoker();

        if (colorRemoveJoker.size() != colorRemoveBoard.size()) {
            throw new CCArgumentException(SIZE_DONT_MATCH);
        }

        if (colorRemoveJoker.size() > 3) {
            throw new CCArgumentException(SIZE_TOO_BIG);
        }

        List<Student> playerEntrance = model.publicModel.getCurrentPlayer().getBoard().getEntrance();

        List<Student> backupEntrance = new ArrayList<>(playerEntrance);
        List<Student> backupJokerStudents = new ArrayList<>(jokerStudents);

        List<Student> studentsFromEntrance = new ArrayList<>(colorRemoveJoker.size());
        for (Color colorToMove : colorRemoveBoard) {
            try {
                studentsFromEntrance.add(findStudentByColorAndNull(playerEntrance, colorToMove));
            } catch (NotFoundException e) {
                playerEntrance.clear();
                playerEntrance.addAll(backupEntrance);
                throw new CCArgumentException(COLOR_NOT_FOUND_ENTRANCE);
            }
        }

        List<Student> studentsFromJoker = new ArrayList<>(colorRemoveJoker.size());
        for (Color colorToMove : colorRemoveJoker) {
            try {
                studentsFromJoker.add(findStudentByColorAndNull(jokerStudents, colorToMove));
            } catch (NotFoundException e) {
                playerEntrance.clear();
                playerEntrance.addAll(backupEntrance);
                jokerStudents.clear();
                jokerStudents.addAll(backupJokerStudents);

                throw new CCArgumentException(COLOR_NOT_FOUND_JOKER);
            }
        }

        swapNullsWith(jokerStudents, studentsFromEntrance);
        swapNullsWith(playerEntrance, studentsFromJoker);
    }
}
