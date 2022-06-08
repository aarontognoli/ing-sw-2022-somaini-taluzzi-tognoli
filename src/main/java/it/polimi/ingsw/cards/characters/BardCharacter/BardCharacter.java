package it.polimi.ingsw.cards.characters.BardCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.CharacterCardsEffectArguments;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.ClientSideCheckException;
import it.polimi.ingsw.exceptions.DiningRoomFullException;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.messages.ClientMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.GameCLIStringHandler.CharacterArgumentHandler.CLIBardCharacterArgumentHandler;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.player.Board;

import java.util.ArrayList;
import java.util.List;

public class BardCharacter extends CharacterCard {

    static String LIST_SIZE_NOT_MATCH = "Student List and Entrance List have different sizes";
    static String LIST_SIZE_TOO_BIG = "Cannot exchange more than 2 students";
    static String STUDENT_NOT_IN_ENTRANCE = "Student you want to move not found in the entrance";
    static String STUDENT_NOT_IN_DINING = "Student you want to move not found in the dining room";
    static String DINING_ROOM_FULL = "The dining room of the color of one of the chosen students is already full";


    public BardCharacter(Model model) {
        super(model, 1);
        super.argumentType = CharacterCardsEffectArguments.BARD;
    }


    private List<Student> deepCopyEntrance(List<Student> entrance) {
        return new ArrayList<>(entrance);
    }

    private List<List<Student>> deepCloneDining(List<List<Student>> dining) {
        List<List<Student>> returnValue = new ArrayList<>();

        for (List<Student> diningByColor : dining) {
            returnValue.add(new ArrayList<>(diningByColor));
        }

        return returnValue;
    }

    @Override
    public ClientMessage CLIClientSideActivate(CLIView cliView, int cardIndex) throws ClientSideCheckException {
        cliView.setCurrentQueryMessage("""
                Choose up to two students to exchange between your Entrance and your dining room.
                                        
                Type: <students_entrance> <students_dining>
                                        
                Where:
                <students_entrance> is the color (or the colors) of the students in your entrance you want to exchange.
                <students_dining> is the color (or the colors) of the students in your dining room you want to exchange.
                color = yellow | blue | green | red | pink
                                    
                Type 'exit' if you have changed your mind.
                """);

        cliView.setCliStringHandler(new CLIBardCharacterArgumentHandler(cardIndex));

        throw new ClientSideCheckException();
    }

    @Override
    protected void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (!(arguments instanceof BardCharacterArgument bardArgument)) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }
        List<Color> studColorDining = bardArgument.getStudentsColorToExchangeDiningRoom();
        List<Color> studColorEntrance = bardArgument.getStudentsColorToExchangeEntrance();

        if (studColorDining.size() != studColorEntrance.size()) {
            throw new CCArgumentException(LIST_SIZE_NOT_MATCH);
        }
        if (studColorDining.size() > 2) {
            throw new CCArgumentException(LIST_SIZE_TOO_BIG);
        }

        List<Student> entrance = model.publicModel.getCurrentPlayer().getBoard().getEntrance();
        List<Student> copyEntrance = deepCopyEntrance(entrance);
        List<List<Student>> diningRoom = model.publicModel.getCurrentPlayer().getBoard().getDiningRoom();
        List<List<Student>> copyDiningRoom = deepCloneDining(diningRoom);

        Board board = model.publicModel.getCurrentPlayer().getBoard();
        List<Student> studentsFromDining = new ArrayList<>(2);
        for (Color color : studColorDining) {
            List<Student> diningThisColor = board.getDiningRoom()
                    .get(color.ordinal());

            if (diningThisColor.size() == 0) {
                // Student not found in dining room: restore and throw
                diningRoom.clear();
                entrance.clear();
                diningRoom.addAll(copyDiningRoom);
                entrance.addAll(copyEntrance);
                throw new CCArgumentException(STUDENT_NOT_IN_DINING);
            }

            studentsFromDining.add(diningThisColor.remove(diningThisColor.size() - 1));
        }

        for (Color color : studColorEntrance) {
            try {
                model.publicModel.moveStudentToDiningRoom(color);
            } catch (NotFoundException | DiningRoomFullException e) {
                // Restore and throw
                diningRoom.clear();
                entrance.clear();
                diningRoom.addAll(copyDiningRoom);
                entrance.addAll(copyEntrance);
                if (e instanceof NotFoundException)
                    throw new CCArgumentException(STUDENT_NOT_IN_ENTRANCE);
                else
                    throw new CCArgumentException(DINING_ROOM_FULL);
            }
        }

        entrance.addAll(studentsFromDining);

        // After we moved students to the dining room, we need to update the professors
        // This does not apply for students moved to the entrance, since the rules
        // Say to update the profs position only when a student is placed in the dining
        // but not on the entrance
        for (Color color : studColorEntrance) {
            model.characterModel.updateProfessorPosition(color);
        }
    }
}
