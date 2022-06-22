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


    /**
     * @param card assistant card we want to play
     * @throws NotFoundException assistant card not found in deck
     */
    public void playAssistantCard(AssistantCard card) throws NotFoundException {
        if (cards.contains(card)) {
            cards.remove(card);
            return;
        }

        throw new NotFoundException("Card not in Deck");
    }

    /**
     * @return list of assistant cards in deck
     */
    public ArrayList<AssistantCard> getHand() {
        return new ArrayList<>(cards);
    }

    public DeckName getDeckName() {
        return deckName;
    }

    /**
     * @return true if there are no assistant cards in deck, otherwise false
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
