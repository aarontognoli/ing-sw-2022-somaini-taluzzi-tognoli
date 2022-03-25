package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.Player;

public class InfluenceCalculator_2_3 extends InfluenceCalculator {

    public InfluenceCalculator_2_3(Model model, InfluenceCalculatorRules influenceCalculatorRules) {
        super(model, influenceCalculatorRules);
    }

    Board getInfluence(Island island) {
        Player playerMaxInfluence = null;
        int maxInfluence = -1;
        boolean tie = false;
        int currentPlayerInfluence;

        for (int playerIndex = 0; playerIndex < fatherModel.totalPlayerCount; playerIndex++) {
            currentPlayerInfluence = 0;

            // Compute tower influence
            // [!] CentaurCharacter denies tower influence
            currentPlayerInfluence += influenceRules.getTowerInfluence(playerIndex, island);

            boolean[] currentPlayerHasProfessor = new boolean[Color.values().length];
            for (Color color : Color.values()) {
                // Check if this player has the professor of this color
                // [!] MushroomCharacter denies the influence of a color
                currentPlayerHasProfessor[color.ordinal()] = influenceRules.hasProfessorOfColor(playerIndex, color);
            }

            for (Student s : island.getStudents()) {
                if (currentPlayerHasProfessor[s.getColor().ordinal()]) {
                    currentPlayerInfluence++;
                }
            }

            currentPlayerInfluence += influenceRules.incrementInfluence(playerIndex);

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

        if (playerMaxInfluence == null) {
            // Should not happen
            throw new RuntimeException("No tie nor max influence player during calculation of influence");
        }
        return playerMaxInfluence.getBoard();
    }
}
