package it.polimi.ingsw.player;

import it.polimi.ingsw.pawn.Tower;
import it.polimi.ingsw.pawn.Student;

import java.util.ArrayList;
import java.util.List;

public class Board {

    static private final int DINING_ROOM_CAPACITY = 5;

    private List<Tower> towers;
    private List<List<Student>> diningRoom = new ArrayList<List<Student>>(DINING_ROOM_CAPACITY);
    private List<Student> entrance;
    private int coinCount;

    public Board() {
        towers = new ArrayList<Tower>();
        entrance = new ArrayList<Student>();
        coinCount = 0;
        for (int i = 0; i < DINING_ROOM_CAPACITY; i++) {
            diningRoom.set(i, new ArrayList<Student>());
        }
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public List<List<Student>> getDiningRoom() {
        return diningRoom;
    }

    public List<Student> getEntrance() {
        return entrance;
    }

    public void rewardCoin() {
        coinCount++;
    }

    public void useCoins(int amount) throws Exception {
        if (amount > coinCount) {
            throw new Exception("Not enough coins");
        }
        coinCount -= amount;
    }
}
