package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.pawn.Tower;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.Player;
/*
    Modified effects:
    1. CentaurCharacter: Tower have no influence
    2. KnightCharacter: Two more influence for currentPlayer
    3. MushroomCharacter: The chosen color's students have no influnce 
*/

public abstract class InfluenceCalculator {
    Board getInfluence(Island island) {
        Player maxInfluencePlayer = null;
        int maxPlayerInfluence = 0;
        Boolean tie = false;
        int currentPlayerInfluence;

        Model fatherModel;

        for (int i = 0; i < fatherModel.totalPlayerCount; i++) {
            currentPlayerInfluence = 0;
            
            currentPlayerInfluence += getTowerInfluence();
            
            boolean[] currentPlayerHasProfessor = new boolean[Color.values().length];

            for (int colorIndex = 0; colorIndex < Color.values().length; colorIndex++) {
                currentPlayerHasProfessor[colorIndex] = hasProfessorOfColor(i, Color.values()[colorIndex]);
            }

            for (Student s : island.getStudents()) {
                if (currentPlayerHasProfessor[s.getColor().ordinal()]) {
                    currentPlayerInfluence++;
                }
            }
            if (currentPlayerInfluence > maxPlayerInfluence || maxInfluencePlayer == null) {
                maxPlayerInfluence = currentPlayerInfluence;
                maxInfluencePlayer = fatherModel.players.get(i);
                tie = false;
            } else if (currentPlayerInfluence == maxPlayerInfluence) {
                tie = true;
            }

            currentPlayerInfluence += incrementInfluence(i);
        }



        if (tie)
            return null;
        return maxInfluencePlayer.getBoard();
    }

    abstract int getTowerInfluence() {
    }

    abstract int getStudentsInfluence() {

    }

    abstract boolean hasProfessorOfColor(int playerIndex, Color color) {
    }
}
