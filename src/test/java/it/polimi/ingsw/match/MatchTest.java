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
        Model model = twoPlayersTestAllRed();

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

            while (i < 3) {
                try {
                    model.publicModel.moveStudentToDiningRoom(Color.RED_DRAGONS);
                    i++;
                } catch (NotFoundException e) {
                    assert false;
                } catch (DiningRoomFullException e) {
                    try {
                        model.publicModel.moveStudentToIsland(Color.RED_DRAGONS, (getIslands(model).indexOf(model.publicModel.getMotherNatureIsland()) + 1) % getIslands(model).size());
                        i++;
                    } catch (NotFoundException ecc) {
                        assert false;
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

    /*
     *   Create threePlayersEasyMatch
     *   p1 and p0
     *    - plays a card so that they play first then p3 then p2 then p0
     *    - places students in their dining room or if the dining room is full they place them on the island next to motherNature
     *    - moves motherNature by 1
     *   p2 and p0
     *    - places students in the island next to motherNature
     *    - moves motherNature by 1

     *   keep repeating these moves until someone wins
     *   check that the winner is either p0 or p2(same team as p3)
     * */

    @Test
    public void fourPlayersEasyMatchTest() {
        System.out.println("---------------- Four Players Easy Match -----------------");
        Model model = fourPlayersTestAllRed();

        Player p0 = getPlayers(model).get(0);

        Player p1 = getPlayers(model).get(1);

        Player p2 = getPlayers(model).get(2);

        Player p3 = getPlayers(model).get(3);


        int turn = 0;

        do {

            System.out.println("--------- TURN" + (++turn) + " ------------");
            writeTable(model);
            for (int i = 0; i < 4; i++) {
                if (model.publicModel.getCurrentPlayer().equals(p1)) {
                    AssistantCard card2 = AssistantCard.values()[(turn - 1) % 10];
                    assertDoesNotThrow(() -> model.publicModel.playAssistant(card2));
                } else if (model.publicModel.getCurrentPlayer().equals(p3)) {
                    AssistantCard card = AssistantCard.values()[(turn) % 10];
                    assertDoesNotThrow(() -> model.publicModel.playAssistant(card));

                } else if (model.publicModel.getCurrentPlayer().equals(p2)) {
                    AssistantCard card1 = AssistantCard.values()[(turn + 1) % 10];
                    assertDoesNotThrow(() -> model.publicModel.playAssistant(card1));
                } else {
                    AssistantCard card1 = AssistantCard.values()[(turn + 2) % 10];
                    assertDoesNotThrow(() -> model.publicModel.playAssistant(card1));
                }
                model.publicModel.endTurn();
            }


            assertEquals(p1, model.publicModel.getCurrentPlayer());
            //P1 TURN
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

            //P3 Turn
            assertEquals(p3, model.publicModel.getCurrentPlayer());

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

            assertThrows(CloudEmptyException.class, () -> model.publicModel.drawStudentsIntoEntrance(0));

            assertDoesNotThrow(() -> model.publicModel.drawStudentsIntoEntrance(1));
            assertThrows(TooMuchStepsException.class, () -> model.publicModel.moveMotherNature(10));
            assertDoesNotThrow(() -> model.publicModel.moveMotherNature(1));
            model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());
            if (model.publicModel.getWinner() != null)
                break;

            writePlayerDiningRoom(p3, model);
            model.publicModel.endTurn();


            //P2 Turn
            assertEquals(p2, model.publicModel.getCurrentPlayer());

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
            assertThrows(CloudEmptyException.class, () -> model.publicModel.drawStudentsIntoEntrance(1));
            assertDoesNotThrow(() -> model.publicModel.drawStudentsIntoEntrance(2));
            model.publicModel.endTurn();

            //P0 Turn
            assertEquals(p0, model.publicModel.getCurrentPlayer());

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
            assertThrows(CloudEmptyException.class, () -> model.publicModel.drawStudentsIntoEntrance(1));
            assertThrows(CloudEmptyException.class, () -> model.publicModel.drawStudentsIntoEntrance(2));
            assertDoesNotThrow(() -> model.publicModel.drawStudentsIntoEntrance(3));
            model.publicModel.endTurn();


            if (model.publicModel.getWinner() != null)
                break;
            assertEquals(p1, model.publicModel.getCurrentPlayer());


        } while (true);
        Player win = null;
        if (p0.getBoard().getTowers().size() == 0) {
            assertNotEquals(0, p2.getBoard().getTowers().size());
            win = p0;
        } else {
            win = p2;
        }
        System.out.println(win.getNickname() + " WINS");
        assertEquals(win, model.publicModel.getWinner());


    }

    @Test
    public void fourPlayersEasyMatchTestSecondTeamWinning() {
        System.out.println("---------------- Four Players Easy Match -----------------");
        Model model = fourPlayersTestAllRed();

        Player p0 = getPlayers(model).get(0);

        Player p1 = getPlayers(model).get(1);

        Player p2 = getPlayers(model).get(2);

        Player p3 = getPlayers(model).get(3);


        int turn = 0;

        do {

            System.out.println("--------- TURN" + (++turn) + " ------------");
            writeTable(model);
            for (int i = 0; i < 4; i++) {
                if (model.publicModel.getCurrentPlayer().equals(p3)) {
                    AssistantCard card2 = AssistantCard.values()[(turn - 1) % 10];
                    assertDoesNotThrow(() -> model.publicModel.playAssistant(card2));
                } else if (model.publicModel.getCurrentPlayer().equals(p1)) {
                    AssistantCard card = AssistantCard.values()[(turn) % 10];
                    assertDoesNotThrow(() -> model.publicModel.playAssistant(card));

                } else if (model.publicModel.getCurrentPlayer().equals(p2)) {
                    AssistantCard card1 = AssistantCard.values()[(turn + 1) % 10];
                    assertDoesNotThrow(() -> model.publicModel.playAssistant(card1));
                } else {
                    AssistantCard card1 = AssistantCard.values()[(turn + 2) % 10];
                    assertDoesNotThrow(() -> model.publicModel.playAssistant(card1));
                }
                model.publicModel.endTurn();
            }


            assertEquals(p3, model.publicModel.getCurrentPlayer());
            //P3 TURN
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

            //P1 Turn
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

            assertThrows(CloudEmptyException.class, () -> model.publicModel.drawStudentsIntoEntrance(0));

            assertDoesNotThrow(() -> model.publicModel.drawStudentsIntoEntrance(1));
            assertThrows(TooMuchStepsException.class, () -> model.publicModel.moveMotherNature(10));
            assertDoesNotThrow(() -> model.publicModel.moveMotherNature(1));
            model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());
            if (model.publicModel.getWinner() != null)
                break;

            writePlayerDiningRoom(p3, model);
            model.publicModel.endTurn();


            //P2 Turn
            assertEquals(p2, model.publicModel.getCurrentPlayer());

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
            assertThrows(CloudEmptyException.class, () -> model.publicModel.drawStudentsIntoEntrance(1));
            assertDoesNotThrow(() -> model.publicModel.drawStudentsIntoEntrance(2));
            model.publicModel.endTurn();

            //P0 Turn
            assertEquals(p0, model.publicModel.getCurrentPlayer());

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
            assertThrows(CloudEmptyException.class, () -> model.publicModel.drawStudentsIntoEntrance(1));
            assertThrows(CloudEmptyException.class, () -> model.publicModel.drawStudentsIntoEntrance(2));
            assertDoesNotThrow(() -> model.publicModel.drawStudentsIntoEntrance(3));
            model.publicModel.endTurn();


            if (model.publicModel.getWinner() != null)
                break;
            assertEquals(p3, model.publicModel.getCurrentPlayer());


        } while (true);
        Player win = null;
        if (p0.getBoard().getTowers().size() == 0) {
            assertNotEquals(0, p2.getBoard().getTowers().size());
            win = p0;
        } else {
            win = p2;
        }
        System.out.println(win.getNickname() + " WINS");
        assertEquals(win, model.publicModel.getWinner());


    }

    /*
     *   Create threePlayersEasyMatch
     *   p1 plays a card so that they play first then p2 then p0
     *   p1 places students in their dining room or if the dining room is full they place them on the island next to motherNature
     *   p1 moves motherNature by 1
     *   p2 places students in the island next to motherNature
     *   p2 moves motherNature by 1
     *   p0 places students in the island next to motherNature
     *   p0 moves motherNature by 1
     *   keep repeating these moves until someone wins
     *   check that the winner is p1
     * */

    @Test
    public void threePlayersEasyMatchTest() {
        System.out.println("---------------- Three Players Easy Match -----------------");
        Model model = threePlayersTestAllRed();

        Player p0 = getPlayers(model).get(0);

        Player p1 = getPlayers(model).get(1);

        Player p2 = getPlayers(model).get(2);


        int turn = 0;

        do {

            System.out.println("--------- TURN" + (++turn) + " ------------");
            writeTable(model);
            for (int i = 0; i < 3; i++) {
                if (model.publicModel.getCurrentPlayer().equals(p0)) {
                    AssistantCard card2 = AssistantCard.values()[(turn + 1) % 10];
                    assertDoesNotThrow(() -> model.publicModel.playAssistant(card2));
                } else if (model.publicModel.getCurrentPlayer().equals(p1)) {
                    AssistantCard card = AssistantCard.values()[(turn - 1) % 10];
                    assertDoesNotThrow(() -> model.publicModel.playAssistant(card));

                } else {
                    AssistantCard card1 = AssistantCard.values()[(turn) % 10];
                    assertDoesNotThrow(() -> model.publicModel.playAssistant(card1));
                }
                model.publicModel.endTurn();
            }


            assertEquals(p1, model.publicModel.getCurrentPlayer());
            //P1 TURN
            int i = 0;
            for (Color c : Color.values()) {
                while (i < 4) {
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
            assertEquals(4, i);
            assertDoesNotThrow(() -> model.publicModel.drawStudentsIntoEntrance(0));
            assertThrows(TooMuchStepsException.class, () -> model.publicModel.moveMotherNature(10));
            assertDoesNotThrow(() -> model.publicModel.moveMotherNature(1));
            model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());
            if (model.publicModel.getWinner() != null)
                break;

            writePlayerDiningRoom(p0, model);
            model.publicModel.endTurn();
            //P2 Turn
            assertEquals(p2, model.publicModel.getCurrentPlayer());

            i = 0;
            for (Color c : Color.values()) {
                while (i < 4) {
                    try {
                        model.publicModel.moveStudentToIsland(c, (getIslands(model).indexOf(model.publicModel.getMotherNatureIsland()) + 1) % (getIslands(model).size()));
                        i++;
                    } catch (NotFoundException e) {
                        break;
                    }
                }
            }
            assertEquals(4, i);
            assertDoesNotThrow(() -> model.publicModel.moveMotherNature(1));
            model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());

            writeTable(model);
            if (model.publicModel.getWinner() != null)
                break;
            assertThrows(CloudEmptyException.class, () -> model.publicModel.drawStudentsIntoEntrance(0));
            assertDoesNotThrow(() -> model.publicModel.drawStudentsIntoEntrance(1));
            model.publicModel.endTurn();

            //P0 Turn
            assertEquals(p0, model.publicModel.getCurrentPlayer());

            i = 0;
            for (Color c : Color.values()) {
                while (i < 4) {
                    try {
                        model.publicModel.moveStudentToIsland(c, (getIslands(model).indexOf(model.publicModel.getMotherNatureIsland()) + 1) % (getIslands(model).size()));
                        i++;
                    } catch (NotFoundException e) {
                        break;
                    }
                }
            }
            assertEquals(4, i);
            assertDoesNotThrow(() -> model.publicModel.moveMotherNature(1));
            model.publicModel.updateIslandOwner(model.publicModel.getMotherNatureIsland());

            writeTable(model);
            if (model.publicModel.getWinner() != null)
                break;
            assertThrows(CloudEmptyException.class, () -> model.publicModel.drawStudentsIntoEntrance(0));
            assertThrows(CloudEmptyException.class, () -> model.publicModel.drawStudentsIntoEntrance(1));
            assertDoesNotThrow(() -> model.publicModel.drawStudentsIntoEntrance(2));
            model.publicModel.endTurn();
            if (model.publicModel.getWinner() != null)
                break;
            assertEquals(p1, model.publicModel.getCurrentPlayer());


        } while (true);

        assertEquals(p1, model.publicModel.getWinner());


    }


}
