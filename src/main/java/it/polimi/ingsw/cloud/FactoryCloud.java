package it.polimi.ingsw.cloud;

public class FactoryCloud {
    public static Cloud createTwoFourPlayersCloud() {
        // Three students per cloud when there are 2 or 4 players
        return new Cloud(3);
    }

    public static Cloud createThreePlayersCloud() {
        // Four students per cloud when there are 3 players
        return new Cloud(4);
    }
}