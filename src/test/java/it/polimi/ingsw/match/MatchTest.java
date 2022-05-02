package it.polimi.ingsw.match;

import it.polimi.ingsw.exceptions.NoTowerException;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Player;

import static it.polimi.ingsw.mvc.model.PublicModelTest.*;

public class MatchTest {
    //TODO fix this
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



}
