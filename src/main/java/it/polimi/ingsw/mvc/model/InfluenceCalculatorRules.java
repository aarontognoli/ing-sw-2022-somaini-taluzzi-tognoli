package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.places.Island;

import java.io.Serializable;

public abstract class InfluenceCalculatorRules implements Serializable {

    final Model fatherModel;

    public InfluenceCalculatorRules(Model fatherModel) {
        this.fatherModel = fatherModel;
    }

    abstract int getTowerInfluence(int playerIndex, Island island);

    abstract boolean hasProfessorOfColor(int playerIndex, Color color);

    abstract int incrementInfluence(int playerIndex);
}
