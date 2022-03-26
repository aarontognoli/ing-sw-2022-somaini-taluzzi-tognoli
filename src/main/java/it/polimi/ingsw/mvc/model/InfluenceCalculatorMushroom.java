package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.enums.Color;

public class InfluenceCalculatorMushroom extends DefaultInfluenceCalculatorRules {
    private final Color targetColor;

    public InfluenceCalculatorMushroom(Model fatherModel, Color targetColor) {
        super(fatherModel);
        this.targetColor = targetColor;
    }

    @Override
    boolean hasProfessorOfColor(int playerIndex, Color color) {
        if (color.equals(targetColor))
            return false;

        return super.hasProfessorOfColor(playerIndex, color);
    }
}
