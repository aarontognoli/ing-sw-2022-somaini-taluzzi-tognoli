package it.polimi.ingsw.player;

import it.polimi.ingsw.cards.Deck;
import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.exceptions.NotFoundException;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {

    final private Board board;
    final private String nickname;
    final private TowerColor towerColor;

    private Deck deck;
    private AssistantCard currentAssistantCard;
    private int maxMotherNatureMovementValue;

    public Player(String nickname, TowerColor towerColor, DeckName deckName, int towersNumber) {
        this.nickname = nickname;
        this.towerColor = towerColor;
        currentAssistantCard = null;

        board = new Board(towerColor, towersNumber);
        setDeck(new Deck(deckName));
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setCurrentAssistantCard(AssistantCard currentAssistantCard) throws NotFoundException {
        deck.playAssistantCard(currentAssistantCard);
        this.currentAssistantCard = currentAssistantCard;
        this.maxMotherNatureMovementValue = currentAssistantCard.getMaxMotherNatureMovementValue();
    }

    public void setMaxMotherNatureMovementValue(int maxMotherNatureMovementValue) {
        this.maxMotherNatureMovementValue = maxMotherNatureMovementValue;
    }

    public int getMaxMotherNatureMovementValue() {
        return maxMotherNatureMovementValue;
    }

    public AssistantCard getCurrentAssistantCard() {
        return currentAssistantCard;
    }

    public void draftAssistantCard() {
        currentAssistantCard = null;
    }

    public String getNickname() {
        return nickname;
    }

    public Board getBoard() {
        return board;
    }

    public Deck getDeck() {
        return deck;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Player player = (Player) o;
        return nickname.equals(player.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }
}
