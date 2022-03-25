package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.pawn.Tower;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.Player;
/*
    Modified effects:
    1. CentaurCharacter  : Tower have no influence
    2. KnightCharacter   : Two more influence for currentPlayer
    3. MushroomCharacter : The chosen color's students have no influnce 
*/

public abstract class InfluenceCalculator {

    protected Model fatherModel;

    public InfluenceCalculator(Model model) {
        fatherModel = model;
    }

    // TODO: These rules apply only for 2-3 players
    Board getInfluence(Island island) {
        Player playerMaxInfluence = null;
        int maxInfluence = -1;
        boolean tie = false;
        int currentPlayerInfluence;

        for (int playerIndex = 0; playerIndex < fatherModel.totalPlayerCount; playerIndex++) {
            currentPlayerInfluence = 0;

            // Compute tower influence
            // [!] CentaurCharacter denies tower influence
            currentPlayerInfluence += getTowerInfluence(playerIndex, island);

            boolean[] currentPlayerHasProfessor = new boolean[Color.values().length];
            for (Color color : Color.values()) {
                // Check if this player has the professor of this color
                // [!] MushroomCharacter denies the influence of a color
                currentPlayerHasProfessor[color.ordinal()] = hasProfessorOfColor(playerIndex, color);
            }

            for (Student s : island.getStudents()) {
                if (currentPlayerHasProfessor[s.getColor().ordinal()]) {
                    currentPlayerInfluence++;
                }
            }

            currentPlayerInfluence += incrementInfluence(playerIndex);

            if (currentPlayerInfluence > maxInfluence) {
                tie = false;
                maxInfluence = currentPlayerInfluence;
                playerMaxInfluence = fatherModel.players.get(playerIndex);
            } else if (currentPlayerInfluence == maxInfluence) {
                tie = true;
            }
        }

        if (tie)
            return null;

        return playerMaxInfluence.getBoard();
    }

    abstract int getTowerInfluence(int playerIndex, Island island);

    abstract boolean hasProfessorOfColor(int playerIndex, Color color);

    abstract int incrementInfluence(int playerIndex);
}
