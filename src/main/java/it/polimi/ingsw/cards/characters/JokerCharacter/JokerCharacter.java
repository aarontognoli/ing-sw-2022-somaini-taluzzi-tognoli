package it.polimi.ingsw.cards.characters.JokerCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCardWithStudents;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CharacterArgumentHandler.CLIJokerCharacterArgumentHandler;
import it.polimi.ingsw.pawn.Student;

import java.util.ArrayList;
import java.util.List;

public class JokerCharacter extends CharacterCardWithStudents {

    public static final
    String SIZE_DONT_MATCH = "Must move same amount of players from player board and from Joker card";
    public static final
    String SIZE_TOO_BIG = "Cannot move more than 3 players from player board and from Joker card";

    public static final
    String COLOR_NOT_FOUND_ENTRANCE = "A student with one of the chosen colors was not found in entrance";

    public static final
    String COLOR_NOT_FOUND_JOKER = "A student with one of the chosen colors was not found in joker";

    public static final int INITIAL_STUDENT_SIZE = 6;


    public JokerCharacter(Model model) {
        super(model, 1, INITIAL_STUDENT_SIZE);
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
    public ClientMessage CLIClientSideActivate(CLIView cliView, int cardIndex) throws ClientSideCheckException {
        cliView.setCurrentQueryMessage("""
                Choose up to three students to exchange between the JokerCharacter card and your entrance.
                                        
                Type: <students_joker> <students_entrance>
                                        
                Where:
                <students_joker> is the color (or the colors) of the students in the joker card you want to exchange.
                <students_entrance> is the color (or the colors) of the students in your entrance you want to exchange.
                color = yellow | blue | green | red | pink
                                    
                Type 'exit' if you have changed your mind.
                """);

        cliView.setCliStringHandler(new CLIJokerCharacterArgumentHandler(cardIndex));

        throw new ClientSideCheckException();
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
        List<Student> backupJokerStudents = new ArrayList<>(students);

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
                studentsFromJoker.add(findStudentByColorAndNull(students, colorToMove));
            } catch (NotFoundException e) {
                playerEntrance.clear();
                playerEntrance.addAll(backupEntrance);
                students.clear();
                students.addAll(backupJokerStudents);

                throw new CCArgumentException(COLOR_NOT_FOUND_JOKER);
            }
        }

        swapNullsWith(students, studentsFromEntrance);
        swapNullsWith(playerEntrance, studentsFromJoker);
    }

    /**
     * @return pointer to the students list
     * @apiNote should only be used by tests, in fact is package private
     */
    List<Student> getStudentsListReference() {
        return students;
    }
}
