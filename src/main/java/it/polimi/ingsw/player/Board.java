package it.polimi.ingsw.player;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.pawn.Tower;
import it.polimi.ingsw.pawn.Student;

import java.util.ArrayList;
import java.util.List;

public class Board {

    static private final int DINING_ROOMS_COUNT = 5;
    static private final int DINING_ROOM_MAX_STUDENT_COUNT = 10;

    private List<Tower> towers;
    private List<List<Student>> diningRoom = new ArrayList<List<Student>>(DINING_ROOMS_COUNT);
    private List<Student> entrance;
    private int coinCount;

    public Board() {
        towers = new ArrayList<Tower>();
        entrance = new ArrayList<Student>();
        coinCount = 0;
        for (int i = 0; i < DINING_ROOMS_COUNT; i++) {
            diningRoom.set(i, new ArrayList<Student>());
        }
    }

    public Tower removeTower() throws Exception {
        if (towers.size() > 0) {
            return towers.remove(towers.size() - 1);
        } else
            throw new Exception("No towers, end the game");
    }

    public void addStudentsToEntrance(List<Student> newStudents) {
        entrance.addAll(newStudents);
    }

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
