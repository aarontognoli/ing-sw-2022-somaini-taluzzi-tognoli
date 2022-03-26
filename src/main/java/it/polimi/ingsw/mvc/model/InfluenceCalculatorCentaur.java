package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.places.Island;

public class InfluenceCalculatorCentaur extends DefaultInfluenceCalculatorRules {
    public InfluenceCalculatorCentaur(Model fatherModel) {
        super(fatherModel);
    }

    @Override
    int getTowerInfluence(int playerIndex, Island island) {
        return 0;
    }
}
