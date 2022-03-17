package it.polimi.ingsw.cards.assistant;

public class AssistantCardSingletons {
    private final static AssistantCard[] cards = {
            new AssistantCard(5, 3),
            new AssistantCard(3, 6),
    };

    public static AssistantCard getCardById(AssistantCardID cardId) {
        return cards[cardId.getIndex()];
    }
}
