package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
/*
    Modified effects:
    1. CentaurCharacter  : Tower have no influence
    2. KnightCharacter   : Two more influence for currentPlayer
    3. MushroomCharacter : The chosen color's students have no influence
*/

public abstract class InfluenceCalculator {
    protected final Model fatherModel;
    protected final InfluenceCalculatorRules influenceRules;

    public InfluenceCalculator(Model fatherModel, InfluenceCalculatorRules influenceRules) {
        this.fatherModel = fatherModel;
        this.influenceRules = influenceRules;
    }

    /**
     * @param island Island in consideration
     * @return null if tie, otherwise return the board of the player with the maximum influence
     * When 4
     */
    abstract Board getInfluence(Island island);
}
