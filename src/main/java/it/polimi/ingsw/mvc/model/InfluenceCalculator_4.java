package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;

public class InfluenceCalculator_4 extends InfluenceCalculator {
    public InfluenceCalculator_4(Model fatherModel) {
        super(fatherModel);
    }

    /**
     * @param island Island in consideration
     * @return null if tie;
     * otherwise the board of the player of the time that has the towers (only one of the team has the towers)
     */
    @Override
    Board getInfluence(Island island) {
        // TODO: Implementation upon this.influenceRules
        return null;
    }
}
