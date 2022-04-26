package it.polimi.ingsw.match;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.player.DiningRoomFullException;
import it.polimi.ingsw.player.Player;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.mvc.model.PublicModelTest.*;
import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {
    @Test
    public void twoPlayersEasyMatchTest() {
        System.out.println("---------------- Two Players Easy Match -----------------");
        Model model = twoPlayersBasicSetup();
        //SAME DECKNAME CHECK IN CONTROLLER(?)
        Player p0 = model.publicModel.getCurrentPlayer();
        assertDoesNotThrow(() -> model.publicModel.playAssistant(AssistantCard.CARD_2));//(2 turn, 1 movement)
        model.publicModel.endTurn();
        Player p1 = model.publicModel.getCurrentPlayer();
        assertThrows(AssistantCardAlreadyPlayedException.class, () -> model.publicModel.playAssistant(AssistantCard.CARD_2));
        assertDoesNotThrow(() -> model.publicModel.playAssistant(AssistantCard.CARD_1));//(1 turn, 1 movement)
        model.publicModel.endTurn();
        assertEquals(p1, model.publicModel.getCurrentPlayer());
        //CHECK FOR MANDATORY ACTIONS IN CONTROLLER(?)
        int i = 0;
        for (Color c : Color.values()) {
            while (i < 3) {
                try {
                    model.publicModel.moveStudentToDiningRoom(c);
                    i++;
                } catch (NotFoundException e) {
                    break;
                } catch (DiningRoomFullException e) {
                    assert false;
                }
            }
        }
        assertEquals(3, i);
        assertDoesNotThrow(() -> model.publicModel.drawStudentsIntoEntrance(0));
        assertThrows(TooMuchStepsException.class, () -> model.publicModel.moveMotherNature(2));
        assertDoesNotThrow(() -> model.publicModel.moveMotherNature(1));
        model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());
        //Just to see what's happening
        Player curr;
        for (Professor p : getProfessors(model)) {
            curr = getProfessorOwnerPlayer(model, p);
            System.out.print(p.getColor() + ": " + (curr == null ? "Null" : curr.getNickname()) + " { ");
            //print player one dining rooms
            for (Student s : p1.getBoard().getDiningRoom().get(p.getColor().ordinal())) {
                System.out.print("[ " + s.getID().toString() + " ] ");
            }
            System.out.print("}\n");
        }


        model.publicModel.endTurn();
        assertEquals(p0, model.publicModel.getCurrentPlayer());

        i = 0;
        for (Color c : Color.values()) {
            while (i < 3) {
                try {
                    model.publicModel.moveStudentToIsland(c, 2);
                    i++;
                } catch (NotFoundException e) {
                    break;
                }
            }
        }
        assertEquals(3, i);
        assertDoesNotThrow(() -> model.publicModel.moveMotherNature(1));
        model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());
        System.out.println("Island number 2 ");
        System.out.print("{ ");
        for (Student s : model.publicModel.getMotherNatureIsland().getStudents()) {
            System.out.print("[ " + s.getID().toString() + " , " + s.getColor() + " ] ");
        }
        System.out.print("}\n");
        System.out.print("Tower color: ");
        try {
            System.out.println(model.publicModel.getMotherNatureIsland().getTowerColor());
        } catch (NoTowerException e) {
            System.out.println("No Tower");
        }

        assertThrows(CloudEmptyException.class, () -> model.publicModel.drawStudentsIntoEntrance(0));
        assertDoesNotThrow(() -> model.publicModel.drawStudentsIntoEntrance(1));
        model.publicModel.endTurn();

        assertEquals(p1, model.publicModel.getCurrentPlayer());
        //TODO


    }


}
