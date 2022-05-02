package it.polimi.ingsw.messages.lobby.client;

import it.polimi.ingsw.enums.DeckName;

public class SetDeckMessage extends ClientLobbyMessage {
    final private DeckName deckName;

    public SetDeckMessage(DeckName deckName) {
        this.deckName = deckName;
    }

    public DeckName getDeckName() {
        return deckName;
    }
}
