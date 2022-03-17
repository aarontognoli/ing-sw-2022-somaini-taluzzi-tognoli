package it.polimi.ingsw.cards.assistant;

public enum AssistantCardID {
    CARD_1(0),
    CARD_2(1);
    // TODO: add them all, check the index in AssistantCardSingletons


    private int index;

    public int getIndex() {
        return index;
    }

    AssistantCardID(int index) {
        this.index = index;
    }
}
