package it.polimi.ingsw.cards;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.exceptions.NotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Deck implements Serializable {
    List<AssistantCard> cards;

    DeckName deckName;

    public Deck(DeckName deckName) {
        this.deckName = deckName;
        cards = new ArrayList<>(List.of(AssistantCard.values()));

    }


    public void playAssistantCard(AssistantCard card) throws NotFoundException {
        if (cards.contains(card)) {
            cards.remove(card);
            return;
        }

        throw new NotFoundException("Card not in Deck");
    }

    public ArrayList<AssistantCard> getHand() {
        return new ArrayList<>(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
