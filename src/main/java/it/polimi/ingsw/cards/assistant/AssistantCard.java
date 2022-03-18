package it.polimi.ingsw.cards.assistant;

public enum AssistantCard {
    // TODO: Add all the cards
    CARD_1(3, 5),
    CARD_2(5, 2);


    private final int turnOrderValue;
    private final int maxMotherNatureMovementValue;

    AssistantCard(int turnOrderValue, int maxMotherNatureMovementValue) {
        this.turnOrderValue = turnOrderValue;
        this.maxMotherNatureMovementValue = maxMotherNatureMovementValue;
    }

    public int getTurnOrderValue() {
        return turnOrderValue;
    }

    public int getMaxMotherNatureMovementValue() {
        return maxMotherNatureMovementValue;
    }

    public static AssistantCard getCardByIndex(int ordinalIndex) {
        return AssistantCard.values()[ordinalIndex];
    }

    public static int getIndexByCard(AssistantCard card) {
        return card.ordinal();
    }
}
