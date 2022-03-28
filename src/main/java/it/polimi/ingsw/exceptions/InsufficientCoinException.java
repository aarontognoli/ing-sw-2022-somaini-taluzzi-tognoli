package it.polimi.ingsw.exceptions;

public class InsufficientCoinException extends Exception {
    public InsufficientCoinException(int currentCoins, int neededCoins) {
        super("Insufficient amount of coins to play character card. Playes has "
                + currentCoins + " coins but they need " + neededCoins
        );
    }

}
