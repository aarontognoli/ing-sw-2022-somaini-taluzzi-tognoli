package it.polimi.ingsw.cards;

import it.polimi.ingsw.enums.DeckName;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    List<AssistantCard> cards;

    DeckName deckName;
    public Deck(DeckName deckName)
    {
        this.deckName = deckName;
        cards = new ArrayList<>();
        //TODO: create list of cards, waiting for AssistantCards definitive implementation
    }

    //Default deck definition, just for testing
    public Deck() {
        this(DeckName.DESERT_KING);
    }
}
