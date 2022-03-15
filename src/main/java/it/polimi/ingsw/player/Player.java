package it.polimi.ingsw.player;

import it.polimi.ingsw.cards.Deck;

import java.util.Objects;

public class Player {

    final private Board board;
    final private String nickname;
    private Deck deck;

    public Player(String nickname) {
        this.nickname = nickname;

        board = new Board();
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public String getNickname() {
        return nickname;
    }

    Board getBoard() {
        return board;
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
