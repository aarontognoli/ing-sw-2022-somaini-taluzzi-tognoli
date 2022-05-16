package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;

import java.io.Serializable;
/*
    Modified effects:
    1. CentaurCharacter  : Tower have no influence
    2. KnightCharacter   : Two more influence for currentPlayer
    3. MushroomCharacter : The chosen color's students have no influence
*/

public abstract class InfluenceCalculator implements Serializable {
    protected final Model fatherModel;
    protected InfluenceCalculatorRules influenceRules;

    public InfluenceCalculator(Model fatherModel) {
        this.fatherModel = fatherModel;
        this.influenceRules = new DefaultInfluenceCalculatorRules(fatherModel);
    }

    public void setInfluenceCalculatorRules(InfluenceCalculatorRules newRules) {
        influenceRules = newRules;
    }

    /**
     * @param island Island in consideration
     * @return null if tie, otherwise return the board of the player with the maximum influence
     * When 4
     */
    abstract Board getInfluence(Island island);
}
