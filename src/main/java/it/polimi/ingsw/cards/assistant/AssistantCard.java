package it.polimi.ingsw.cards.assistant;

import java.io.Serializable;

public enum AssistantCard implements Serializable {

    CARD_1(1, 1),
    CARD_2(2, 1),
    CARD_3(3, 2),
    CARD_4(4, 2),
    CARD_5(5, 3),
    CARD_6(6, 3),
    CARD_7(7, 4),
    CARD_8(8, 4),
    CARD_9(9, 5),
    CARD_10(10, 5);


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
}

