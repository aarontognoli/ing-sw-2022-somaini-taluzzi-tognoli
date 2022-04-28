package it.polimi.ingsw.match;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.DiningRoomFullException;
import it.polimi.ingsw.player.Player;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.mvc.model.PublicModelTest.*;
import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {

    void writeTable(Model m) {
        System.out.println("Professors:");
        for (Professor p : getProfessors(m))
            System.out.println(p.getColor() + " [ " + (getProfessorOwnerPlayer(m, p) == null ? "null" : getProfessorOwnerPlayer(m, p).getNickname()) + " ]");
        for (Island i : getIslands(m)) {
            System.out.print(getIslands(m).indexOf(i) + ") ");
            try {
                System.out.print(i.getTowerColor());
            } catch (NoTowerException e) {
                System.out.print("No Tower");
            }
            for (Student s : i.getStudents()) {
                System.out.print(" [ " + s.getID().toString() + " ]");
            }
            System.out.print("\n");
        }

    }

    void writePlayerDiningRoom(Player player, Model model) {
        Player curr;
        for (Professor p : getProfessors(model)) {
            curr = getProfessorOwnerPlayer(model, p);
            System.out.print(p.getColor() + ": " + (curr == null ? "Null" : curr.getNickname()) + " { ");
            //print player dining rooms
            for (Student s : player.getBoard().getDiningRoom().get(p.getColor().ordinal())) {
                System.out.print("[ " + s.getID().toString() + " ] ");
            }
            System.out.print("}\n");
        }
    }

    /*
     *   Create twoPlayersEasyMatch
     *   p0 plays a card so that they play before p1
     *   p0 places students in their dining room or if the dining room is full they place them on the island next to motherNature
     *   p0 moves motherNature by 1
     *   p1 places students in the island next to motherNature
     *   p1 moves motherNature by 1
     *   keep repeating these moves until someone wins
     *   check that the winner is p0
     * */
    @Test
    public void twoPlayersEasyMatchTest() {
        System.out.println("---------------- Two Players Easy Match -----------------");
        Model model = twoPlayersBasicSetup();

        Player p0 = getPlayers(model).get(0);

        Player p1 = getPlayers(model).get(1);


        int turn = 0;

        do {

            System.out.println("--------- TURN" + (++turn) + " ------------");
            writeTable(model);
            AssistantCard card = AssistantCard.values()[(turn - 1) % 10];
            assertDoesNotThrow(() -> model.publicModel.playAssistant(card));
            model.publicModel.endTurn();
            assertThrows(AssistantCardAlreadyPlayedException.class, () -> model.publicModel.playAssistant(card));
            AssistantCard card1 = AssistantCard.values()[(turn) % 10];
            assertDoesNotThrow(() -> model.publicModel.playAssistant(card1));


            model.publicModel.endTurn();
            assertEquals(p0, model.publicModel.getCurrentPlayer());

            int i = 0;
            for (Color c : Color.values()) {
                while (i < 3) {
                    try {
                        model.publicModel.moveStudentToDiningRoom(c);
                        i++;
                    } catch (NotFoundException e) {
                        break;
                    } catch (DiningRoomFullException e) {
                        try {
                            model.publicModel.moveStudentToIsland(c, (getIslands(model).indexOf(model.publicModel.getMotherNatureIsland()) + 1) % getIslands(model).size());
                        } catch (NotFoundException ecc) {
                            break;
                        }
                    }
                }
            }
            assertEquals(3, i);
            assertDoesNotThrow(() -> model.publicModel.drawStudentsIntoEntrance(0));
            assertThrows(TooMuchStepsException.class, () -> model.publicModel.moveMotherNature(10));
            assertDoesNotThrow(() -> model.publicModel.moveMotherNature(1));
            model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());
            if (model.publicModel.getWinner() != null)
                break;

            writePlayerDiningRoom(p0, model);
            model.publicModel.endTurn();

            assertEquals(p1, model.publicModel.getCurrentPlayer());

            i = 0;
            for (Color c : Color.values()) {
                while (i < 3) {
                    try {
                        model.publicModel.moveStudentToIsland(c, (getIslands(model).indexOf(model.publicModel.getMotherNatureIsland()) + 1) % (getIslands(model).size()));
                        i++;
                    } catch (NotFoundException e) {
                        break;
                    }
                }
            }
            assertEquals(3, i);
            assertDoesNotThrow(() -> model.publicModel.moveMotherNature(1));
            model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());

            writeTable(model);
            if (model.publicModel.getWinner() != null)
                break;
            assertThrows(CloudEmptyException.class, () -> model.publicModel.drawStudentsIntoEntrance(0));
            assertDoesNotThrow(() -> model.publicModel.drawStudentsIntoEntrance(1));
            model.publicModel.endTurn();
            if (model.publicModel.getWinner() != null)
                break;
            assertEquals(p0, model.publicModel.getCurrentPlayer());


        } while (true);

        assertEquals(p0, model.publicModel.getWinner());


    }


}
