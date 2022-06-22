package it.polimi.ingsw.mvc.view.GUI;

import it.polimi.ingsw.enums.GameMode;

public class TextOutputConstants {
    public static String notMyTurn(String currentPlayerNickname) {
        return "Wait for " + currentPlayerNickname + " to end their turn.";
    }

    public static String planningPhase() {
        return "Choose an assistant card to play by clicking on it.";
    }

    public static String actionPhase(GameMode gm, boolean alreadyPlayedCharacterCard, boolean enoughStudentsMoved, boolean motherNatureMoved) {
        String returnString;
        if (!enoughStudentsMoved)
            returnString = "Drag a student to place it on an island or in the dining room. ";
        else if (!motherNatureMoved)
            returnString = "Click on an island to move MotherNature. ";
        else
            returnString = "Click on a cloud to get Students. ";
        if (gm.equals(GameMode.EXPERT_MODE) && !alreadyPlayedCharacterCard)
            returnString += "Click on a character card to play it.";
        return returnString;
    }

}
