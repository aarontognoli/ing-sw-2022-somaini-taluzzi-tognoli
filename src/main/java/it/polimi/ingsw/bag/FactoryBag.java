package it.polimi.ingsw.bag;

public class FactoryBag {
    public static Bag createBagForIsland() {
        return new Bag(2);
    }

    public static Bag createBagForMatch() {
        // In the game there are 26 students for each color
        // 2 are used in the game preparation, the rest are used for the match
        return new Bag(24);
    }
}
