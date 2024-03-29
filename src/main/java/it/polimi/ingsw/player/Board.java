package it.polimi.ingsw.player;

import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.exceptions.DiningRoomFullException;
import it.polimi.ingsw.exceptions.EntranceFullException;
import it.polimi.ingsw.exceptions.InsufficientCoinException;
import it.polimi.ingsw.exceptions.NoTowerException;
import it.polimi.ingsw.pawn.Tower;
import it.polimi.ingsw.pawn.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {

    static private final int DINING_ROOMS_COUNT = 5;
    static public final int DINING_ROOM_MAX_STUDENT_COUNT = 10;

    final private List<Tower> towers;
    final private List<List<Student>> diningRoom = new ArrayList<>(DINING_ROOMS_COUNT);
    final private List<Student> entrance;
    private int coinCount;

    public Board() {
        towers = new ArrayList<>();
        entrance = new ArrayList<>();
        coinCount = 0;

        for (int i = 0; i < DINING_ROOMS_COUNT; i++) {
            diningRoom.add(new ArrayList<>());
        }
    }

    public Board(TowerColor towerColor, int towersNumber) {
        this();
        for (int i = 0; i < towersNumber; i++)
            towers.add(new Tower(towerColor));
    }

    /**
     * @return tower removed from the board
     * @throws NoTowerException there are no towers on the board
     */
    public Tower removeTower() throws NoTowerException {
        if (towers.size() > 0) {
            return towers.remove(towers.size() - 1);
        } else
            throw new NoTowerException("No towers, end the game");
    }

    /**
     * @param newStudents list of students to add to the entrance
     * @throws EntranceFullException entrance is full
     */
    public void addStudentsToEntrance(List<Student> newStudents) throws EntranceFullException {
        if (entrance.size() + newStudents.size() > 9) {
            throw new EntranceFullException();
        }
        entrance.addAll(newStudents);
    }

    /**
     * @param newStudent student to add to the dining room
     * @throws DiningRoomFullException dining room is full
     */
    public void addStudentsToDiningRoom(Student newStudent) throws DiningRoomFullException {
        List<Student> targetDiningRoom = diningRoom.get(newStudent.getColor().ordinal());

        if (targetDiningRoom.size() == DINING_ROOM_MAX_STUDENT_COUNT) {
            throw new DiningRoomFullException(newStudent.getColor());
        }

        targetDiningRoom.add(newStudent);
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

    public int getCoinCount() {
        return coinCount;
    }

    /**
     * Increments the coin number
     */
    public void rewardCoin() {
        coinCount++;
    }

    /**
     * @param amount number of coins we want to use
     * @throws InsufficientCoinException the amount of coins we want to use
     *                                   is greater than coins we currently have
     */
    public void useCoins(int amount) throws InsufficientCoinException {
        if (amount > coinCount) {
            throw new InsufficientCoinException(coinCount, amount);
        }
        coinCount -= amount;
    }

    public TowerColor getTowerColor() throws NoTowerException {
        if (towers.size() == 0)
            throw new NoTowerException("No tower found in board");

        return towers.get(0).getColor();
    }
}
