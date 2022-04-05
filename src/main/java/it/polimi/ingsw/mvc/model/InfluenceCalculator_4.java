package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;

public class InfluenceCalculator_4 extends InfluenceCalculator {
    public InfluenceCalculator_4(Model fatherModel) {
        super(fatherModel);
    }

    /**
     * @param island Island in consideration
     * @return null if tie;
     *         otherwise the board of the player of the time that has the towers
     *         (only one of the team has the towers)
     */
    @Override
    Board getInfluence(Island island) {
        int currentPlayerInfluence;

        int teamOneInfluence = 0;
        int teamTwoInfluence = 0;

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

            if (playerIndex <= 1) {
                teamOneInfluence += currentPlayerInfluence;
            } else {
                teamTwoInfluence += currentPlayerInfluence;
            }
        }

        if (teamOneInfluence == teamTwoInfluence) {
            return null;
        } else if (teamOneInfluence > teamTwoInfluence) {
            return fatherModel.players.get(0).getBoard();
        } else {
            return fatherModel.players.get(2).getBoard();
        }
    }
}
