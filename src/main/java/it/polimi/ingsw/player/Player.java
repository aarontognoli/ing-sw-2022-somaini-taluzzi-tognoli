package it.polimi.ingsw.player;

import it.polimi.ingsw.cards.Deck;
import it.polimi.ingsw.cards.assistant.AssistantCard;

import java.util.Objects;

public class Player {

    final private Board board;
    final private String nickname;
    private Deck deck;
    private AssistantCard currentAssistantCard;

    public Player(String nickname) {
        this.nickname = nickname;

        board = new Board();
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setCurrentAssistantCard(AssistantCard currentAssistantCard) {
        this.currentAssistantCard = currentAssistantCard;
    }

    public AssistantCard getCurrentAssistantCard() {
        return currentAssistantCard;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return nickname.equals(player.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }
}
