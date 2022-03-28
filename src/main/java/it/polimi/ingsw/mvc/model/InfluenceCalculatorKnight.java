package it.polimi.ingsw.mvc.model;

public class InfluenceCalculatorKnight extends DefaultInfluenceCalculatorRules {
    private static final int KNIGHT_INCREMENT_AMOUNT = 2;

    private final int targetPlayerIndex;

    public InfluenceCalculatorKnight(Model fatherModel, int targetPlayerIndex) {
        super(fatherModel);
        this.targetPlayerIndex = targetPlayerIndex;
    }

    @Override
    int incrementInfluence(int playerIndex) {
        return playerIndex == targetPlayerIndex ? KNIGHT_INCREMENT_AMOUNT : 0;
    }
}
