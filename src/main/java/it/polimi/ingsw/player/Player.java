package it.polimi.ingsw.player;

public class Player {

    final private Board board;
    final private String nickname;

    public Player(String nickname) {
        this.nickname = nickname;

        board = new Board();
    }

    String getNickname() {
        return nickname;
    }

    Board getBoard() {
        return board;
    }
}
