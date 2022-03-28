package it.polimi.ingsw.cards;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.DeckName;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    List<AssistantCard> cards;

    DeckName deckName;

    public Deck(DeckName deckName) {
        this.deckName = deckName;
        cards = new ArrayList<>(List.of(AssistantCard.values()));

    }

    //Default deck definition, just for testing
    public Deck() {
        this(DeckName.DESERT_KING);
    }

    public void playAssistantCard(AssistantCard card) throws Exception {
        if (cards.contains(card)) {
            cards.remove(card);
            return;
        }

        throw new Exception("Card not in Deck");
    }

    public ArrayList<AssistantCard> getHand() {
        return new ArrayList<>(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
