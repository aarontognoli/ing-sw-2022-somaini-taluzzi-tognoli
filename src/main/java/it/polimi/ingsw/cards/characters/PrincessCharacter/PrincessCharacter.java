package it.polimi.ingsw.cards.characters.PrincessCharacter;

import it.polimi.ingsw.bag.BagEmptyException;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCardWithStudents;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.exceptions.DiningRoomFullException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CharacterArgumentHandler.CLIColorCharacterArgumentHandler;
import it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.CardInfoColor;
import it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.CardInfoController;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.player.Board;

import java.util.List;

public class PrincessCharacter extends CharacterCardWithStudents {
    public static final int INITIAL_STUDENT_SIZE = 4;

    public static final String STUDENT_NOT_FOUND = "Student not found in Princess card";
    public static final String DINING_ROOM_FULL = "Dining room full for this color";

    public PrincessCharacter(Model model) {
        super(model, 2, INITIAL_STUDENT_SIZE);
    }

    @Override
    public CardInfoController getCharacterCardInfoController() {
        return new CardInfoColor();
    }

    @Override
    public ClientMessage CLIClientSideActivate(CLIView cliView, int cardIndex) throws ClientSideCheckException {
        cliView.setCurrentQueryMessage("""
                Choose a student from this card and place it in your dining room.
                                        
                Type: <chosen_student>
                                    
                Where:
                <chosen_student> is the color of the chosen student.
                color = yellow | blue | green | red | pink
                                    
                Type 'exit' if you have changed your mind.
                """);

        cliView.setCliStringHandler(new CLIColorCharacterArgumentHandler(cardIndex));
        throw new ClientSideCheckException();
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
