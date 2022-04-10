package it.polimi.ingsw.cards.characters.BardCharacter;

import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.player.DiningRoomFullException;

import java.util.ArrayList;
import java.util.List;

public class BardCharacter extends CharacterCard {

    static String LIST_SIZE_NOT_MATCH = "Student List and Entrance List have different sizes";
    static String LIST_SIZE_TOO_BIG = "Cannot exchange more than 2 students";

    public BardCharacter(Model model) {
        super(model, 1);
    }

    @Override
    protected void internalActivateEffect(Object arguments) throws CCArgumentException {
        if (!(arguments instanceof BardCharacterArgument bardArgument)) {
            throw new CCArgumentException(CCArgumentException.INVALID_CLASS_MESSAGE);
        }
        List<Student> studentList = new ArrayList<>();
        List<Student> entrance = model.publicModel.getCurrentPlayer().getBoard().getEntrance();

        List<Color> studColorDining = bardArgument.getStudentsColorToExchangeDiningRoom();
        List<Color> studColorEntrance = bardArgument.getStudentsColorToExchangeEntrance();

        if (studColorDining.size() != studColorEntrance.size()) {
            throw new CCArgumentException(LIST_SIZE_NOT_MATCH);
        }

        if (studColorDining.size() > 2) {
            throw new CCArgumentException(LIST_SIZE_TOO_BIG);
        }

        for (Color color : studColorDining) {
            List<Student> students = model.publicModel.getCurrentPlayer().getBoard().getDiningRoom()
                    .get(color.ordinal());
            try {
                studentList.add(students.get(students.size() - 1));
                students.remove(students.get(students.size() - 1));
            } catch (IndexOutOfBoundsException e) {
                throw new CCArgumentException("Invalid argument");
            }
        }
        for (Color color : studColorEntrance) {
            for (Student student : entrance) {
                if (student.getColor().equals(color)) {
                    try {
                        model.publicModel.moveStudentToDiningRoom(color);
                    } catch (NotFoundException e) {
                        for (Student s : studentList) {
                            model.publicModel.getCurrentPlayer().getBoard().getDiningRoom()
                                    .get(s.getColor().ordinal()).add(s);
                        }
                        throw new CCArgumentException("Student you want to move not found in the entrance");
                    } catch (DiningRoomFullException e) {
                        for (Student s : studentList) {
                            model.publicModel.getCurrentPlayer().getBoard().getDiningRoom()
                                    .get(s.getColor().ordinal()).add(s);
                        }
                        throw new CCArgumentException(
                                "The dining room of the color of one of the chosen students is already full");
                    }
                }
                break;
            }
        }
        entrance.addAll(studentList);

    }
}
