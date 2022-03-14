package it.polimi.ingsw.cards;

public class AssistantCard {
    private final int turnOrderValue;
    private final int maxMotherNatureMovementValue;

    public AssistantCard(int turnOrderValue, int maxMotherNatureMovementValue) {
        this.turnOrderValue = turnOrderValue;
        this.maxMotherNatureMovementValue = maxMotherNatureMovementValue;
    }

    public int getTurnOrderValue() {
        return turnOrderValue;
    }

    public int getMaxMotherNatureMovementValue() {
        return maxMotherNatureMovementValue;
    }
}
